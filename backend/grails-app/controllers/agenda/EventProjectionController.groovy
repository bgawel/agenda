package agenda

import grails.plugin.springsecurity.annotation.Secured

class EventProjectionController {

    static responseFormats = ['json']

    def eventProjectionFacadeService

    def byDate() {
        respond eventProjectionFacadeService.showByDate(params.date, params.category, params.inst)
    }

    def byEvent() {
        respond eventProjectionFacadeService.showByPdtp(params.id as long)
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def submitted() {
        respond eventProjectionFacadeService.submittedEvents(params.id as long)
    }
}
