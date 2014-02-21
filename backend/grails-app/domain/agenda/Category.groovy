package agenda

import groovy.transform.ToString;
import groovy.transform.EqualsAndHashCode;

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode(includes='name')
class Category {

    String name
    
    static constraints = {
        name maxSize: 32, blank: false, unique: true
    }
    
    static mapping = {
        version false
    }
}
