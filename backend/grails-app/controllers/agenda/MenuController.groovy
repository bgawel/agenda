package agenda

import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE
import grails.converters.JSON

class MenuController {

    static allowedMethods = [week: 'GET', categories: 'GET']

    def weekMenuService
    def categoryMenuService

    def week() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            render weekMenuService.week as JSON
        }
    }

    def categories() {
        if (params.format != 'json') {
            render status: UNSUPPORTED_MEDIA_TYPE.value
        } else {
            render categoryMenuService.categories as JSON
        }
    }
}
