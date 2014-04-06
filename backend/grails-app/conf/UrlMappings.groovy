import org.springframework.security.authentication.InsufficientAuthenticationException

class UrlMappings {

	static mappings = {
        "/menu/$action(.${format})?"(controller: 'menu')
        "/category/$action(.${format})?"(controller: 'category')
        "/evntProj/byDate/$date(.${format})?"(controller: 'eventProjection', action: 'byDate')
        "/evntProj/byEvent/$id(.${format})?"(controller: 'eventProjection', action: 'byEvent')
        "/evntProj/submitted/$id(.${format})?"(controller: 'eventProjection', action: 'submitted')
        "/instProj/names(.${format})?"(controller: 'institutionProjection')
        '/inst'(resources: 'institution')
        '/event'(resources: 'event')
        "/config/$action(.${format})?"(controller: 'configuration')
        "/upload/$action"(controller: 'fileUpload')
        "/rest/$action(.${format})?"(controller: 'password')

        "/confirm/$id"(controller: 'emailConfirmation')
        "/greenmail/$action?/$id?"(controller: 'greenmail')
        "/console/$action?"(controller: 'console')
        "/login/$action?"(controller: 'login')
        "/logout/$action?"(controller: 'logout')
        "/grails"(view:'/grails')

        "/"(view: '/index')

        "500"(controller: 'securityAttack', exception: InsufficientAuthenticationException)
        "500"(view:'/error')
	}
}
