package agenda.security

class Role {

    static final ADMIN = 'ROLE_ADMIN'

    String authority

    static isAdmin(authority) {
        authority == ADMIN
    }

    static createAdmin() {
        new Role(authority: ADMIN).save()
    }

    static mapping = {
        cache usage: 'read-only'
    }

    static constraints = {
        authority blank: false, maxSize: 16, unique: true
    }
}
