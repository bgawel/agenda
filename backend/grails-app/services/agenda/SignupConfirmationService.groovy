package agenda

import static agenda.PresentationContext.locale
import grails.events.Listener
import grails.transaction.Transactional

import javax.annotation.PostConstruct

class SignupConfirmationService {

    def grailsApplication
    def emailConfirmationService
    def baseConfirmationService
    private g
    private config
    private signupConfig

    @Transactional
    def sendConfirmation(inst) {
        if (signupConfig.adminmustconfirm) {
            sendEmailConfirmation(baseConfirmationService.adminEmail, inst.id, inst)
        }
        else if (signupConfig.usermustconfirm) {
            sendEmailConfirmation(inst.email, inst.id)
        } else {
            enableInst(inst)
        }
    }

    @Transactional
    @Listener(topic='signup.confirmed', namespace='plugin.emailConfirmation')
    def receivedConfirmation(info) {
        def inst = Institution.get(info.id)
        if (inst) {
            def onConfirmed
            if (info.email == baseConfirmationService.adminEmail) {
                sendEmailConfirmation(inst.email, inst.id)
                onConfirmed = signupConfig.adminconfirmed
            } else if (info.email == inst.email) {
                enableInst(inst)
                onConfirmed = signupConfig.userconfirmed
            } else {
                throw new IllegalStateException("Received confirmation from unknown email $info")
            }
            log.info "Signup confirmed for inst ${info.email}"
            onConfirmed
        } else {
            log.warn "Could not find inst with id=${info.id}; info $info"
            baseConfirmationService.onInvalid
        }
    }

    @Transactional
    @Listener(topic='signup.timeout', namespace='plugin.emailConfirmation')
    def receivedConfirmationTimedOut(info) {
        log.info "Signup timeout; info $info"
        def inst = Institution.get(info.id)
        if (inst) {
            inst.delete()
        }
    }

    def getActionCodeForUser() {
        signupConfig.adminmustconfirm ? 'confirmation.signup.adminmustconfirm' :
            (signupConfig.usermustconfirm ? 'confirmation.signup.usermustconfirm' : null)
    }

    private sendEmailConfirmation(sendTo, instId, inst=null) {
        emailConfirmationService.sendConfirmation(
            to: sendTo,
            from: baseConfirmationService.defaultEmail,
            subject: g.message(code:'confirmation.signup.subject', locale: locale),
            id: instId,
            event: 'signup',
            view: '/emails/signupConfirmationRequest',
            model: [locale: locale, inst: inst])
    }

    private enableInst(inst) {
        inst.enabled = true
        inst.save()
    }

    @PostConstruct
    def init() {
        g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
        config = grailsApplication.config
        signupConfig = config.agenda.signup
    }
}
