package agenda.security

import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.util.logging.Slf4j

import com.odobo.grails.plugin.springsecurity.rest.token.storage.TokenNotFoundException
import com.odobo.grails.plugin.springsecurity.rest.token.storage.TokenStorageService

@Slf4j
class TokenStorageServiceImpl implements TokenStorageService {

    def grailsCacheManager

    @Override
    void storeToken(String tokenValue, Object principal) {
        grailsCacheManager.getCache('tokenStorage').put(tokenValue, principal)
        log.debug "Stored principal $principal for token $tokenValue"
    }

    @Override
    @Cacheable(value='tokenStorage', key='#tokenValue')
    Object loadUserByToken(String tokenValue) throws TokenNotFoundException {
        def tokenNotFoundMsg = "Token $tokenValue not found"
        log.debug tokenNotFoundMsg
        throw new TokenNotFoundException(tokenNotFoundMsg)
    }

    @Override
    @CacheEvict(value='tokenStorage', key='#tokenValue')
    void removeToken(String tokenValue) throws TokenNotFoundException {
        log.debug "Removed token $tokenValue"
    }
}
