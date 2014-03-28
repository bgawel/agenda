package agenda

import static agenda.LocalContext.dateToString
import static agenda.LocalContext.getCurrentDateTime
import static agenda.PresentationContext.locale
import static agenda.PresentationContext.printShortDayOfWeek
import static org.joda.time.DateTimeConstants.DAYS_PER_WEEK
import grails.plugin.cache.Cacheable

import javax.annotation.PostConstruct

class WeekMenuService {

    static transactional = false

    def allEntryId = 'all'
    def calendarEntryId = 'date'
    def futureEntryId = 'future'
    def activeIndex = 2

    def messageSource
    private todayEntryName
    private calendarEntry
    private allEntry
    private futureEntry

    @Cacheable(value='weekMenu', key='#root.methodName')
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

    private getFutureEntry() {
        makeEntry(futureEntryId, futureEntryName, futureEntryId)
    }

    private makeEntry(id, name, abbr) {
        [id: id, name:name, abbr:abbr]
    }

    @PostConstruct
    void init() {
        todayEntryName = messageSource.getMessage('weekMenu.entry.today', null, locale)
        calendarEntry = makeEntry(calendarEntryId, messageSource.getMessage('weekMenu.entry.calendar', null, locale),
            calendarEntryId)
        allEntry = makeEntry(allEntryId, messageSource.getMessage('weekMenu.entry.all', null, locale), allEntryId)
        futureEntry = makeEntry(futureEntryId, messageSource.getMessage('weekMenu.entry.future', null, locale),
            futureEntryId)
    }
}
