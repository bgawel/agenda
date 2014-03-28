package agenda

import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest

import com.google.common.io.CharStreams

/**
 * borrowed from
 * com.odobo.grails.plugin.springsecurity.rest.credentials.AbstractJsonPayloadCredentialsExtractor
 */
class AddControllerApi {

    Object getJsonBody(HttpServletRequest httpServletRequest) {
        try {
            String body = CharStreams.toString(httpServletRequest.reader)
            JsonSlurper slurper = new JsonSlurper()
            slurper.parseText(body)
        } catch (exception) {
            [:]
        }
    }

    def respondWithCacheHeaders(controller, entity) {
        controller.withCacheHeaders {
            etag {
                // very important toString(), otherwise line 172 in CacheHeadersService evaluates to true
                "${entity.id}:${entity.lastModified.time}".toString()
            }
            delegate.lastModified {
                entity.lastModified
            }
            generate {
                controller.respond entity
            }
        }
    }
}
