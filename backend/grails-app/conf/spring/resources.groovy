import grails.util.Environment
import agenda.EventChangedListener
import agenda.InstitutionChangedListener
import agenda.PdtpRandomQueryH2Service
import agenda.security.RestAdminAuthenticationFilter
import agenda.security.RestAuthenticationTokenJsonRendererImpl
import agenda.security.TokenStorageServiceImpl

beans = {
    switch(Environment.current) {
        case Environment.TEST:
            pdtpRandomQueryService(PdtpRandomQueryH2Service)
            break
        case Environment.DEVELOPMENT:
            pdtpRandomQueryService(PdtpRandomQueryH2Service)
            break
    }

    tokenStorageService(TokenStorageServiceImpl) {
        grailsCacheManager = ref('grailsCacheManager')
    }
    restAuthenticationTokenJsonRenderer(RestAuthenticationTokenJsonRendererImpl)
    restAdminAuthenticationFilter(RestAdminAuthenticationFilter) {
        restAuthenticationFilter = ref('restAuthenticationFilter')
        restTokenValidationFilter = ref('restTokenValidationFilter')
        grailsApplication = ref('grailsApplication')
        userDetailsService = ref('userDetailsService')
    }

    eventChangedListener(EventChangedListener) {
        cacheUpdaterService = ref('cacheUpdaterService')
    }
    institutionChangedListener(InstitutionChangedListener) {
        cacheUpdaterService = ref('cacheUpdaterService')
    }
}
