package agenda

import org.joda.time.DateTime

import spock.lang.Specification

class EventResourceServiceIntegrationSpec extends Specification {

    def date = new DateTime()
    def sessionFactory
    def eventResourceService

    def "should discard changes made for readonly pdtp and restore removed readonly pdtp"() {
        setup:
        def event = Event.findByTitle('title1')
        def readonlyPdtpIds = [event.pdtps[0].id, event.pdtps[2].id]
        event.title = 'new title'
        event.pdtps[0].place = 'new place 1'
        event.pdtps[1].place = 'new place 2'
        event.pdtps.remove(2)
        assert event.pdtps.size() == 2

        when:
        eventResourceService.validate(event, readonlyPdtpIds)

        then:
        event.pdtps.size() == 3
        event.title == 'title1'                 //
        event.pdtps[0].place == 'place1'        // restored value
        event.pdtps[1].place == 'new place 2'   // updated
        event.pdtps[2].place == 'place3'        // restored object
    }

    def setup() {
        def cat1 = new Category(name: 'cat1').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1').save()
        def event1 = new Event(title: 'title1', description: 'desc1', category: cat1,
            institution: inst1, oneTimeType: true)
        (1..3).each { index ->
            event1.addToPdtps(new Pdtp(place: "place$index", startTime: date.toDate(), price: 'price',
                fromDate: date.plusDays(index).toDate(), toDate: date.plusDays(index).toDate()))
        }
        event1.save(flush: true)
        sessionFactory.currentSession.clear()
    }
}
