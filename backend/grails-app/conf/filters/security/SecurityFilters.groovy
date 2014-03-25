package filters.security

import static org.springframework.http.HttpStatus.FORBIDDEN
import filters.LogFilters

class SecurityFilters {

    def dependsOn = [LogFilters]

    def securityAttackService
    def springSecurityService

    def filters = {

        assertEventProjection(controller: 'eventProjection', action: 'submitted') {
            before = {
                assertOwner { params.id }
            }
        }

        assertInstitution(controller: 'institution', actionExclude: 'save') {
            before = {
                assertOwner { params.id }
            }
        }
    }

    private assertOwner(callable) {
        try {
            securityAttackService.assertOwner(callable())
        } catch (e) {
            def principal = springSecurityService.principal
            callable.delegate.with {
                log.error "Security attack from ${request.remoteAddr} against " +
                    "$principal.username (id=${principal?.id}): uri = $request.forwardURI, " +
                    "controller = $controllerName, action = $actionName, params = $params", e
                render status: FORBIDDEN
            }
            return false
        }
    }
}
