package agenda

import static agenda.LocalContext.dateTime
import static agenda.LocalContext.dateTimeToDateOnly
import static agenda.LocalContext.dateTimeToTimeOnly
import static agenda.LocalContext.getCurrentDateTime
import static agenda.LocalContext.jdkDateAndTimeToString
import static agenda.LocalContext.jdkTimeToString
import static agenda.LocalContext.stringToDate
import static agenda.PresentationContext.printFullJdkDate
import static agenda.PresentationContext.printJdkTime
import static agenda.PresentationContext.printMiddleJdkDate
import static agenda.PresentationContext.printShortDayOfWeekForJdkDate
import static agenda.PresentationContext.printShortJdkDate
import static org.joda.time.DateTimeConstants.DAYS_PER_WEEK

class EventProjectionService {

    def minusHoursToShowTodaysEvents = 1
    def maxDisplayedPdtpsForOneTimeType = 3
    def maxDisplayedPdtpsForTmpType = 1

    def weekMenuService
    def institutionMenuService
    def pdtpQueryService
    def eventQueryService
    def staticResourceService

    def showByDate(requestedDate) {
        showByDate(currentDateTime, requestedDate)
    }

    def showByPdtp(pdtpId) {
        showByPdtp(currentDateTime, pdtpId)
    }

    def submittedEvents(instId) {
        // 'events' instead of array
        // see http://haacked.com/archive/2008/11/20/anatomy-of-a-subtle-json-vulnerability.aspx/
        [events: eventQueryService.findAllByInstitution(instId).collect { makeEntryForSubmittedEvent(it) }]
    }

    protected showByDate(now, requestedDate) {
        def events = []
        if (requestedDate == weekMenuService.futureEntryId) {
            events = showByFuture(now)
        } else if (requestedDate == weekMenuService.allEntryId) {
            events = showByAll(now)
        } else {
            events = showByCalendar(now, requestedDate)
        }
        def badgesPerCategory = institutionMenuService.calculateBadgesPerCategory(events)
        [categories: badgesPerCategory, events: events]
    }

    private showByFuture(now) {
        def firstDateOfFuture = dateOf(now.plusDays(DAYS_PER_WEEK))
        makeListOfEntriesForShowByFutureOrAll(pdtpQueryService.findAllNotFinishedFrom(firstDateOfFuture))
    }

    private showByAll(now) {
        makeListOfEntriesForShowByFutureOrAll(pdtpQueryService.findAllNotFinishedFrom(dateOf(now), timeOf(now)))
    }

    private showByCalendar(now, requestedDate) {
        def events = []
        def eventsDate = dateOf(stringToDate(requestedDate))
        def today = dateOf(now)
        if (eventsDate == today) {
            events = pdtpQueryService.findAllNotFinishedFor(eventsDate, timeOf(now))
                .collect { makeEntryForShowByCalendar(it) }
        } else if (today.isBefore(eventsDate)) {
            events = pdtpQueryService.findAllFor(eventsDate).collect { makeEntryForShowByCalendar(it) }
        }
        events
    }

    protected showByPdtp(now, pdtpId) {
        def firstPdtpToDisplay = Pdtp.get(pdtpId)
        if (firstPdtpToDisplay) {
            def otherPdtpsToDisplay = []
            pdtpQueryService.findAllNotFinishedForEventFrom(firstPdtpToDisplay.event.id, dateOf(now), timeOf(now),
                firstPdtpToDisplay.id)
            .each {
                otherPdtpsToDisplay << (makeDateTimeForShowByPdtp(it) + [place: it.place, price: it.price])
            }
            makeCommonPartOfEntryForShow(firstPdtpToDisplay, false) + makeDateTimeForShowByPdtp(firstPdtpToDisplay) +
                [place: firstPdtpToDisplay.place, price: firstPdtpToDisplay.price, pdtps: otherPdtpsToDisplay]
        }
    }

    private makeListOfEntriesForShowByFutureOrAll(pdtps) {
        def entries = []
        if (pdtps) {
            def firstPdtpOfEvent, currentEventId, moreToShow, differentPlaces, differentPrices, differentTime,
            indexOfPdtp, pdtpsOfEventToShow, maxPdtpsPerEvent, pdtp = pdtps[0]
            def initNewEvent = {
                firstPdtpOfEvent = pdtp
                pdtpsOfEventToShow = [firstPdtpOfEvent]
                currentEventId = firstPdtpOfEvent.event.id
                maxPdtpsPerEvent = firstPdtpOfEvent.event.oneTimeType ?
                    maxDisplayedPdtpsForOneTimeType : maxDisplayedPdtpsForTmpType
                moreToShow = false
                differentPlaces = false
                differentPrices = false
                differentTime = false
                indexOfPdtp = 0
            }
            initNewEvent()
            (1..<pdtps.size()).each { i ->
                pdtp = pdtps[i]
                if (pdtp.event.id != currentEventId) {
                    entries << makeEntryForShowByFutureOrAll(firstPdtpOfEvent, pdtpsOfEventToShow, differentPlaces,
                        differentPrices, differentTime, moreToShow)
                    initNewEvent()
                } else {
                    ++indexOfPdtp
                    if (indexOfPdtp < maxPdtpsPerEvent) {
                        pdtpsOfEventToShow << pdtp
                        if (!differentTime && (firstPdtpOfEvent.startTime != pdtp.startTime)) {
                            differentTime = true
                        }
                    } else {
                        moreToShow = true
                    }
                    if (!differentPlaces && firstPdtpOfEvent.place != pdtp.place) {
                        differentPlaces = true
                    }
                    if (!differentPrices && firstPdtpOfEvent.price != pdtp.price) {
                        differentPrices = true
                    }
                }
            }
            entries << makeEntryForShowByFutureOrAll(firstPdtpOfEvent, pdtpsOfEventToShow, differentPlaces,
                differentPrices, differentTime, moreToShow)
        }
        entries
    }

