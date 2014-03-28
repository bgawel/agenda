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
        startTime nullable: false
        toDate nullable: false
        fromDate nullable: false, validator: { value, thisPdtp ->
            if (value > thisPdtp.toDate) {
                ['fromGreaterThanTo']
            }
        }
        timeDescription maxSize: 128, nullable: true, validator: { value, thisPdtp ->
            if (thisPdtp.event && !thisPdtp.event.oneTimeType && !value) {
                ['required']
            }
        }
    }

    static mapping = {
        version false
    }
}
