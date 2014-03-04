package agenda

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(CategoryMenuService)
@Mock(Category)
class CategoryMenuServiceSpec extends Specification {

    def "should get categories menu"() {
        when:
        def categories = service.categories

        then:
        categories.entries.size() == 3
        categories.entries[0].id == 'all'
        categories.entries[0].name == 'Wszystko'
        categories.entries[1].id
        categories.entries[1].name == 'cat1'
        categories.entries[2].id
        categories.entries[2].name == 'cat2'
        categories.activeIndex == 0
    }

    def setup() {
        new Category(name:'cat1').save()
        new Category(name:'cat2').save()
    }
}
