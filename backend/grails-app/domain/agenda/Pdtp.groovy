package agenda

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode(includes='id')
class Pdtp {

    String place
    String price
    Date fromDate
    Date toDate
    Date startTime
    String timeDescription

    Date dateCreated

    static belongsTo = [event: Event]

    static constraints = {
        place maxSize: 64, blank: false
        price maxSize: 64, blank: false
        fromDate nullable: false
        toDate nullable: false
        startTime nullable: false
        timeDescription maxSize: 128, nullable: true
    }

    static mapping = {
        version false
    }
}
