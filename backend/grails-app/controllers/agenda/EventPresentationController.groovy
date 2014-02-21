package agenda

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.GONE
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import grails.converters.JSON

class EventPresentationController {

    def eventPresentationService

    def byDate() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            if (params.date) {
                render eventPresentationService.showByDate(params.date, params.category, params.inst) as JSON
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
                def event = eventPresentationService.showByPdtp(params.long(id))
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
                render eventPresentationService.submittedEvents(params.long(params.id)) as JSON
            } else {
                render status: BAD_REQUEST.value
            }
        }
    }
}
