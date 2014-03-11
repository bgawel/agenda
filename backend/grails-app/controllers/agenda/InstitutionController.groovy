package agenda

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class InstitutionController {

    static responseFormats = ['json']

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def responseBuilderService
    def institutionResourceService

    def show() {
        respond institutionResourceService.show(params.id)
    }

    def save() {
        def inst = bindWithParams(new Institution())
        inst.validate()
        if (inst.hasErrors()) {
            respond((Object)responseBuilderService.makeMsgsFromErrors(inst.errors), [status: UNPROCESSABLE_ENTITY])
        } else {
            respond((Object)institutionResourceService.save(inst), [status: CREATED])
        }
    }

    def update() {
        def inst = Institution.get(params.id)
        if (inst) {
            bindWithParams(inst)
            inst.validate()
            if (inst.hasErrors()) {
                respond((Object)responseBuilderService.makeMsgsFromErrors(inst.errors), [status: UNPROCESSABLE_ENTITY])
            } else {
                respond((Object)institutionResourceService.update(inst), [status: OK])
            }
        } else {
            render status: NOT_FOUND
        }
    }

    def delete() {
        def inst = Institution.get(params.id)
        if (inst) {
            institutionResourceService.delete(inst)
            render status: NO_CONTENT
        } else {
            render status: NOT_FOUND
        }
    }

    private bindWithParams(inst) {
        bindData(inst, params, [include: ['name', 'email', 'password', 'address', 'web', 'telephone']])
    }
}
