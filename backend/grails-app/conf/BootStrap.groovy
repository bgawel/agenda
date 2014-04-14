import static agenda.LocalContext.dateTimeToDateOnly
import static agenda.LocalContext.getCurrentDateTime
import static agenda.LocalContext.toTimeOnly
import grails.util.Environment
import agenda.Category
import agenda.Event
import agenda.Institution
import agenda.Pdtp
import agenda.security.Role
import agenda.security.UserRole

class BootStrap {

    def messageSource
    def securityContextPersistenceFilter

    def init = { servletContext ->
        securityContextPersistenceFilter.repo.allowSessionCreation = false
        switch(Environment.current) {
            case Environment.DEVELOPMENT:
                messageSource.useCodeAsDefaultMessage = true
                initDevelopmentModeData()
                break
            // TODO bgawel: tmp
            case Environment.PRODUCTION:
                messageSource.useCodeAsDefaultMessage = true
                initDevelopmentModeData()
                break
        }
    }
    def destroy = {
    }

    def initDevelopmentModeData() {
        def admin = new Institution(name: Role.ADMIN, email: 'info@agenda.pl', password: 'admin-password',
            internal: true, enabled: true).save()
        UserRole.create admin, Role.createAdmin()

        def cat1 = new Category(name: 'cat1').save()
        def cat2 = new Category(name: 'cat2').save()
        def inst1 = new Institution(name: 'name-inst1', email: 'inst1@email.com', password: 'password1',
            address: 'address1', web: 'www.google.com', enabled: true).save()
        def inst2 = new Institution(name: 'name-inst2', email: 'inst2@email.com', password: 'password2',
            enabled: true).save()
        def nowDate = dateTimeToDateOnly(currentDateTime)
        def time = toTimeOnly(18, 0)
        def description1 = ''
        (1..100).each { index ->
            description1 <<= "desc$index "
        }
        def event1 = new Event(title: 'title1', description: description1.toString(), category: cat1,
            institution: inst1, oneTimeType: true)
        (0..1).each { index ->
            event1.addToPdtps(new Pdtp(place: "1-place$index", fromDate: nowDate.plusDays(index).toDate(),
                toDate: nowDate.plusDays(index).toDate(),
                startTime: time.plusHours(index).toDate(), price: "1-price$index"))
        }
        event1.save()
        def event2 = new Event(title: 'title2', description: 'desc2', category: cat2,
            institution: inst2, oneTimeType: false)
        (0..1).each { index ->
            event2.addToPdtps(new Pdtp(place: "2-place$index", fromDate: nowDate.plusDays(index).toDate(),
                toDate: nowDate.plusDays(index * 3).toDate(),
                startTime: time.plusHours(index).toDate(), price: "2-price$index",
                timeDescription: "$index wernisaż: 7 lutego, piątek, o godz. 18:00, poniedziałek – piątek: " +
                    '13.00-20.00, sobota: 11.00-15.00'))
        }
        event2.save()
    }
}
