package agenda

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import agenda.security.Role
import agenda.security.UserRole

@ToString(includeNames=true, includeFields=true, excludes='password')
@EqualsAndHashCode(includes='email')
class Institution {

    String name
    String email
    String password
    String address
    String web
    String telephone
    boolean enabled = false
    boolean internal = false

    Date dateCreated
    Date lastUpdated

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    static hasMany = [events: Event]

    static constraints = {
        name maxSize: 64, blank: false, unique: true
        email email: true, maxSize: 64, blank: false, unique: true
        password size: 5..60, blank: false
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

    def springSecurityService

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }
}
