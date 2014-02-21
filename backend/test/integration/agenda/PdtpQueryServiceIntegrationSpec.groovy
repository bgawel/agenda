package agenda

import static LocalContext.dateTimeToDateOnly;
import static LocalContext.dateTimeToTimeOnly;
import spock.lang.Ignore;
import spock.lang.Specification;

import org.joda.time.DateTime;

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
        pdtps[0].time == pdtps[1].time
        def event2Id = Event.findByTitle('title2').id
        pdtps[2].event.id == event2Id
        pdtps[3].event.id == event2Id
        pdtps[2].fromDate == pdtps[3].fromDate
        pdtps[2].time < pdtps[3].time
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
        pdtps[0].time == pdtps[1].time
        def event2Id = Event.findByTitle('title2').id
        pdtps[2].event.id == event2Id
        pdtps[3].event.id == event2Id
        pdtps[2].fromDate == pdtps[3].fromDate
        pdtps[2].time < pdtps[3].time
        pdtps[4].event.id == Event.findByTitle('title3').id
        pdtps[4].place == '3-place1'
    }
    
    void "should find all for given date"() {
        when:
        def pdtps = pdtpQueryService.findAllFor(dateTimeToDateOnly(now))
        
        then:
        pdtps.size() == 2
        pdtps[0].time < pdtps[1].time
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
        pdtps[0].time < pdtps[1].time
    }
    
    void "should find all not finished for event for given date and time (escape one)"() {
        when:
        def pdtps = pdtpQueryService.findAllNotFinishedForEventFrom(Event.findByTitle('title2').id,
            dateTimeToDateOnly(now.plusDays(10)), dateTimeToTimeOnly(now), Pdtp.findByPlace('2-place1').id)
        
        then:
        pdtps.size() == 1
    }
    
    def setup() {
        def cat = new Category(name: 'cat1').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1').save()
        def futureDate = dateTimeToDateOnly(now.plusDays(10))
        def pastDate = dateTimeToDateOnly(now.minusDays(1))
        def nowDate = dateTimeToDateOnly(now)
        def time = dateTimeToTimeOnly(now)
        def event1 = new Event(title: 'title1', description: 'desc1', category: cat,
            institution: inst1, isOneTimeType: true)
        def fd = futureDate
        (1..2).each { index ->
            event1.addToPdtps(new Pdtp(place: '1-place$index', fromDate: fd.toDate(), toDate: fd.toDate(),
                time: time.toDate(), price: '1-price$index'))
            fd = futureDate.plusDays(3 - index)
        }
        event1.addToPdtps(new Pdtp(place: 'past-place$index', fromDate: pastDate.toDate(), toDate: pastDate.toDate(),
                time: time.toDate(), price: 'past-price$index'))
        event1.save()
        def event2 = new Event(title: 'title2', description: 'desc2', category: cat,
            institution: inst1, isOneTimeType: false)
        (1..2).each { index ->
            event2.addToPdtps(new Pdtp(place: "2-place$index", fromDate: futureDate.toDate(), toDate: futureDate.toDate(),
                time: time.plusHours(3 - index).toDate(), price: "2-price$index", timeDescription: "timeDescription$index"))
        }
        event2.save()
        def event3 = new Event(title: 'title3', description: 'desc3', category: cat,
            institution: inst1, isOneTimeType: true)
        (1..2).each { index ->
            event3.addToPdtps(new Pdtp(place: "3-place$index", fromDate: nowDate.toDate(), toDate: nowDate.toDate(),
                time: time.minusHours(index - 1).toDate(), price: "3-price$index"))
        }
        event3.save(flush:true)
        sessionFactory.currentSession.clear()
    }
}
