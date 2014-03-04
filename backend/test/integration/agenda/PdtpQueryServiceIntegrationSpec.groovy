package agenda

import static agenda.LocalContext.dateTimeToDateOnly
import static agenda.LocalContext.dateTimeToTimeOnly

import org.joda.time.DateTime

import spock.lang.Ignore
import spock.lang.Specification

class PdtpQueryServiceIntegrationSpec extends Specification {

    def now = new DateTime(2014, 1, 1, 19, 0)
    def pdtpQueryService
    def sessionFactory

    void "should find all not finished starting from given date"() {
        when:
        def pdtps = pdtpQueryService.findAllNotFinishedFrom(dateTimeToDateOnly(now))

        then:
        pdtps.size() == 6
        def event1Id = Event.findByTitle('title1').id
        pdtps[0].event.id == event1Id
        pdtps[1].event.id == event1Id
        pdtps[0].fromDate < pdtps[1].fromDate
        pdtps[0].startTime == pdtps[1].startTime
        def event2Id = Event.findByTitle('title2').id
        pdtps[2].event.id == event2Id
        pdtps[3].event.id == event2Id
        pdtps[2].fromDate == pdtps[3].fromDate
        pdtps[2].startTime < pdtps[3].startTime
        def event3Id = Event.findByTitle('title3').id
        pdtps[4].event.id == event3Id
        pdtps[5].event.id == event3Id
    }

    @Ignore('TODO bgawel: time ignored for now')
    void "should find all not finished starting from given date and time"() {
        when:
        def pdtps = pdtpQueryService.findAllNotFinishedFrom(dateTimeToDateOnly(now), dateTimeToTimeOnly(now))

        then:
        pdtps.size() == 5
        def event1Id = Event.findByTitle('title1').id
        pdtps[0].event.id == event1Id
        pdtps[1].event.id == event1Id
        pdtps[0].fromDate < pdtps[1].fromDate
        pdtps[0].startTime == pdtps[1].startTime
        def event2Id = Event.findByTitle('title2').id
        pdtps[2].event.id == event2Id
        pdtps[3].event.id == event2Id
        pdtps[2].fromDate == pdtps[3].fromDate
        pdtps[2].startTime < pdtps[3].startTime
        pdtps[4].event.id == Event.findByTitle('title3').id
        pdtps[4].place == '3-place1'
    }

    void "should find all for given date"() {
        when:
        def pdtps = pdtpQueryService.findAllFor(dateTimeToDateOnly(now))

        then:
        pdtps.size() == 2
        pdtps[0].startTime < pdtps[1].startTime
    }

    @Ignore('TODO bgawel: time ignored for now')
    void "should find all not finished for given date and time"() {
        when:
        def pdtps = pdtpQueryService.findAllNotFinishedFor(dateTimeToDateOnly(now), dateTimeToTimeOnly(now))

        then:
        pdtps.size() == 1
        pdtps[0].place == '3-place1'
    }

    void "should find all not finished for event for given date and time (no escape)"() {
        when:
        def pdtps = pdtpQueryService.findAllNotFinishedForEventFrom(Event.findByTitle('title2').id,
            dateTimeToDateOnly(now.plusDays(10)), dateTimeToTimeOnly(now))

        then:
        pdtps.size() == 2
        pdtps[0].startTime < pdtps[1].startTime
    }

    void "should find all not finished for event for given date and time (escape one)"() {
        when:
        def pdtps = pdtpQueryService.findAllNotFinishedForEventFrom(Event.findByTitle('title2').id,
            dateTimeToDateOnly(now.plusDays(10)), dateTimeToTimeOnly(now), Pdtp.findByPlace('2-place1').id)

        then:
        pdtps.size() == 1
    }

    void "should find all newest from given date"() {
        when:
        def pdtp = pdtpQueryService.findAllNewestFrom(dateTimeToDateOnly(now), 2)

        then:
        pdtp.size() == 2
        pdtp[0].dateCreated > pdtp[1].dateCreated
        pdtp.each {
            assert it.event.institution.email != 'inst1@email.com'
            assert it.event.category.name != 'cat1'
        }
    }

