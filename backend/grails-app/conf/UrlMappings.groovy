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

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
