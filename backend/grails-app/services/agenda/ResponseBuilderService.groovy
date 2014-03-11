package agenda

import static agenda.PresentationContext.locale

import javax.annotation.PostConstruct

class ResponseBuilderService {

    static transactional = false

    def grailsApplication
    private g

    def makeGlobalMsg(messageId, parameters=[]) {
        [global: [g.message(code: messageId, args: parameters, locale: locale)]]
    }

    def makeMsgsFromErrors(errors) {
        def global = []
        def fields = [:]
        errors.fieldErrors?.each {
            fields << [(it.field): g.message(error:it, locale: locale)]
        }
        errors.globalErrors?.each {
            global << g.message(error:it, locale: locale)
        }
        [global: global, fields: fields]
    }

    @PostConstruct
    void init() {
        g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }
}
