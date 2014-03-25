package agenda

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import grails.plugin.springsecurity.annotation.Secured

class InstitutionController {

    static responseFormats = ['json']

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def responseBuilderService
    def institutionResourceService

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def show() {
        respond institutionResourceService.show(params.id)
    }

    def save() {
        def inst = bindWithParams(new Institution(), true)
        def responseObject
        Event.withTransaction {
            inst.validate()
            if (inst.hasErrors()) {
                respond responseBuilderService.makeMsgsFromErrors(inst.errors), [status: UNPROCESSABLE_ENTITY]
            } else {
                responseObject = institutionResourceService.save(inst)
            }
        }
        if (responseObject) {
            respond((Object)responseObject, [status: CREATED])
        }
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def update() {
        def responseObject
        Event.withTransaction {
            def inst = Institution.get(params.id)
            if (inst) {
                bindWithParams(inst)
                inst.validate()
                if (inst.hasErrors()) {
                    respond responseBuilderService.makeMsgsFromErrors(inst.errors), [status: UNPROCESSABLE_ENTITY]
                } else {
                    responseObject = institutionResourceService.update(inst)
                }
            } else {
                render status: NOT_FOUND
            }
        }
        if (responseObject) {
            respond((Object)responseObject, [status: OK])
        }
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def delete() {
        def responseObject
        Event.withTransaction {
            def inst = Institution.get(params.id)
            if (inst) {
                responseObject = institutionResourceService.delete(inst)
            } else {
                render status: NOT_FOUND
            }
        }
        if (responseObject) {
            render status: NO_CONTENT
        }
    }

    private bindWithParams(inst, includePwd=false) {
        def include = ['name', 'email', 'address', 'web', 'telephone']
        if (includePwd) {
            include <<= 'password'
        }
        bindData(inst, params, [include:  include])
    }
}
