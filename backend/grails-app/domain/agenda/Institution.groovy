package agenda

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true, excludes='password')
@EqualsAndHashCode(includes='email')
class Institution {

    String name
    String email
    String password
    String address
    String web
    String telephone

    Date dateCreated
    Date lastUpdated

    static hasMany = [events: Event]

    static constraints = {
        name maxSize: 64, blank: false, unique: true
        email email: true, maxSize: 64, blank: false, unique: true
        password size: 5..16, blank: false
        address maxSize: 128, nullable: true
        telephone maxSize: 64, nullable: true
        web maxSize: 64, nullable: true, validator: {
            if (!WwwValidator.validate(it)) {
                ['incorrectUrl']
            }
        }
    }

    static mapping = {
        version false
        events cascade: 'all-delete-orphan'
    }
}
