package agenda

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.GONE
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import grails.converters.JSON

class EventPresentationController {

    def eventPresentationFacadeService

    def byDate() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            if (params.date) {
                render eventPresentationFacadeService.showByDate(params.date, params.category, params.inst) as JSON
            } else {
                render status: BAD_REQUEST.value
            }
        }
    }

    def byEvent() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            if (params.id) {
                def event = eventPresentationFacadeService.showByPdtp(params.id as long)
                if (event) {
                    render event as JSON
                } else {
                    render status: GONE.value
                }
            } else {
                render status: BAD_REQUEST.value
            }
        }
    }

    def submitted() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            if (params.id) {
                render eventPresentationFacadeService.submittedEvents(params.id as long) as JSON
            } else {
                render status: BAD_REQUEST.value
            }
        }
    }
}
