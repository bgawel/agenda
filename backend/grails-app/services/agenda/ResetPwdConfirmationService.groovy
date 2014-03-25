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
    private config

    def sendConfirmation(inst) {
        emailConfirmationService.sendConfirmation(
            to: inst.email,
            from: baseConfirmationService.defaultEmail,
            subject: g.message(code:'confirmation.resetpwd.subject', locale: locale),
            id: inst.id,
            event: 'resetPwd',
            view: '/emails/resetPwdConfirmationRequest',
            model: [locale: locale, baseUri: config.baseUri, username: inst.email])
    }

    @Transactional
    @Listener(topic='resetPwd.confirmed', namespace='plugin.emailConfirmation')
    def receivedConfirmation(info) {
        def inst = Institution.get(info.id)
        if (inst) {
            inst.password = WebUtils.retrieveGrailsWebRequest().params['password']
            log.info "Reset password confirmed for inst ${info.email}"
            config.confirmed
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
        config = grailsApplication.config.agenda.setpwd
    }
}