    private makeEntryForShowByFutureOrAll(pdtp, pdtpsOfEventToShow, differentPlaces, differentPrices,
        differentTime, moreToShow) {
        makeCommonPartOfEntryForShow(pdtp, moreToShow) +
            [place: differentPlaces ? '' : pdtp.place, price: differentPrices ? '' : pdtp.price,
             dateTime: printDateTimeOfEventToSort(pdtp),
             displayDt: printDateTimeOfEventForShowByFutureOrAll(pdtpsOfEventToShow, differentTime)]
    }

    private makeEntryForShowByCalendar(pdtp) {
        makeCommonPartOfEntryForShow(pdtp, false) +
            [place: pdtp.place, price: pdtp.price, displayDt: pdtp.timeDescription ?: printJdkTime(pdtp.startTime),
             dateTime: printTimeOfEventToSort(pdtp)]
    }

    private makeCommonPartOfEntryForShow(pdtp, moreToShow) {
        def event = pdtp.event
        [id: pdtp.id, title: event.title, pic: staticResourceService.makeImageSrc(event.pic), more: event.more,
           desc: event.description, whoId:event.institution.id, whoName: event.institution.name, moreToShow: moreToShow,
           whoAbout: makeDescOfInstitution(event.institution), catId:event.category.id, catName: event.category.name]
    }

    private makeDescOfInstitution(institution) {
        def description = new StringBuilder()
        appendValue(description, institution.address)
        appendWeb(description, institution.web)
        appendValue(description, institution.telephone)
        description.toString()
    }

    private appendWeb(description, originalWeb) {
        if (originalWeb) {
            def web = WwwValidator.startWithScheme(originalWeb) ? originalWeb : ('http://' + originalWeb)
            appendValue(description, "<a href=\"$web\">$originalWeb</a>")
        }
    }

    private appendValue(description, value) {
        if (value) {
            description <<= description.size() > 0 ? ", $value" : value
        }
    }

    private printDateTimeOfEventToSort(pdtp) {
        jdkDateAndTimeToString(pdtp.fromDate, pdtp.startTime)
    }

    private printTimeOfEventToSort(pdtp) {
        jdkTimeToString(pdtp.startTime)
    }

    private printDateTimeOfEventForShowByFutureOrAll(pdtps, differentTime) {
        def dateTime = new StringBuilder()
        def firstPdtp = pdtps[0]
        if (firstPdtp.event.oneTimeType && !differentTime) {
            dateTime <<= printDateOfEventForShowByFutureOrAll(firstPdtp)
            (1..<pdtps.size()).each { i ->
                dateTime <<= ", ${printDateOfEventForShowByFutureOrAll(pdtps[i])}"
            }
            dateTime <<= " o ${printJdkTime(firstPdtp.startTime)}"
        } else {
            dateTime <<= printDateTimeOfEventForShowByFutureOrAll(firstPdtp)
            (1..<pdtps.size()).each { i ->
                dateTime <<= ", ${printDateTimeOfEventForShowByFutureOrAll(pdtps[i])}"
            }
        }
        dateTime.toString()
    }

    private printDateTimeOfEventForShowByFutureOrAll(pdtp) {
        def dateTime = makeDateTimeToDisplayWithDayOfWeek(pdtp) { printShortJdkDate(it) }
        "${dateTime.displayDate} o ${dateTime.displayTime}"
    }

    private printDateOfEventForShowByFutureOrAll(pdtp) {
        makeDateTimeToDisplayWithDayOfWeek(pdtp) { printShortJdkDate(it) }.displayDate
    }

    private makeDateTimeForShowByPdtp(pdtp) {
        makeDateTimeToDisplayWithDayOfWeek(pdtp) { printFullJdkDate(it) }
    }

    private makeDateTimeToDisplayWithDayOfWeek(pdtp, dateMaker) {
        makeDateTimeToDisplay(pdtp) { "${dateMaker(it)}(${printShortDayOfWeekForJdkDate(it).toLowerCase()})" }
    }

    private makeEntryForSubmittedEvent(event) {
        [id: event.id, title: event.title, mod: event.lastUpdated, locdate: printLocdateForSubmittedEvent(event)]
    }

    private printLocdateForSubmittedEvent(event) {
        def lastPdtp = event.pdtps[-1]
        def dateTime = makeDateTimeToDisplay(lastPdtp) { printMiddleJdkDate(it) }
        "${lastPdtp.place}, ${dateTime.displayDate}, ${dateTime.displayTime} (${event.pdtps.size()})"
    }

    private makeDateTimeToDisplay(pdtp, dateMaker) {
        def date, time
        def fromDateAsString = dateMaker(pdtp.fromDate)
        if (pdtp.event.oneTimeType) {
            date = fromDateAsString
            time = printJdkTime(pdtp.startTime)
        } else {
            date = pdtp.fromDate == pdtp.toDate ? fromDateAsString : "$fromDateAsString - ${dateMaker(pdtp.toDate)}"
            time = pdtp.timeDescription
        }
        [displayDate: date, displayTime: time]
    }

    private timeOf(date) {
        dateTimeToTimeOnly(date.minusHours(minusHoursToShowTodaysEvents))
    }

    private dateOf(date) {
        dateTimeToDateOnly(date)
    }
}
