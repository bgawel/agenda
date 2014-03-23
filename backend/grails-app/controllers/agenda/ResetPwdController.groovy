package agenda

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

class ResetPwdController {

    static allowedMethods = [change: 'POST']

    def resetPwdConfirmationService

    def index() {
        def inst = Institution.findByEmail(params.id)
        if (inst) {
            resetPwdConfirmationService.sendConfirmation(inst)
            render status: OK
        } else {
            render status: UNPROCESSABLE_ENTITY
        }
    }
}
