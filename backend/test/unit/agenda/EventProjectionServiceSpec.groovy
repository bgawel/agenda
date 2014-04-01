package agenda

import static agenda.LocalContext.dateTimeToDateOnly
import static agenda.LocalContext.dateTimeToTimeOnly
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

import org.joda.time.DateTime

import spock.lang.Specification

@TestFor(EventProjectionService)
@Mock([Category, Event, Institution, Pdtp])
class EventProjectionServiceSpec extends Specification {

    def now = new DateTime(2014, 1, 2, 19, 0)
    def events = []

    def "should find events by 'future'"() {
        setup:
        1 * service.pdtpQueryService.findAllNotFinishedFrom(_) >> {
            assert it[0].dayOfMonth == 9
            assert it[0].hourOfDay == 15
            def pdtps = []
            events.each {
                pdtps.addAll(it.pdtps)
            }
            pdtps
        }
        service.staticResourceService.makeImageSrc(_) >> 'pic1'

        when:
        def result = service.showByDate(now, 'future')

        then:
        result.categories == ['badgesPerCategory']
        def events = result.events
        events.size() == 4
        events[0].title == 'title1'
        events[0].pic == 'pic1'
        events[0].more == 'more1'
        events[0].desc == 'desc1'
        events[0].whoName == 'name-inst1'
        events[0].whoAbout == 'address1, <a href="http://www.example1.com">www.example1.com</a>, tel1'
        events[0].catName == 'cat1'
        events[0].place == '1-place1'
        events[0].price == '1-price1'
        events[0].moreToShow
        events[0].dateTime == '2014-01-12T19:00'
        events[0].displayDt == '12.01(n), 13.01(pn), 14.01(wt) o 19:00'
        events[1].title == 'title2'
        events[1].place == ''
        events[1].price == ''
        events[1].moreToShow
        events[1].dateTime == '2013-12-28T20:00'
        events[1].displayDt == '28.12(so) - 12.01(n) o timeDescription1'
        events[2].title == 'title3'
        events[2].place == '3-place1'
        events[2].price == '3-price1'
        !events[2].moreToShow
        events[2].dateTime == '2014-01-12T19:00'
        events[2].displayDt == '12.01(n) o 19:00, 13.01(pn) o 19:00, 14.01(wt) o 18:00'
        events[3].title == 'title4'
        events[3].place == '4-place1'
        events[3].price == '4-price1'
        !events[3].moreToShow
        events[3].dateTime == '2014-01-12T19:00'
        events[3].displayDt == '12.01(n) o timeDescription'
    }

    def "should find events by 'all'"() {
        setup:
        1 * service.pdtpQueryService.findAllNotFinishedFrom(_, _) >> {
            assert it[0].dayOfMonth == 2
            assert it[0].hourOfDay == 15
            assert it[1].dayOfMonth == 1
            assert it[1].hourOfDay == 18
            def pdtps = []
            events.each {
                pdtps.addAll(it.pdtps)
            }
            pdtps
        }
        service.staticResourceService.makeImageSrc(_) >> 'pic4'

        when:
        def events = service.showByDate(now, 'all').events

        then:
        events.size() == 4
    }

    def "should find events by today"() {
        setup:
        1 * service.pdtpQueryService.findAllNotFinishedFor(_, _) >> {
            assert it[0].dayOfMonth == 2
            assert it[0].hourOfDay == 15
            assert it[1].dayOfMonth == 1
            assert it[1].hourOfDay == 18
            events[3].pdtps
        }
        service.staticResourceService.makeImageSrc(_) >> 'pic4'

        when:
        def events = service.showByDate(now, '2014-01-02').events

        then:
        events.size() == 1
        events[0].title == 'title4'
        events[0].pic == 'pic4'
        events[0].more == 'more4'
        events[0].desc == 'desc4'
        events[0].whoName == 'name-inst1'
        events[0].whoAbout == 'address1, <a href="http://www.example1.com">www.example1.com</a>, tel1'
        events[0].catName == 'cat1'
        events[0].place == '4-place1'
        events[0].price == '4-price1'
        !events[0].moreToShow
        events[0].dateTime == '19:00'    // because faked entry was returned
        events[0].displayDt== 'timeDescription'
    }

    def "should find events by calendar date"() {
        setup:
        1 * service.pdtpQueryService.findAllFor(_) >> {
            assert it[0].dayOfMonth == 3
            assert it[0].hourOfDay == 15
            [events[0].pdtps[0]]
        }

        when:
        def events = service.showByDate(now, '2014-01-03').events

        then:
        events.size() == 1
        events[0].dateTime == '19:00'    // because faked entry was returned
        events[0].displayDt == '19:00'
    }

    def "should not find events if calendar date < now"() {
        setup:
        0 * service.pdtpQueryService.findAllNotFinishedFor(_)
        0 * service.pdtpQueryService.findAllFor(_)

        when:
        def events = service.showByDate(now, '2014-01-01').events

        then:
        !events
    }

