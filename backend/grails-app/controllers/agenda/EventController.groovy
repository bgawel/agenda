package agenda

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class EventController {

    static responseFormats = ['json']

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def responseBuilderService
    def eventResourceService

    def show() {
        respond eventResourceService.show(params.id)
    }

    def save() {
        def event = bindWithParams(new Event(), true)
        def responseObject
        Event.withTransaction {
            eventResourceService.validate(event)
            if (event.hasErrors()) {
                respond responseBuilderService.makeMsgsFromErrors(event.errors), [status: UNPROCESSABLE_ENTITY]
            } else {
                responseObject = eventResourceService.save(event, params.picId)
            }
        }
        if (responseObject) {
            respond((Object)responseObject, [status: CREATED])
        }
    }

    def update() {
        def responseObject
        Event.withTransaction {
            def event = Event.get(params.id)
            if (event) {
                def readonlyPdtpIds = eventResourceService.getReadonlyPdtpIds(event)
                bindWithParams(event)
                eventResourceService.validate(event, readonlyPdtpIds)
                if (event.hasErrors()) {
                    respond responseBuilderService.makeMsgsFromErrors(event.errors), [status: UNPROCESSABLE_ENTITY]
                } else {
                    responseObject = eventResourceService.update(event, params.picId)
                }
            } else {
                render status: NOT_FOUND
            }
        }
        if (responseObject) {
            respond((Object)responseObject, [status: OK])
        }
    }

    def delete() {
        def responseObject
        Event.withTransaction {
            def event = Event.get(params.id)
            if (event) {
                eventResourceService.checkIfCanDelete(event)
                if (event.hasErrors()) {
                    respond responseBuilderService.makeMsgsFromErrors(event.errors), [status: UNPROCESSABLE_ENTITY]
                } else {
                    responseObject = eventResourceService.delete(event)
                }
            } else {
                render status: NOT_FOUND
            }
        }
        if (responseObject) {
            render status: NO_CONTENT
        }
    }

    private bindWithParams(event, includeInst=false) {
        def include = ['title', 'pic', 'more', 'description', 'oneTimeType', 'category', 'pdtps']
        if (includeInst) {
            include <<= 'institution'
        }
        bindData(event, params, [include: include])
    }
}
