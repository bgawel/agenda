package agenda

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode(includes='name')
class Category {

    String name

    static constraints = {
        name maxSize: 32, blank: false, unique: true
    }

    static mapping = {
        cache true
        version false
        sort 'name'
    }
}
