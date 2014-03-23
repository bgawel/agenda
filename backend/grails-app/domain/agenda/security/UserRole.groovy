package agenda.security

import org.apache.commons.lang.builder.HashCodeBuilder

import agenda.Institution

class UserRole implements Serializable {

    private static final long serialVersionUID = 1

    Institution user
    Role role

    static UserRole get(long userId, long roleId) {
        UserRole.where { user == Institution.load(userId) && role == Role.load(roleId) }.get()
    }

    static UserRole create(Institution user, Role role, boolean flush = false) {
        new UserRole(user: user, role: role).save(flush: flush, insert: true)
    }

    static boolean remove(Institution u, Role r, boolean flush = false) {
        int rowCount = UserRole.where { user == Institution.load(u.id) && role == Role.load(r.id) }.deleteAll()
        rowCount > 0
    }

    static void removeAll(Institution u) {
        UserRole.where { user == Institution.load(u.id) }.deleteAll()
    }

    static void removeAll(Role r) {
        UserRole.where { role == Role.load(r.id) }.deleteAll()
    }

    boolean equals(other) {
        if (!(other instanceof UserRole)) {
            return false
        }
        other.user?.id == user?.id && other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) {
            builder.append(user.id)
        }
        if (role) {
            builder.append(role.id)
        }
        builder.toHashCode()
    }

    static mapping = {
        id composite: ['role', 'user']
        version false
    }
}