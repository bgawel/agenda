package agenda


import grails.test.mixin.TestFor

import org.joda.time.DateTime

import spock.lang.Specification

@TestFor(EventRecommendationService)
class EventRecommendationServiceSpec extends Specification {

    def date = new DateTime(2014, 1, 2, 19, 0)
    def pdtps

    def "get newly added events"() {
        setup:
        def categoryId = 'all'
        def instId = 1
        1 * service.pdtpQueryService.findAllNewestFrom(_, 3, null, instId) >> {
            assert it[0].hourOfDay == 15
            pdtps
        }

        when:
        def events = service.getNewlyAdded(categoryId, instId)

        then:
        events.size() == 4
        events[0].label == '[01.01-03.01, cat1]'
        events[0].title == 'title2'
        !events[0].desc
        events[0].more == 'more'
        events[1].label == '[02.01, cat1]'
        events[1].title == 'title1'
        events[1].desc == 'description'
        events[2].desc == 'part1'
        events[3].desc == 'descriptiondescriptiondescriptiodescriptiondescriptiondescriptio...'
    }

    def "get comming soon events"() {
        setup:
        def categoryId = 2
        def instId = 3
        1 * service.pdtpQueryService.findAllNotFinishedRandomly(_, 5, categoryId, instId, []) >> {
            assert it[0].hourOfDay == 15
            assert it[0].dayOfMonth == 3
            pdtps[0..0]
        }
        1 * service.pdtpQueryService.findAllNotFinishedRandomly(_, 4, null, instId, [pdtps[0].id]) >> pdtps[1..1]
        1 * service.pdtpQueryService.findAllNotFinishedRandomly(_, 3, null, null, [pdtps[0].id, pdtps[1].id]) >> pdtps[2..2]

        when:
        def events = service.getComingSoon(date, categoryId, instId)

        then:
        events.size() == 3
        events[0].title == 'title2'
        events[1].title == 'title1'
        events[2].title == 'title3'
    }

    def setup() {
        service.pdtpQueryService = Mock(PdtpQueryService)
        service.categoryMenuService = new CategoryMenuService()
        service.institutionMenuService = new InstitutionMenuService()
        def cat = new Category(name: 'cat1')
        pdtps = [
            new Pdtp(fromDate: date.toDate(), toDate: date.toDate(), event: new Event(title: 'title1',
                description: 'description', category: cat)),
            new Pdtp(fromDate: date.minusDays(1).toDate(), toDate: date.plusDays(1).toDate(), event: new Event(
                title: 'title2', more: 'more', category: cat)),
            new Pdtp(fromDate: date.toDate(), toDate: date.toDate(), event: new Event(title: 'title3',
                description: 'part1. Part2', category: cat)),
            new Pdtp(fromDate: date.toDate(), toDate: date.toDate(), event: new Event(title: 'title3',
                description: 'descriptiondescriptiondescriptiodescriptiondescriptiondescription', category: cat))
        ]
        pdtps.eachWithIndex { pdtp, index ->
            pdtp.id = index + 1
        }
    }
}
