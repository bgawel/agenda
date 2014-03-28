package agenda

import grails.plugin.springsecurity.annotation.Secured
import grails.util.Mixin

@Mixin(AddControllerApi)
class EventProjectionController {

    static responseFormats = ['json']

    def eventProjectionFacadeService

    def byDate() {
        cache shared:true, validFor:300
        respond eventProjectionFacadeService.showByDate(params.date, params.category, params.inst)
    }

    def byEvent() {
        cache shared:true, validFor:300
        respond eventProjectionFacadeService.showByPdtp(params.id as long)
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def submitted() {
        respondWithCacheHeaders this, eventProjectionFacadeService.submittedEvents(params.id as long)
    }
}
