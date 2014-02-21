package agenda

import spock.lang.Specification

@TestFor(CategoryQueryService)
@Mock(Category)
class CategoryQueryServiceSpec extends Specification {

    def "should return all categories"() {
        when:
        def categories = service.all
        
        then:
        categories.size() == 2
        categories[0].id == 1
        categories[0].name == 'cat1'
    }
    
    def setup() {
        new Category(name:'cat1').save()
        new Category(name:'cat2').save()
    }
}
