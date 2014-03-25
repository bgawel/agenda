package agenda

import static agenda.PresentationContext.locale
import grails.events.Listener

import javax.annotation.PostConstruct

import org.codehaus.groovy.grails.web.util.WebUtils

import com.grailsrocks.emailconfirmation.EmailConfirmationService

class BaseConfirmationService {

    static transactional = false

    def grailsApplication
    def appUrlService
    def emailConfirmationService
    private config

    @Listener(topic='invalid', namespace='plugin.emailConfirmation')
    def receivedConfirmationWasInvalid(info) {
        log.warn "Invalid confirmation; info $info"
        onInvalid
    }

    def getOnInvalid() {
        def configPrefix = config.agenda.confirm.invalid
        WebUtils.retrieveGrailsWebRequest().params['xhrMode'] ? configPrefix.xhrMode : configPrefix.redirect
    }

    @PostConstruct
    def init() {
        config = grailsApplication.config
        emailConfirmationService.maxAge = config.agenda.confirm.timeout
        EmailConfirmationService.metaClass.makeURL = { token ->
            "${appUrlService.makeServerUrl()}/confirm/${token.encodeAsURL()}"
        }
    }

    def getAdminEmail() {
        config.agenda.mail.admin
    }

    def getDefaultEmail() {
        config.grails.mail.default.from
    }
}
