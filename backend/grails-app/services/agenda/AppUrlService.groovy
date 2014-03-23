package agenda

import org.codehaus.groovy.grails.web.util.WebUtils

class AppUrlService {

    static transactional = false

    def grailsApplication

    def makeServerUrl() {
        def context = grailsApplication.config.grails.app.context
        def serverURL = grailsApplication.config.grails.serverURL
        def request = retrieveRequest()
        if (!serverURL) {
            serverURL = "http://${request.serverName}:${request.serverPort}"
        }
        if (!context) {
            context = request.contextPath
        }
        "$serverURL$context"
    }

    private retrieveRequest() {
        WebUtils.retrieveGrailsWebRequest().currentRequest
    }
}
