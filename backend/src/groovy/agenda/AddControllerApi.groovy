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
        def response = controller.response
        // http://stackoverflow.com/questions/5017454/make-ie-to-cache-resources-but-always-revalidate
        response.setHeader('Cache-Control', 'must-revalidate, private')
        response.setDateHeader('Expires', -1)
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
