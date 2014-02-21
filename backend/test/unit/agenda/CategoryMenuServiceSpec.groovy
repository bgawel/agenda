package agenda

import spock.lang.Specification;

@TestFor(CategoryMenuService)
class CategoryMenuServiceSpec extends Specification {

    def "should get categories menu"() {
        when:
        def categories = service.categories
        
        then:
        categories.entries.size() == 2
        categories.entries[0].id == 'all'
        categories.entries[0].name == 'Wszystko'
        categories.entries[1].id == 1
        categories.entries[1].name == 'cat1'
        categories.activeIndex == 0
    }
    
    def setup() {
        service.categoryQueryService = Mock(CategoryQueryService)
        def category = new Category(name:'cat1')
        category.id = 1 // if passed in constructor, id is null
        1 * service.categoryQueryService.all >> [category]
    }
}
