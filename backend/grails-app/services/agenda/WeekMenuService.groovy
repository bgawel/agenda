package agenda

import static agenda.LocalContext.dateToString
import static agenda.LocalContext.getCurrentDateTime
import static agenda.LocalContext.locale
import static agenda.PresentationContext.printShortDayOfWeek
import static org.joda.time.DateTimeConstants.DAYS_PER_WEEK

class WeekMenuService {

    static transactional = false

    def allEntryId = 'all'
    def calendarEntryId = 'date'
    def futureEntryId = 'future'
    def todayEntryName = 'dziś'
    def calendarEntryName = 'kalendarz'
    def allEntryName = 'wszystkie'
    def futureEntryName = 'przyszłe'
    def activeIndex = 2

    def getWeek() {
        getWeek(currentDateTime)
    }

    protected getWeek(forDate) {
        def entries = []
        entries << calendarEntry
        entries << allEntry
        entries += getDayEntries(forDate)
        entries << futureEntry
        [entries: entries, activeIndex: activeIndex]
    }

    private getDayEntries(startingDate) {
        def days = []
        def date = startingDate

        days << makeEntry(dateToString(date), todayEntryName, getDayAbbr(date))
        (1..<DAYS_PER_WEEK).each {
            date = date.plusDays(1)
            days << makeEntry(dateToString(date), date.dayOfMonth, getDayAbbr(date))
        }
        days
    }

    private getDayAbbr(date) {
        printShortDayOfWeek(date).toUpperCase(locale)
    }

    private getCalendarEntry() {
        makeEntry(calendarEntryId, calendarEntryName, '')
    }

    private getAllEntry() {
        makeEntry(allEntryId, allEntryName, '')
    }

    private getFutureEntry() {
        makeEntry(futureEntryId, futureEntryName, '')
    }

    private makeEntry(id, name, abbr) {
        [id: id, name:name, abbr:abbr]
    }
}
