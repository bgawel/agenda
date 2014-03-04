class UrlMappings {

	static mappings = {
        "/menu/$action(.${format})?"(controller: 'menu')
        "/category/$action(.${format})?"(controller: 'category')
        "/evntPres/byDate/$date(.${format})?"(controller: 'eventPresentation', action: 'byDate')
        "/evntPres/byEvent/$id(.${format})?"(controller: 'eventPresentation', action: 'byEvent')
        "/evntPres/submitted/$id(.${format})?"(controller: 'eventPresentation')
        '/inst'(resources: 'institution')
        '/event'(resources: 'event')

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
