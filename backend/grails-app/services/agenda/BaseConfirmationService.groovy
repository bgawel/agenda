package agenda

import static agenda.PresentationContext.locale
import grails.events.Listener

import javax.annotation.PostConstruct

import com.grailsrocks.emailconfirmation.EmailConfirmationService

class BaseConfirmationService {

    static transactional = false

    def grailsApplication
    def appUrlService
    def emailConfirmationService

    @Listener(topic='invalid', namespace='plugin.emailConfirmation')
    def receivedConfirmationWasInvalid(info) {
        log.warn "Invalid confirmation; info $info; $warning"
        onInvalid
    }

    def onInvalid = { render(view: '/rc/invalid', model: [locale: locale]) }

    @PostConstruct
    def init() {
        emailConfirmationService.maxAge = grailsApplication.config.agenda.confirm.timeout
        EmailConfirmationService.metaClass.makeURL = { token ->
            "${appUrlService.makeServerUrl()}/confirm/${token.encodeAsURL()}"
        }
    }
}
