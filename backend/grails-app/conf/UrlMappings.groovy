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

        "/rest/resetPwd/$id"(controller: 'resetPwd')
        '/rest/resetPwd/change/$id'(controller: 'emailConfirmation', parseRequest:true)

        "/confirm/$id"(controller: 'emailConfirmation')
        "/greenmail/$id?"(controller: 'greenmail')

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
