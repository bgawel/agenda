package agenda

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class EventController {

    static responseFormats = ['json']

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def responseBuilderService
    def eventResourceService

    def show() {
        respond eventResourceService.show(params.id)
    }

    def save() {
        def event = bindWithParams(new Event())
        eventResourceService.validate(event)
        if (event.hasErrors()) {
            respond((Object)responseBuilderService.makeMsgsFromErrors(event.errors), [status: UNPROCESSABLE_ENTITY])
        } else {
            respond((Object)eventResourceService.save(event), [status: CREATED])
        }
    }

    def update() {
        def event = Event.get(params.id)
        if (event) {
            def readonlyPdtpIds = eventResourceService.getReadonlyPdtpIds(event)
            bindWithParams(event)
            eventResourceService.validate(event, readonlyPdtpIds)
            if (event.hasErrors()) {
                respond((Object)responseBuilderService.makeMsgsFromErrors(event.errors), [status: UNPROCESSABLE_ENTITY])
            } else {
                respond((Object)eventResourceService.update(event), [status: OK])
            }
        } else {
            render status: NOT_FOUND
        }
    }

    def delete() {
        def event = Event.get(params.id)
        if (event) {
            eventResourceService.checkIfCanDelete(event)
            if (event.hasErrors()) {
                respond((Object)responseBuilderService.makeMsgsFromErrors(event.errors), [status: UNPROCESSABLE_ENTITY])
            } else {
                eventResourceService.delete(event)
                render status: NO_CONTENT
            }
        } else {
            render status: NOT_FOUND
        }
    }

    private bindWithParams(event) {
        bindData(event, params, [include: ['title', 'pic', 'more', 'description', 'oneTimeType', 'category', 'pdtps',
            // TODO bgawel: only until logging is implemented
            'institution']])
    }
}
