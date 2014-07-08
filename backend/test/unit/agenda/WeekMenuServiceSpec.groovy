package agenda

import grails.test.mixin.TestFor

import org.joda.time.DateTime

import spock.lang.Specification

@TestFor(WeekMenuService)
class WeekMenuServiceSpec extends Specification {

    def "should get week menu"() {
        when:
        def week = service.getWeek(new DateTime(2014, 2, 6, 0, 0))

        then:
        week.entries.size() == 10
        week.activeIndex == 2
        week.entries[0].id == 'cal'
        week.entries[0].name == 'Calendar'
        week.entries[0].type == 'cal'
        !week.entries[0].abbr
        week.entries[1].id == 'all'
        week.entries[1].name == 'All'
        week.entries[1].type == 'all'
        !week.entries[1].abbr
        week.entries[2].id == '2014-02-06'
        week.entries[2].name == 'Today'
        week.entries[2].abbr == 'CZ'
        week.entries[3].id == '2014-02-07'
        week.entries[3].name == 7
        week.entries[3].abbr == 'PT'
        week.entries[8].id == '2014-02-12'
        week.entries[8].name == 12
        week.entries[8].abbr == 'ŚR'
        week.entries[9].id == 'future'
        week.entries[9].name == 'Future'
        week.entries[9].type == 'future'
        !week.entries[9].abbr
    }

    def setup() {
        service.messageSource = messageSource
        service.init()
    }

    def setupSpec() {
        PresentationContext.locale = new Locale('pl')
        messageSource.addMessage('weekMenu.entry.today', PresentationContext.locale, 'Today')
        messageSource.addMessage('weekMenu.entry.calendar', PresentationContext.locale, 'Calendar')
        messageSource.addMessage('weekMenu.entry.all', PresentationContext.locale,  'All')
        messageSource.addMessage('weekMenu.entry.future', PresentationContext.locale, 'Future')
    }
}
