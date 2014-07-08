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
    //def securityContextPersistenceFilter

    def init = { servletContext ->
        //securityContextPersistenceFilter.repo.allowSessionCreation = false
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
        def classical = new Category(name: 'Classical & Opera').save()
        def dance = new Category(name: 'Dance').save()
        def gallery = new Category(name: 'Gallery').save()
        def jazz = new Category(name: 'Jazz').save()
        def movie = new Category(name: 'Movie').save()
        def museum = new Category(name: 'Museum').save()
        def publicArt = new Category(name: 'Public Art').save()
        def rockNpop = new Category(name: 'Rock & Pop').save()
        def theater = new Category(name: 'Theater').save()
        def forChilren = new Category(name: 'For Children').save()

        def admin = new Institution(name: Role.ADMIN, email: 'admin@agenda.com', password: 'admin-password',
            internal: true, enabled: true).save()
        UserRole.create admin, Role.createAdmin()
        def inst1 = new Institution(name: 'Institution 1', email: 'inst1@email.com', password: 'inst1-password',
            address: 'Address 1', web: 'www.inst1.com', enabled: true).save()
        def inst2 = new Institution(name: 'Institution 2', email: 'inst2@email.com', password: 'inst2-password',
            address: 'Address 2', web: 'www.inst2.com', phone: '555 444 666', enabled: true).save()
        def nowDate = dateTimeToDateOnly(currentDateTime)
        def time = toTimeOnly(15, 0)
        def description =
        '''
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut
            labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
            aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum
            dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui
            officia deserunt mollit anim id est laborum
        '''
        def event1 = new Event(title: 'Title 1', description: description, category: classical,
            institution: inst1, oneTimeType: true)
        (0..7).each { index ->
            event1.addToPdtps(new Pdtp(place: "Place 1, $index", fromDate: nowDate.plusDays(index).toDate(),
                toDate: nowDate.plusDays(index).toDate(),
                startTime: time.plusHours(index).toDate(), price: "2$index" + '$'))
        }
        event1.save()
        time = toTimeOnly(11, 0)
        description =
        '''
            Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium,
            totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta
            sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia
            consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est,
            qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi
            tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam,
            quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?
            Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur,
            vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?
        '''
        def event2 = new Event(title: 'Title 2', description: description, category: gallery,
            institution: inst2, oneTimeType: false)
        event2.addToPdtps(new Pdtp(place: 'Place 2', fromDate: nowDate.toDate(), toDate: nowDate.plusDays(30).toDate(),
            startTime: time.toDate(), price: 'free of charge',
            timeDescription: 'Mo - Fr: 13.00-20.00, Sa: 11.00-15.00'))
        event2.save()
    }
}
