package agenda

import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import grails.converters.JSON

class CategoryController {

    static allowedMethods = [all: 'GET']

    def categoryQueryService

    def all() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            render categoryQueryService.all as JSON
        }
    }
}
