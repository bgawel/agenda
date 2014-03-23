package agenda

import static agenda.PresentationContext.locale
import grails.events.Listener
import grails.transaction.Transactional

import javax.annotation.PostConstruct

import org.codehaus.groovy.grails.web.util.WebUtils

class ResetPwdConfirmationService {

    static transactional = false

    def grailsApplication
    def emailConfirmationService
    def baseConfirmationService
    private g

    def sendConfirmation(inst) {
        emailConfirmationService.sendConfirmation(
            to: inst.email,
            from: grailsApplication.config.grails.mail.default.from,
            subject: g.message(code:'confirmation.signup.subject', locale: locale),
            id: inst.id,
            event: 'resetPwd',
            view: '/emails/resetPwdConfirmationRequest',
            model: [locale: locale])
    }

    @Transactional
    @Listener(topic='resetPwd.confirmed', namespace='plugin.emailConfirmation')
    def receivedConfirmation(info) {
        def inst = Institution.get(info.id)
        if (inst) {
            inst.password = WebUtils.retrieveGrailsWebRequest().params['password']
            def confirmed = { render(view: '/panel/', model: [locale: locale]) }
            log.info "Reset password confirmed for inst ${info.email}"
            confirmed
        } else {
            log.warn "Could not find inst with id=${info.id}; info $info"
            baseConfirmationService.onInvalid
        }
    }

    @Listener(topic='resetPwd.timeout', namespace='plugin.emailConfirmation')
    def receivedConfirmationTimedOut(info) {
        log.info "Reset password timeout; info $info"
    }

    @PostConstruct
    def init() {
        g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }
}
