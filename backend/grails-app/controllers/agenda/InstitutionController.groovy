package agenda

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class InstitutionController {

    static responseFormats = ['json']

    static allowedMethods = [save: 'POST', update: 'PUT']

    def responseBuilderService
    def institutionResourceService

    def show() {
        respond Institution.get(params.id)
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

    private bindWithParams(inst) {
        bindData(inst, params, [include: ['name', 'email', 'password', 'address', 'web', 'telephone']])
    }
}
