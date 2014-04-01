package agenda.security

import org.springframework.security.authentication.InsufficientAuthenticationException

class SecurityAttackService {

    static transactional = false

    def springSecurityService

    def assertOwner(sentAsOwner) {
        try {
            if (!(sentAsOwner && isOwnerOrAdmin(sentAsOwner))) {
                throw new InsufficientAuthenticationException("$sentAsOwner is not logged in owner")
            }
        } catch (InsufficientAuthenticationException e) {
            throw e
        } catch (e) {
            throw new InsufficientAuthenticationException('Unexpected exception', e)
        }
    }

    private isOwnerOrAdmin(ownerIdAsString) {
        def principal = springSecurityService.principal
        ((ownerIdAsString as long) == principal.id) || isAdmin(principal)
    }

    private isAdmin(userDetails) {
        userDetails.authorities.any { Role.isAdmin(it.authority) }
    }
}
