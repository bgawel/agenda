package agenda

import static agenda.LocalContext.getCurrentDate
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true, includeFields=true)
@EqualsAndHashCode(includes='id')
class Event {

    String title
    String pic
    String more
    String description
    Category category

    Date dateCreated
    Date lastUpdated

    Boolean oneTimeType

    List pdtps = []
    static hasMany = [pdtps: Pdtp]
    static belongsTo = [institution: Institution]

    static constraints = {
        title maxSize: 64, blank: false
        pic maxSize: 64, nullable: true
        more maxSize: 64, nullable: true
        category nullable: false
        oneTimeType nullable: false
        description maxSize: 1800, nullable: true, validator: { value, thisEvent ->
            if (!value && !thisEvent.more) {
                ['descriptionOrMoreRequired']
            }
        }
        pdtps minSize: 1, validator: { value, thisEvent ->
            def j, firstPdtp, secondPdtp
            def currentDate = currentDate.toDate()
            for (int i = 0; i < value.size(); ++i) {
                firstPdtp = value[i]
                if (!firstPdtp.id && firstPdtp.toDate < currentDate) {
                    thisEvent.errors.rejectValue("pdtps[$i].toDate", 'pdtp.toDate.toDateBeforeNow')
                } else {
                    for (j = i + 1; j < value.size(); ++j) {
                        secondPdtp = value[j]
                        if ((firstPdtp.fromDate <= secondPdtp.toDate) && (secondPdtp.fromDate <= firstPdtp.toDate)) {
                            thisEvent.errors.rejectValue("pdtps[$i].fromDate", 'pdtps.datesOverlap')
                            thisEvent.errors.rejectValue("pdtps[$i].toDate", 'pdtps.datesOverlap')
                            thisEvent.errors.rejectValue("pdtps[$j].fromDate", 'pdtps.datesOverlap')
                            thisEvent.errors.rejectValue("pdtps[$j].toDate", 'pdtps.datesOverlap')
                            break
                        }
                    }
                }
            }
        }
    }

    static mapping = {
        version false
        pdtps cascade: 'all-delete-orphan'
    }
}
