package agenda

import groovy.transform.ToString;
import groovy.transform.EqualsAndHashCode;

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode(includes='id')
class Pdtp {

    String place
    String price
    Date fromDate
    Date toDate
    Date time
    String timeDescription
    
    static belongsTo = [event: Event]
    
    static constraints = {
        place maxSize: 64, blank: false
        price maxSize: 64, blank: false
        fromDate nullable: false
        toDate nullable: false
        time nullable: false
        timeDescription maxSize: 128, nullable: true
    }
    
    static mapping = {
        version false
    }
}
