package agenda.security

import static org.springframework.http.HttpStatus.FORBIDDEN

class SecurityAttackController {

    def springSecurityService

    def index() {
        def principal = springSecurityService.principal
        log.error "Security attack from ${request.remoteAddr} against " +
            "$principal.username (id=${principal?.id}): uri = $request.forwardURI, params = $params", request.exception
        render status: FORBIDDEN
    }
}
