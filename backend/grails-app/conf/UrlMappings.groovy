class UrlMappings {

	static mappings = {
        "/menu/$action(.${format})?"(controller: 'menu')
        "/category/$action(.${format})?"(controller: 'category')
        "/evntPres/byDate/$date(.${format})?"(controller: 'eventPresentation', action: 'byDate')
        "/evntPres/byEvent/$id(.${format})?"(controller: 'eventPresentation', action: 'byEvent')
        "/evntPres/$action/$id?(.${format})?"(controller: 'eventPresentation')

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
