package agenda

import grails.transaction.Transactional

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class ChangePasswordService {

    def springSecurityService
    def authenticationManager
    def restReauthenticationService

    def isPasswordValid(currentPwd) {
        try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               springSecurityService.principal?.username, currentPwd))
           return true
        }
        catch (e) {
           log.debug e
           return false
        }
    }

    @Transactional
    def changePassword(newPwd) {
        def principal = springSecurityService.principal
        def inst = Institution.get(principal?.id)
        if (inst) {
            inst.password = newPwd
        } else {
            throw new IllegalStateException("Principal $principal logged in but no corresponding institution")
        }
        restReauthenticationService.reauthenticate(principal?.username, newPwd)
    }
}
