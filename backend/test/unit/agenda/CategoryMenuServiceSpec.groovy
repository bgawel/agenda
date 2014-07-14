package agenda

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(CategoryMenuService)
class CategoryMenuServiceSpec extends Specification {

    def "should get categories menu"() {
        when:
        def categories = service.categories

        then:
        categories.entries.size() == 3
        categories.entries[0].id == 'all'
        categories.entries[0].name == 'Wszystko'
        categories.entries[1].name == 'cat1'
        categories.entries[2].name == 'cat2'
        categories.activeIndex == 0
    }

    def setup() {
        service.messageSource = messageSource
        service.categoryQueryService = Mock(CategoryQueryService)
        1 * service.categoryQueryService.all >> [new Category(name:'cat1'), new Category(name:'cat2')]
        service.init()
    }

    def setupSpec() {
        PresentationContext.locale = new Locale('pl')
        messageSource.addMessage('category.menu.all', PresentationContext.locale, 'Wszystko')
    }
}