    void "should find all newest from given date and for given category and institution"() {
        when:
        def pdtp = pdtpQueryService.findAllNewestFrom(dateTimeToDateOnly(now), 2, Category.findByName('cat1').id,
            Institution.findByEmail('inst1@email.com').id)

        then:
        pdtp.size() == 2
        pdtp[0].dateCreated > pdtp[1].dateCreated
        pdtp.each {
            assert it.event.institution.email == 'inst1@email.com'
            assert it.event.category.name == 'cat1'
        }
    }

    void "should find all random for given date"() {
        when:
        def pdtp = pdtpQueryService.findAllNotFinishedRandomly(dateTimeToDateOnly(now), 2)

        then:
        pdtp.size() == 2
    }

    void "should find all random for given date and for given category and institution"() {
        when:
        def pdtp = pdtpQueryService.findAllNotFinishedRandomly(dateTimeToDateOnly(now), 2,
            Category.findByName('cat1').id, Institution.findByEmail('inst1@email.com').id)

        then:
        pdtp.size() == 2
        pdtp.each {
            assert it.event.institution.email == 'inst1@email.com'
            assert it.event.category.name == 'cat1'
        }
    }

    void "should find all random for given date and for given category and institution, excluding id"() {
        setup:
        def categoryId = Category.findByName('cat2').id
        def instId = Institution.findByEmail('inst2@email.com').id
        def date = dateTimeToDateOnly(now)
        def limitTo = 2
        def pdtp = pdtpQueryService.findAllNotFinishedRandomly(date, limitTo, categoryId, instId)
        assert pdtp.size() == 2
        def idOfPdtpToExclude = Pdtp.findByPlace('3-place1').id

        when:
        pdtp = pdtpQueryService.findAllNotFinishedRandomly(date, limitTo, categoryId, instId,
            [idOfPdtpToExclude])

        then:
        pdtp.size() == 1
        pdtp[0].id != idOfPdtpToExclude
        pdtp[0].event.institution.email == 'inst2@email.com'
        pdtp[0].event.category.name == 'cat2'
    }

    def setup() {
        def cat1 = new Category(name: 'cat1').save()
        def cat2 = new Category(name: 'cat2').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1').save()
        def inst2 = new Institution(name: 'name-inst2', email: 'inst2@email.com', password: 'password2').save()
        def futureDate = dateTimeToDateOnly(now.plusDays(10))
        def pastDate = dateTimeToDateOnly(now.minusDays(1))
        def nowDate = dateTimeToDateOnly(now)
        def time = dateTimeToTimeOnly(now)
        def event1 = new Event(title: 'title1', description: 'desc1', category: cat1,
            institution: inst1, oneTimeType: true)
        def fd = futureDate
        (1..2).each { index ->
            event1.addToPdtps(new Pdtp(place: '1-place$index', fromDate: fd.toDate(), toDate: fd.toDate(),
                startTime: time.toDate(), price: '1-price$index'))
            fd = futureDate.plusDays(3 - index)
        }
        event1.addToPdtps(new Pdtp(place: 'past-place$index', fromDate: pastDate.toDate(), toDate: pastDate.toDate(),
                startTime: time.toDate(), price: 'past-price$index'))
        event1.save(validate: false)
        def event2 = new Event(title: 'title2', description: 'desc2', category: cat1,
            institution: inst1, oneTimeType: false)
        (1..2).each { index ->
            event2.addToPdtps(new Pdtp(place: "2-place$index", fromDate: futureDate.toDate(), toDate: futureDate.toDate(),
                startTime: time.plusHours(3 - index).toDate(), price: "2-price$index", timeDescription: "timeDescription$index"))
        }
        event2.save(validate: false)
        def event3 = new Event(title: 'title3', description: 'desc3', category: cat2,
            institution: inst2, oneTimeType: true)
        (1..2).each { index ->
            event3.addToPdtps(new Pdtp(place: "3-place$index", fromDate: nowDate.toDate(), toDate: nowDate.toDate(),
                startTime: time.minusHours(index - 1).toDate(), price: "3-price$index"))
        }
        event3.save(validate: false)
        sessionFactory.currentSession.clear()
    }
}
