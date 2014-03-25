package agenda.security

import org.codehaus.groovy.grails.web.util.WebUtils

class RestReauthenticationService {

    def grailsApplication
    def springSecurityService
    def tokenStorageService

    def reauthenticate(username, newPwd=null) {
        springSecurityService.reauthenticate(username, newPwd)
        tokenStorageService.storeToken(retrieveSecurityTokenAssociatedWithRequest(), springSecurityService.principal)
    }

    private retrieveSecurityTokenAssociatedWithRequest() {
        WebUtils.retrieveGrailsWebRequest().currentRequest.getHeader(
            grailsApplication.config.grails.plugin.springsecurity.rest.token.validation.headerName)
    }
}
