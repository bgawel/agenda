package agenda

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Mixin

@Mixin(AddControllerApi)
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
        respondWithCacheHeaders this, eventProjectionFacadeService.submittedEvents(params.id as long)
    }
}
