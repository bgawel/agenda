package agenda

import static agenda.PresentationContext.locale
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.CachePut
import grails.plugin.cache.Cacheable

import javax.annotation.PostConstruct

class InstitutionResourceService {

    def signupConfirmationService
    def grailsApplication
    def restReauthenticationService
    private g

    @Cacheable(value='inst', key='#id')
    def show(id) {
        def inst = Institution.get(id)
        if (inst) {
            convert(inst)
        }
    }

    def save(inst) {
        def newInst = addToSession(inst)
        new OnTransaction(
            null,
            {
                try {
                    signupConfirmationService.sendConfirmation(newInst)
                } catch (e) {
                    log.error "Could not send registration confirmation after commit for inst $newInst", e
                    try {
                        newInst.delete flush:true
                    } catch (eWhileDeleting) {
                        log.error "Could not delete inst; inst $newInst is orphaned", eWhileDeleting
                    }
                    throw new RuntimeException(e)
                }
            }
        )
        def messageCode = signupConfirmationService.actionCodeForUser
        if (messageCode) {
            [message: g.message(code: messageCode, locale: locale)]
        } else {
            convert(newInst)
        }
    }

    @CachePut(value='inst', key='#inst.id')
    def update(inst) {
        if (inst.email != inst.getPersistentValue('email')) {
            new OnTransaction(
                null,
                {
                    try {
                        restReauthenticationService.reauthenticate(inst.email)
                    } catch (e) {
                        log.error "Could not re-authenticate user after commit for inst $inst", e
                    }
                }
            )
        }
        def converted = convert(inst)
        publishEvent new InstitutionChangedEvent([updated: true, instId: inst.id])
        converted
    }

    @CacheEvict(value='inst, submittedEvents', key='#inst.id')
    def delete(inst) {
        inst.delete()
        publishEvent new InstitutionChangedEvent([deleted: true, instId: inst.id])
        true
    }

    private convert(inst) {
        [id: inst.id, name: inst.name, email: inst.email, address: inst.address, web: inst.web,
            telephone: inst.telephone, lastModified: inst.lastUpdated]
    }

    private addToSession(inst) {
        inst.save(validate: false)
    }

    @PostConstruct
    def init() {
        g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }
}
