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
        week.entries[0].id == 'date'
        week.entries[1].id == 'all'
        week.entries[1].name == 'wszystkie'
        week.entries[2].id == '2014-02-06'
        week.entries[2].name == 'dziś'
        week.entries[2].abbr == 'CZ'
        week.entries[3].id == '2014-02-07'
        week.entries[3].name == 7
        week.entries[3].abbr == 'PT'
        week.entries[8].id == '2014-02-12'
        week.entries[8].name == 12
        week.entries[8].abbr == 'ŚR'
        week.entries[9].id == 'future'
    }
}
