package agenda.security

import grails.converters.JSON
import grails.plugin.springsecurity.userdetails.GrailsUser
import groovy.util.logging.Slf4j

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.Assert

import com.odobo.grails.plugin.springsecurity.rest.RestAuthenticationToken
import com.odobo.grails.plugin.springsecurity.rest.token.rendering.RestAuthenticationTokenJsonRenderer

@Slf4j
class RestAuthenticationTokenJsonRendererImpl implements RestAuthenticationTokenJsonRenderer {

    String generateJson(RestAuthenticationToken restAuthenticationToken) {
        Assert.isInstanceOf(GrailsUser, restAuthenticationToken.principal, "A GrailsUser implementation is required")
        UserDetails userDetails = restAuthenticationToken.principal

        def result = [
            username: userDetails.username,
            id: userDetails.id,
            token: restAuthenticationToken.tokenValue]

        def jsonResult = result as JSON
        log.debug "Generated JSON:\n ${jsonResult.toString(true)}"

        jsonResult.toString()
    }
}