    def "should find event by id, date and time"() {
        setup:
        def pdtpId = events[0].pdtps[0].id
        Pdtp.getAll() // to preload data, otherwise Pdtp.get(pdtpId) returns null (sic!)
        1 * service.pdtpQueryService.findAllNotFinishedForEventFrom(events[0].id, _, _, pdtpId) >> {
            assert it[1].dayOfMonth == 2
            assert it[1].hourOfDay == 15
            assert it[2].dayOfMonth == 1
            assert it[2].hourOfDay == 18
            events[0].pdtps[1..-1]
        }
        service.staticResourceService.makeImageSrc(_) >> 'pic1'

        when:
        def event = service.showByPdtp(now, pdtpId)

        then:
        event.title == 'title1'
        event.pic == 'pic1'
        event.more == 'more1'
        event.desc == 'desc1'
        event.whoName == 'name-inst1'
        event.whoAbout == 'address1, <a href="http://www.example1.com">www.example1.com</a>, tel1'
        event.catName == 'cat1'
        event.place == '1-place1'
        event.price == '1-price1'
        event.pdtps.size() == 3
        event.pdtps[0].displayDate == '13 styczeń 2014(pn)'
        event.pdtps[1].displayDate == '14 styczeń 2014(wt)'
        event.pdtps[2].displayDate == '15 styczeń 2014(śr)'
        event.pdtps.each {
            assert it.displayTime == '19:00'
            assert it.place == event.place
            assert it.price == event.price
        }
    }

    def "should return null if cannot find pdtp"() {
        when:
        def event = service.showByPdtp(666L)

        then:
        !event
    }

    def "should return submitted events"() {
        setup:
        1 * service.eventQueryService.findAllByInstitution(1) >> { [events[0], events[3]] }

        when:
        def submitted = service.submittedEvents(1)

        then:
        def events = submitted.events
        events.size() == 2
        events[0].title == 'title1'
        events[0].locdate == '1-place1, 15 sty 2014, 19:00 (4)'
        events[1].title == 'title4'
        events[1].locdate == '4-place1, 12 sty 2014, timeDescription (1)'
        submitted.id == 1
        submitted.lastModified == events[0].lastUpdated
    }

    def setup() {
        Institution.metaClass.encodePassword = { null }
        def cat = new Category(name: 'cat1').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1',
            address: 'address1', web: 'www.example1.com', telephone: 'tel1', fax: 'fax1').save()
        def futureDate = dateTimeToDateOnly(now.plusDays(10))
        def time = dateTimeToTimeOnly(now)
        def event1 = new Event(title: 'title1', pic: 'pic1', more: 'more1', description: 'desc1', category: cat,
            institution: inst1, oneTimeType: true)
        def fd = futureDate
        (1..4).each { index -> // place, time, price the same, only first 3 will be displayed
            event1.addToPdtps(new Pdtp(place: '1-place1', fromDate: fd.toDate(), toDate: fd.toDate(),
                startTime: time.toDate(), price: '1-price1'))
            fd = futureDate.plusDays(index)
        }
        event1.save(validate: false)
        def event2 = new Event(title: 'title2', pic: 'pic2', more: 'more2', description: 'desc2', category: cat,
            institution: inst1, oneTimeType: false)
        fd = futureDate
        (1..2).each { index ->  // place, time, price different, only first one will be displayed
            event2.addToPdtps(new Pdtp(place: "2-place$index", fromDate: now.minusDays(5).toDate(), toDate: fd.toDate(),
                startTime: time.plusHours(index).toDate(), price: "2-price$index", timeDescription: "timeDescription$index"))
            fd = futureDate.plusDays(index)
        }
        event2.save(validate: false)
        def event3 = new Event(title: 'title3', pic: 'pic3', more: 'more3', description: 'desc3', category: cat,
            institution: inst1, oneTimeType: true)
        fd = futureDate
        (1..3).each { index -> // different time, all will be displayed
            event3.addToPdtps(new Pdtp(place: '3-place1', fromDate: fd.toDate(), toDate: fd.toDate(),
                startTime: index == 3 ? time.minusHours(1).toDate() : time.toDate(), price: '3-price1'))
            fd = futureDate.plusDays(index)
        }
        event3.save(validate: false)
        def event4 = new Event(id:4, title: 'title4', pic: 'pic4', more: 'more4', description: 'desc4', category: cat,
            institution: inst1, oneTimeType: false)
        // fromDate = toDate for tmp event
        event4.addToPdtps(new Pdtp(place: '4-place1', fromDate: futureDate.toDate(), toDate: futureDate.toDate(),
            startTime: time.toDate(), price: '4-price1', timeDescription: 'timeDescription'))
        event4.save(validate: false)
        events = [event1, event2, event3, event4]
        service.pdtpQueryService = Mock(PdtpQueryService)
        service.eventQueryService = Mock(EventQueryService)
        service.weekMenuService = new WeekMenuService()
        service.institutionMenuService = Mock(InstitutionMenuService)
        service.institutionMenuService.calculateBadgesPerCategory(_) >> ['badgesPerCategory']
        service.staticResourceService = Mock(StaticResourceService)
    }
}
