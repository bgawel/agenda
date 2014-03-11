package agenda

import org.joda.time.DateTime

import spock.lang.Specification
import spock.lang.Unroll

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
        event.errors.fieldErrors.size()
    }

    def "should check if toDate is before now for a new pdtp"() {
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
        event.errors.fieldErrors.size()
    }

    def "should check if toDate is before now for an existing pdtp"() {
        setup:
        def event = Event.findByTitle('title1')
        event.pdtps[0].fromDate = date.minusDays(2).toDate()
        event.pdtps[0].toDate = date.minusDays(1).toDate()

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.each {
            assert it.field == 'pdtps[0].toDate'
            assert it.codes.find { it == 'pdtp.toDate.toDateBeforeNow' }
        }
        event.errors.fieldErrors.size()
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
        event.errors.fieldErrors.size()
    }

    def "should check if two date ranges overlap for one time type"() {
        setup:
        def event = Event.findByTitle('title1')
        event.addToPdtps(new Pdtp(place: 'place', startTime: date.toDate(), price: 'price',
                fromDate: date.minusDays(1).toDate(), toDate: date.plusDays(1).toDate()))

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.globalErrors.each {
            assert it.arguments.size() == 2
            assert it.arguments[0]
            assert it.arguments[1]
            assert it.codes.find { it == 'pdtps.sameDateTime' }
        }
        event.errors.globalErrors.size()
    }

    def "should check if timeDescription present if tmp type"() {
        setup:
        def event = Event.findByTitle('title1')
        event.oneTimeType = false

        when:
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.each {
            assert it.field == 'pdtps[0].timeDescription'
            assert it.codes.find { it == 'pdtp.timeDescription.required' }
        }
        event.errors.fieldErrors.size()
    }

    @Unroll
    def "should check if url is correct"(String url, _) {
        setup:
        def event = Event.findByTitle('title1')

        when:
        event.more = url
        event.validate()

        then:
        event.hasErrors()
        event.errors.fieldErrors.each {
            assert it.field == 'more'
            assert it.codes.find { it == 'event.more.incorrectUrl' }
        }
        event.errors.fieldErrors.size()

        where:
        url                     | _
        'wwww.example'          | _
        'httpwww.example.com'   | _
    }

    @Unroll
    def "should allow to save a correct url"(String url, _) {
        setup:
        def event = Event.findByTitle('title1')

        when:
        event.more = url
        event.validate()

        then:
        !event.hasErrors()

        where:
        url                         | _
        'wwww.example.com'          | _
        'http://www.example.com'    | _
        'https://www.example.com'   | _
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
