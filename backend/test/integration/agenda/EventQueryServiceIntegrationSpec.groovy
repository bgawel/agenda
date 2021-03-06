package agenda

import static agenda.LocalContext.getCurrentDate
import spock.lang.Specification

class EventQueryServiceIntegrationSpec extends Specification {

    def eventQueryService
    def sessionFactory

    def "should find all events for a given institution"() {
        when:
        def events = eventQueryService.findAllByInstitution(Institution.findByEmail('inst1@email.com').id)

        then:
        events.size() == 2
        events[0].lastUpdated > events[1].lastUpdated
    }

    def setup() {
        def cat = new Category(name: 'cat1').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1').save()
        def date = currentDate.toDate()
        (1..2).each { index ->
            def event = new Event(title: "title$index", description: 'desc', category: cat,
                institution: inst1, oneTimeType: true)
            event.addToPdtps(new Pdtp(place: 'place', fromDate: date, toDate: date, startTime: date, price: 'price'))
            event.save()
        }
        sessionFactory.currentSession.flush()
        sessionFactory.currentSession.clear()
    }
}
