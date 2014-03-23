package agenda

import static agenda.PresentationContext.locale

import javax.annotation.PostConstruct

class InstitutionResourceService {

    def signupConfirmationService
    def grailsApplication
    private g

    def show(id) {
        def inst = Institution.get(id)
        if (inst) {
            convert(inst)
        }
    }

    def save(inst) {
        def newInst = persistInst(inst)
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

    def update(inst) {
        convert(persistInst(inst))
    }

    def delete(inst) {
        inst.delete()
        true
    }

    private convert(inst) {
        [id: inst.id, name: inst.name, email: inst.email, password: inst.password, address: inst.address,
            web: inst.web, telephone: inst.telephone]
    }

    private persistInst(inst) {
        inst.save(validate: false)
    }

    @PostConstruct
    def init() {
        g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }
}
