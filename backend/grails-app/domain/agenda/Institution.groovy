package agenda

import groovy.transform.ToString;
import groovy.transform.EqualsAndHashCode;

@ToString(includeNames=true, includeFields=true, excludes='password')
@EqualsAndHashCode(includes='email')
class Institution {

    String name
    String email
    String password
    String address
    String web
    String telephone
    String fax
    
    Date dateCreated
    Date lastUpdated
    
    static hasMany = [events: Event]

    static constraints = {
        name maxSize: 128, blank: false, unique: true
        email email: true, maxSize: 64, blank: false, unique: true
        password size: 5..16, blank: false
        address maxSize: 128, nullable: true
        web maxSize: 64, nullable: true
        telephone maxSize: 64, nullable: true
        fax maxSize: 64, nullable: true
    }
        
    static mapping = {
        version false
    }
}
