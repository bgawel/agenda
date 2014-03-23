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
        def sendTo = signupConfig.adminmustconfirm ? config.agenda.mail.admin :
            (signupConfig.usermustconfirm ? inst.email : null)
        if (sendTo) {
            emailConfirmationService.sendConfirmation(
                to: sendTo,
                from: config.grails.mail.default.from,
                subject: g.message(code:'confirmation.signup.subject', locale: locale),
                id: inst.id,
                event: 'signup',
                view: '/emails/signupConfirmationRequest',
                model: [locale: locale])
        } else {
            enableInst(inst)
        }
    }

    @Transactional
    @Listener(topic='signup.confirmed', namespace='plugin.emailConfirmation')
    def receivedConfirmation(info) {
        def inst = Institution.get(info.id)
        if (inst) {
            enableInst(inst)
            def confirmed = { render(view: '/rc/signup', model: [locale: locale]) }
            if (signupConfig.adminmustconfirm) {
                sendEmailWithStatusConfirmedTo(inst.email)
            }
            log.info "Signup confirmed for inst ${info.email}"
            confirmed
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

    private sendEmailWithStatusConfirmedTo(sendTo) {
        new OnTransaction(
            null,
            {
                try {
                    sendMail {
                        to sendTo
                        subject g.message(code:'confirmed.signup.subject', locale: locale),
                        body(view: '/confirmation/signup', model: [locale: locale])
                    }
                } catch (e) {
                    log.error "Could not send email with status confirmed to $sendTo after commit", e
                    throw new RuntimeException(e)
                }
            }
        )
    }

    @PostConstruct
    def init() {
        g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
        config = grailsApplication.config
        signupConfig = config.agenda.signup
    }

    private enableInst(inst) {
        inst.enabled = true
        inst.save()
    }
}
