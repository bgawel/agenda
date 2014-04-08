package agenda

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import grails.plugin.springsecurity.annotation.Secured
import grails.util.Mixin

import javax.annotation.PostConstruct

import com.grailsrocks.emailconfirmation.EmailConfirmationController

@Mixin(AddControllerApi)
class PasswordController {

    static responseFormats = ['json']

    static allowedMethods = [change: 'POST', reset: 'POST', set: 'POST']

    def changePasswordService
    def resetPwdConfirmationService
    def emailConfirmationService
    def responseBuilderService
    private emailConfirmationController

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def change() {
        def payload = getJsonBody(request)
        if (changePasswordService.isPasswordValid(payload.pwd)) {
            render status: OK
        } else {
            respond responseBuilderService.makeGlobalMsg('password.changePwd.badPassword'),
                [status: UNPROCESSABLE_ENTITY]
        }
    }

    def reset() {
        def payload = getJsonBody(request)
        def inst = Institution.findByEmail(payload.username)
        if (inst) {
            resetPwdConfirmationService.sendConfirmation(inst)
            render status: OK
        } else {
            respond responseBuilderService.makeGlobalMsg('password.resetPwd.badUsername'),
                [status: UNPROCESSABLE_ENTITY]
        }
    }

    def set() {
        def payload = getJsonBody(request)
        params.id = payload.token
        params.password = payload.pwd
        params.xhrMode = true
        emailConfirmationController.index()
    }

    @PostConstruct
    void init() {
        emailConfirmationController = new EmailConfirmationController(emailConfirmationService: emailConfirmationService)
        emailConfirmationController.index.delegate = this
        emailConfirmationController.index.resolveStrategy = Closure.DELEGATE_FIRST
    }
}
