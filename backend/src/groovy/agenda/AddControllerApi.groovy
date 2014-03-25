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
}
