package agenda

import static agenda.LocalContext.getCurrentDateTime
import static agenda.LocalContext.jdkDateTimeToDateOnly
import static agenda.LocalContext.jdkDateTimeToDateTime
import static agenda.LocalContext.jdkDateTimeToTimeOnly

class EventResourceService {

    def get(id) {
        def event = Event.get(id)
        if (event) {
            def pdtps = makePdtpsToShow(event.pdtps)
            def canDelete = !pdtps.any { it.readonly }
            [id: event.id, title: event.title, pic: event.pic, more: event.more, description: event.description,
                category: [id: event.category.id], oneTimeType: event.oneTimeType, pdtps: pdtps, canDelete: canDelete]
        }
    }

    def getReadonlyPdtpIds(event) {
        findAllReadOnlyPdtps(event.pdtps)*.id
    }

    def validate(event, readonlyPdtpIds) {
        prepareForValidation(event, readonlyPdtpIds)
        event.validate()
    }

    def save(event) {
        persistEvent(event)
    }

    def update(event) {
        persistEvent(event)
    }

    def checkIfCanDelete(event) {
        def cannotDelete = findAllReadOnlyPdtps(event.pdtps).size() > 0
        if (cannotDelete) {
            event.errors.reject('event.delete.cannotDelete')
        }
        !cannotDelete
    }

    def delete(event) {
        event.delete()
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
                pdtp.fromDate = jdkDateTimeToDateOnly(pdtp.fromDate).toDate()
                pdtp.toDate = jdkDateTimeToDateOnly(pdtp.toDate).toDate()
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

    private persistEvent(event) {
        get(event.save(validate: false)?.id)
    }

    private makePdtpsToShow(pdtps) {
        def now = currentDateTime
        pdtps.collect { [id: it.id, place: it.place, fromDate: it.fromDate, toDate: it.toDate, startTime: it.startTime,
            timeDescription: it.timeDescription, readonly: isReadOnlyPdtp(it, now), price: it.price] }
    }

    private findAllReadOnlyPdtps(pdtps) {
        def now = currentDateTime
        pdtps.findAll { isReadOnlyPdtp(it, now) }
    }

    private isReadOnlyPdtp(pdtp, now) {
        jdkDateTimeToDateTime(pdtp.toDate, pdtp.startTime) < now
    }
}
