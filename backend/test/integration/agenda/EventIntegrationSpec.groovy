package agenda

import org.joda.time.DateTime

import spock.lang.Specification

class EventIntegrationSpec extends Specification {

    def date = new DateTime()
    def sessionFactory

    def "should check if from date <= to date"() {
        setup:
        def event = Event.findByTitle('title1')
        event.pdtps[0].fromDate = date.plusDays(2).toDate()

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.each {
            assert it.field == 'pdtps[0].fromDate'
            assert it.codes.find { it == 'pdtp.fromDate.fromGreaterThanTo' }
        }
        true
    }

    def "should check if toDate is before now"() {
        setup:
        def event = Event.findByTitle('title1')
        event.addToPdtps(new Pdtp(place: 'place', startTime: date.toDate(), price: 'price',
                fromDate: date.minusDays(2).toDate(), toDate: date.minusDays(1).toDate()))

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.each {
            assert it.field == 'pdtps[1].toDate'
            assert it.codes.find { it == 'pdtp.toDate.toDateBeforeNow' }
        }
        true
    }

    def "should check if description or more"() {
        setup:
        def event = Event.findByTitle('title1')
        event.description = null
        event.more = null

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.each {
            assert it.field == 'description'
            assert it.codes.find { it == 'event.description.descriptionOrMoreRequired' }
        }
        true
    }

    def "should check if two date ranges overlap"() {
        setup:
        def event = Event.findByTitle('title1')
        event.addToPdtps(new Pdtp(place: 'place', startTime: date.toDate(), price: 'price',
                fromDate: date.minusDays(1).toDate(), toDate: date.plusDays(1).toDate()))

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.size() == 4
        event.errors.fieldErrors.each {
            assert it.field == 'pdtps[0].toDate' ||
                   it.field == 'pdtps[0].fromDate' ||
                   it.field == 'pdtps[1].toDate' ||
                   it.field == 'pdtps[1].fromDate'
            assert it.codes.find { it == 'pdtps.datesOverlap' }
        }
        true
    }

    def setup() {
        def cat1 = new Category(name: 'cat1').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1').save()
        def event1 = new Event(title: 'title1', description: 'desc1', category: cat1,
            institution: inst1, oneTimeType: true)
        event1.addToPdtps(new Pdtp(place: 'place', startTime: date.toDate(), price: 'price',
                fromDate: date.plusDays(1).toDate(), toDate: date.plusDays(1).toDate()))
        event1.save(flush: true)
        sessionFactory.currentSession.clear()
    }
}
