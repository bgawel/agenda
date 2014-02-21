package agenda

import static agenda.LocalContext.areDatesTheSame
import static agenda.LocalContext.dateTime
import static agenda.LocalContext.dateTimeToDateOnly
import static agenda.LocalContext.dateTimeToTimeOnly
import static agenda.LocalContext.getCurrentDateTime
import static agenda.LocalContext.jdkDateAndTimeToString
import static agenda.LocalContext.stringToDate
import static agenda.PresentationContext.printFullJdkDate
import static agenda.PresentationContext.printJdkTime
import static agenda.PresentationContext.printMiddleJdkDate
import static agenda.PresentationContext.printShortDayOfWeekForJdkDate
import static agenda.PresentationContext.printShortJdkDate
import static org.joda.time.DateTimeConstants.DAYS_PER_WEEK

class EventPresentationService {

    private static final MINUS_HOURS_TO_SHOW_TODAYS_EVENTS = 1
    private static final MAX_DISPLAYED_PDTPS_PER_EVENT_OF_ONE_TIME_TYPE = 3
    private static final MAX_DISPLAYED_PDTPS_PER_EVENT_OF_TMP_TYPE = 1

    def weekMenuService
    def pdtpQueryService
    def eventQueryService

    def showByDate(requestedDate) {
        showByDate(currentDateTime, requestedDate)
    }

    def showByPdtp(pdtpId) {
        showByPdtp(currentDateTime, pdtpId)
    }

    def submittedEvents(instId) {
        eventQueryService.findAllByInstitution(instId).collect { makeEntryForSubmittedEvent(it) }
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
        events
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
        def isDateOfEventsToday = areDatesTheSame(now, eventsDate)
        if (isDateOfEventsToday) {
            events = pdtpQueryService.findAllNotFinishedFor(eventsDate, timeOf(now))
                .collect { makeEntryForShowByCalendar(it) }
        } else if (now.isBefore(eventsDate)) {
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
            makeEntryForShowByCalendar(firstPdtpToDisplay) + [pdtps: otherPdtpsToDisplay]
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
                maxPdtpsPerEvent = firstPdtpOfEvent.event.isOneTimeType ?
                    MAX_DISPLAYED_PDTPS_PER_EVENT_OF_ONE_TIME_TYPE : MAX_DISPLAYED_PDTPS_PER_EVENT_OF_TMP_TYPE
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
                        if (!differentTime && (firstPdtpOfEvent.time != pdtp.time)) {
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
             displayDt: printDateTimeOfEventForShowByFutureOrAll(pdtpsOfEventToShow, differentTime)]
    }

    private makeEntryForShowByCalendar(pdtp) {
        makeCommonPartOfEntryForShow(pdtp, false) +
            [place: pdtp.place, price: pdtp.price, displayTime: pdtp.timeDescription ?: printJdkTime(pdtp.time)]
    }

    private makeCommonPartOfEntryForShow(pdtp, moreToShow) {
        def event = pdtp.event
        [id: pdtp.id, title: event.title, pic: event.pic, more: event.more, desc: event.description,
            whoName: event.institution.name, whoAbout: makeDescOfInstitution(event.institution),
            catName: event.category.name, dateTime: printDateTimeOfEventToSort(pdtp), moreToShow: moreToShow]
    }

    private makeDescOfInstitution(institution) {
        def description = new StringBuilder()
        appendValue(description, institution.address)
        appendValue(description, institution.web)
        appendValue(description, institution.telephone)
        description.toString()
    }

    private appendValue(description, value) {
        if (value) {
            description <<= description.size() > 0 ? ", $value" : value
        }
    }

    private printDateTimeOfEventToSort(pdtp) {
        jdkDateAndTimeToString(pdtp.fromDate, pdtp.time)
    }

    private printDateTimeOfEventForShowByFutureOrAll(pdtps, differentTime) {
        def dateTime = new StringBuilder()
        def firstPdtp = pdtps[0]
        if (firstPdtp.event.isOneTimeType && !differentTime) {
            dateTime <<= printDateOfEventForShowByFutureOrAll(firstPdtp)
            (1..<pdtps.size()).each { i ->
                dateTime <<= ", ${printDateOfEventForShowByFutureOrAll(pdtps[i])}"
            }
            dateTime <<= " o ${printJdkTime(firstPdtp.time)}"
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
        makeDateTimeToDisplay(pdtp) { "${dateMaker(it)} (${printShortDayOfWeekForJdkDate(it)})" }
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
        if (pdtp.event.isOneTimeType) {
            date = fromDateAsString
            time = printJdkTime(pdtp.time)
        } else {
            date = pdtp.fromDate == pdtp.toDate ? fromDateAsString : "$fromDateAsString - ${dateMaker(pdtp.toDate)}"
            time = pdtp.timeDescription
        }
        [displayDate: date, displayTime: time]
    }

    private timeOf(date) {
        dateTimeToTimeOnly(date.minusHours(MINUS_HOURS_TO_SHOW_TODAYS_EVENTS))
    }

    private dateOf(date) {
        dateTimeToDateOnly(date)
    }
}
