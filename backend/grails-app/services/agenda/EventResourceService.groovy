package agenda

import static agenda.Event.PIC_MAX_SIZE
import static agenda.LocalContext.getCurrentDateTime
import static agenda.LocalContext.jdkDateTimeToDateOnly
import static agenda.LocalContext.jdkDateTimeToDateTime
import static agenda.LocalContext.jdkDateTimeToString
import static agenda.LocalContext.jdkDateTimeToTimeOnly

import org.apache.commons.io.FilenameUtils

class EventResourceService {

    def sessionFactory
    def staticResourceService
    def securityAttackService

    def show(id) {
        def event = Event.get(id)
        if (event) {
            assertOwnerOfEvent(event)
            convert(event)
        }
    }

    def getReadonlyPdtpIds(event) {
        findAllReadOnlyPdtps(event.pdtps)*.id
    }

    def validate(event, readonlyPdtpIds=[]) {
        assertOwnerOfEvent(event)
        prepareForValidation(event, readonlyPdtpIds)
        event.validate()
    }

    def save(event, picId=null) {
        def sessionEvent = addToSession(event)
        if (picId) {
            saveImgIfUploaded(sessionEvent, picId)
        }
        convert(sessionEvent)
    }

    def update(event, picId=null) {
        deleteImgIfNeeded(event)
        if (picId) {
            saveImgIfUploaded(event, picId)
        }
        convert(event)
    }

    def checkIfCanDelete(event) {
        assertOwnerOfEvent(event)
        def cannotDelete = findAllReadOnlyPdtps(event.pdtps).size() > 0
        if (cannotDelete) {
            event.errors.reject('event.delete.cannotDelete')
        }
        !cannotDelete
    }

    def delete(event) {
        event.delete()
        true
    }

    private convert(event) {
        def pdtps = makePdtpsToShow(event.pdtps)
        def canDelete = !pdtps.any { it.readonly }
        [id: event.id, title: event.title, pic: staticResourceService.makeImageSrc(event.pic), more: event.more,
            description: event.description, category: [id: event.category.id], oneTimeType: event.oneTimeType,
            pdtps: pdtps, canDelete: canDelete]
    }

    private prepareForValidation(event, readonlyPdtpIds) {
        prepareEventForValidation(event, readonlyPdtpIds)
        preparePdtpsForValidation(event, readonlyPdtpIds)
    }

    private prepareEventForValidation(event, readonlyPdtpIds) {
        def cannotModifyTitle = readonlyPdtpIds.size() > 0
        if (cannotModifyTitle) {
            event.title = event.getPersistentValue('title')
        }
    }

    private preparePdtpsForValidation(event, readonlyPdtpIds) {
        def readonlyId
        event.pdtps.each { pdtp ->
            pdtp.event = event
            readonlyId = readonlyPdtpIds.find { it == pdtp.id }
            if (readonlyId) {
                readonlyPdtpIds.remove(readonlyId)
                if (pdtp.isDirty()) {
                    pdtp.refresh()
                }
            } else {
                pdtp.toDate = jdkDateTimeToDateOnly(pdtp.toDate).toDate()
                pdtp.fromDate = event.oneTimeType ? pdtp.toDate : jdkDateTimeToDateOnly(pdtp.fromDate).toDate()
                pdtp.startTime = jdkDateTimeToTimeOnly(pdtp.startTime).toDate()
            }
        }
        readonlyPdtpIds.each {
            int i = 0
            for (; i < event.pdtps.size(); ++i) {
                if (event.pdtps[i].id > it) {
                    break
                }
            }
            event.pdtps.add(i, Pdtp.get(it))
        }
    }

    private addToSession(event) {
        event.save(validate: false)
    }

    private checkConstraintsErrorsAsap() {
        sessionFactory.currentSession.flush()
    }

    private makePdtpsToShow(pdtps) {
        def now = currentDateTime
        pdtps.collect { [id: it.id, place: it.place, fromDate: jdkDateTimeToString(it.fromDate),
            toDate: jdkDateTimeToString(it.toDate), startTime: jdkDateTimeToString(it.startTime),
            timeDescription: it.timeDescription, readonly: isReadOnlyPdtp(it, now), price: it.price] }
    }

    private findAllReadOnlyPdtps(pdtps) {
        def now = currentDateTime
        pdtps.findAll { isReadOnlyPdtp(it, now) }
    }

    private isReadOnlyPdtp(pdtp, now) {
        jdkDateTimeToDateTime(pdtp.toDate, pdtp.startTime) < now
    }

    private saveImgIfUploaded(event, picId) {
        if (picId) {
            def imgName = event.pic = makeImgName(event.id, picId)
            checkConstraintsErrorsAsap()
            def imgCopied = false
            new OnTransaction(
                // if this throws an exception, transaction will be rolled back
                // if transaction is rolled back after this, we will end up with an orphaned image in image directory
                {
                    try {
                        staticResourceService.copyToImageDir(picId, event.pic)
                        imgCopied = true
                    } catch (e) {
                        log.error "Could not copy uploaded file $picId as $imgName", e
                        throw new RuntimeException(e)
                    }
                },
                null,
                {
                    if (imgCopied) {
                        log.warn "Uploaded file $picId was copied but rollback occurred; image $imgName is orphaned"
                    }
                }
            )
        }
    }

    private deleteImgIfNeeded(event) {
        def previousImgName = event.getPersistentValue('pic')
        if (event.pic) {
            event.pic = previousImgName
        } else if (previousImgName) {
            new OnTransaction(
                null,
                // if this throws an exception, we will end up with an orphaned image in image directory
                {
                    try {
                        staticResourceService.deleteImage(previousImgName)
                    } catch (e) {
                        log.warn "Could not delete image after commit; image $previousImgName is orphaned", e
                        throw e
                    }
                }
            )
        }
    }

    private makeImgName(eventId, picId) {
        def suggestedName = eventId + staticResourceService.getNameFromImageId(picId)
        if (suggestedName.size() > PIC_MAX_SIZE) {
            def extension = FilenameUtils.getExtension(suggestedName)
            extension = extension ? FilenameUtils.EXTENSION_SEPARATOR + extension : extension
            def baseName = suggestedName - extension
            baseName[0..-(suggestedName.size() - PIC_MAX_SIZE - 1)] + extension
        } else {
            suggestedName
        }
    }

    private assertOwnerOfEvent(event) {
        securityAttackService.assertOwner(event.institution.id)
    }
}
