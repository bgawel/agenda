package agenda

import static agenda.LocalContext.dateTimeToDateOnly
import static agenda.LocalContext.getCurrentDateTime
import static agenda.PresentationContext.printShortJdkDate
import grails.plugin.cache.Cacheable

import org.apache.commons.beanutils.BeanComparator

class EventRecommendationService {

    def maxNewestToRecommend = 3
    def maxComingSoonToRecommend = 5
    def dayOffsetForCommingSoon = 1
    def maxSizeOfDescToShow = 64
    def entryComparator = new BeanComparator('fromDate')

    def pdtpQueryService
    def categoryMenuService
    def institutionMenuService

    @Cacheable('newlyAdded')
    def getNewlyAdded(categoryId, instId) {
        getNewlyAdded(currentDateTime, categoryId, instId)
    }

    def getComingSoon(categoryId, instId) {
        getComingSoon(currentDateTime, categoryId, instId)
    }

    protected getNewlyAdded(now, categoryId, instId) {
        sort(pdtpQueryService.findAllNewestFrom(dateOf(now), maxNewestToRecommend,
            validCategoryId(categoryId), validInstId(instId)))
        .collect { makeRecommendationEntry(it) }
    }

    protected getComingSoon(now, categoryId, instId) {
        sort(getComingSoonEvents(dateOf(now.plusDays(dayOffsetForCommingSoon)), maxComingSoonToRecommend,
            categoryId, instId))
        .collect { makeRecommendationEntry(it) }
    }

    protected getComingSoonEvents(now, limitTo, categoryId, instId, notIn=[]) {
        def validCategoryId = validCategoryId(categoryId)
        def validInstId = validInstId(instId)
        def pdtps = pdtpQueryService.findAllNotFinishedRandomly(now, limitTo, validCategoryId, validInstId, notIn)
        if ((pdtps.size() < limitTo) && (validCategoryId || validInstId)) {
            if (validCategoryId) {
                validCategoryId = null
            } else if (validInstId) {
                validInstId = null
            }
            pdtps + getComingSoonEvents(now, limitTo - pdtps.size(), validCategoryId, validInstId, notIn + pdtps*.id)
        } else {
            pdtps
        }
    }

    private validCategoryId(categoryId) {
        (categoryId && categoryId != categoryMenuService.allEntryId) ? categoryId as long : null
    }

    private validInstId(instId) {
        (instId && instId != institutionMenuService.allEntryId) ? instId as long : null
    }

    private makeRecommendationEntry(pdtp) {
        def event = pdtp.event
        [id: pdtp.id, label: printLabel(pdtp), title: event.title] + makeDescription(event)
    }

    private printLabel(pdtp) {
        "[${printDate(pdtp)}, ${pdtp.event.category.name}]"
    }

    private printDate(pdtp) {
        def fromDateAsString = printShortJdkDate(pdtp.fromDate)
        pdtp.fromDate == pdtp.toDate ? fromDateAsString : "$fromDateAsString-${printShortJdkDate(pdtp.toDate)}"
    }

    private makeDescription(event) {
        def description
        if (event.description) {
            def endOfSentenceIndex = event.description.indexOf('.')
            if (endOfSentenceIndex > 0 && endOfSentenceIndex <= maxSizeOfDescToShow) {
                description = event.description[0..endOfSentenceIndex - 1]
            } else {
                def min = Math.min(event.description.size(), maxSizeOfDescToShow)
                def endOfDesc = min == event.description.size() ? '' : '...'
                description = "${event.description[0..min - 1]}$endOfDesc"
            }
            [desc: description]
        } else {
            [more: event.more]
        }
    }

    private sort(recommendations) {
        Collections.sort(recommendations, entryComparator)
        recommendations
    }

    private dateOf(date) {
        dateTimeToDateOnly(date)
    }
}
