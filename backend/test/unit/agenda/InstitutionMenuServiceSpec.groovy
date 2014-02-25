package agenda

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(InstitutionMenuService)
class InstitutionMenuServiceSpec extends Specification {

    def "should calculate badges per category"() {
        given:
        def events = [
            [catId:2, whoId:10, whoName:'Teatr Polski'],
            [catId:2, whoId:12, whoName:'Teatr Współczesny'],
            [catId:1, whoId:11, whoName:'Galeria Dizajn'],
            [catId:2, whoId:10, whoName:'Teatr Polski'],
            [catId:3, whoId:10, whoName:'Teatr Polski']
        ]

        when:
        def badges = service.calculateBadgesPerCategory(events)

        then:
        badges.size() == 5
        badges[0].id == 'all'
        badges[0].badge == 5
        badges[0].who.size() == 4
        badges[0].who[0].id == 'all'
        badges[0].who[0].name == 'Wszyscy'
        badges[0].who[0].badge == 5
        badges[0].who[1].id == 11
        badges[0].who[1].name == 'Galeria Dizajn'
        badges[0].who[1].badge == 1
        badges[0].who[2].id == 10
        badges[0].who[2].name == 'Teatr Polski'
        badges[0].who[2].badge == 3
        badges[0].who[3].id == 12
        badges[0].who[3].name == 'Teatr Współczesny'
        badges[0].who[3].badge == 1
        badges[1].id == 1
        badges[1].badge == 1
        badges[1].who.size() == 2
        badges[1].who[0].id == 'all'
        badges[1].who[0].name == 'Wszyscy'
        badges[1].who[0].badge == 1
        badges[1].who[1].id == 11
        badges[1].who[1].name == 'Galeria Dizajn'
        badges[1].who[1].badge == 1
        badges[2].id == 2
        badges[2].badge == 3
        badges[2].who.size() == 3
        badges[2].who[0].id == 'all'
        badges[2].who[0].name == 'Wszyscy'
        badges[2].who[0].badge == 3
        badges[2].who[1].id == 10
        badges[2].who[1].name == 'Teatr Polski'
        badges[2].who[1].badge == 2
        badges[2].who[2].id == 12
        badges[2].who[2].name == 'Teatr Współczesny'
        badges[2].who[2].badge == 1
        badges[3].id == 3
        badges[3].badge == 1
        badges[3].who.size() == 2
        badges[3].who[0].id == 'all'
        badges[3].who[0].name == 'Wszyscy'
        badges[3].who[0].badge == 1
        badges[3].who[1].id == 10
        badges[3].who[1].name == 'Teatr Polski'
        badges[3].who[1].badge == 1
        badges[4].id == 4
        badges[4].badge == 0
        badges[4].who.size() == 1
        badges[4].who[0].id == 'all'
        badges[4].who[0].name == 'Wszyscy'
        badges[4].who[0].badge == 0
    }

    def setup() {
        service.categoryMenuService = Mock(CategoryMenuService)
        1 * service.categoryMenuService.allEntryId >> 'all'
        1 * service.categoryMenuService.categories >> [entries: [[id:'all'], [id:1], [id:2], [id:3], [id:4]]]
    }
}
