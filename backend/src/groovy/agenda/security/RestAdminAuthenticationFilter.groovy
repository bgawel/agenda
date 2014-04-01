package agenda.security

import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils
import groovy.util.logging.Slf4j

import javax.annotation.PostConstruct
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

import com.odobo.grails.plugin.springsecurity.rest.RestAuthenticationToken

@Slf4j
class RestAdminAuthenticationFilter extends GenericFilterBean {

    def restAuthenticationFilter
    def restTokenValidationFilter
    def grailsApplication
    def userDetailsService

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (grailsApplication.config.agenda.adminMode) {
            def actualUri = request.requestURI - request.contextPath
            def tokenValue = request.getHeader(restTokenValidationFilter.headerName)

            if (actualUri == restAuthenticationFilter.endpointUrl && tokenValue && request.method == 'POST') {
                log.debug 'Applying authentication filter to this request'

                def authenticationRequest = new RestAuthenticationToken(tokenValue)
                try {
                    def authenticationResult = restTokenValidationFilter.restAuthenticationProvider
                        .authenticate(authenticationRequest)
                    if (authenticationResult.authenticated && isAdmin(authenticationResult.principal)) {
                        log.debug "Admin ${authenticationResult.username} authenticated"
                        try {
                            def mappedAuthentication = restAuthenticationFilter.credentialsExtractor
                                .extractCredentials(request)
                            def mappedUser = userDetailsService.loadUserByUsername(mappedAuthentication.principal)

                            def authentication = new RestAuthenticationToken(mappedUser,
                                mappedAuthentication.credentials, mappedUser.authorities, tokenValue)
                            SecurityContextHolder.context.setAuthentication(authentication)
                            log.info "Admin ${authenticationResult.username} authenticated as ${mappedUser.username}"
                            restAuthenticationFilter.authenticationSuccessHandler
                                .onAuthenticationSuccess(request, response, authentication)
                        } catch (e) {
                            log.debug "Exception having admin ${authenticationResult.username} authenticated", e
                            restAuthenticationFilter.authenticationFailureHandler
                                .onAuthenticationFailure(request, response, e)
                        }
                        return
                    }
                } catch (e) {
                    log.debug 'Potential admin not authenticated', e
                }
            }
        }
        chain.doFilter(request, response)
    }

    private isAdmin(userDetails) {
        userDetails.authorities.any { Role.isAdmin(it.authority) }
    }

    @PostConstruct
    void init() {
        SpringSecurityUtils.registerFilter 'restAdminAuthenticationFilter',
            SecurityFilterPosition.FORM_LOGIN_FILTER.order - 1
    }
}
