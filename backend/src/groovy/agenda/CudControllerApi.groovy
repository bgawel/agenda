package agenda

import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class CudControllerApi {

    static doUpdate(Object controller, Class domainClass, Closure cu) {
        Object respondObject
        domainClass.withTransaction {
            respondObject = cu()
        }
        if (respondObject) {
            controller.respond((Object)respondObject, (Map)[status: OK])
        }
    }

    static doDelete(Object controller, domainClass, d) {
        def respondObject
        domainClass.withTransaction {
            d()
        }
        controller.render status: NO_CONTENT
    }
}
