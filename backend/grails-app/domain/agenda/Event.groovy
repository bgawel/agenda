package agenda

import static agenda.LocalContext.dateTime
import static agenda.LocalContext.getCurrentDate
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import org.joda.time.format.DateTimeFormat

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

    boolean oneTimeType = true

    List pdtps = []
    static hasMany = [pdtps: Pdtp]
    static belongsTo = [institution: Institution]

    static final PIC_MAX_SIZE = 64
    static constraints = {
        title maxSize: 64, blank: false
        pic maxSize: PIC_MAX_SIZE, nullable: true
        more maxSize: 64, nullable: true, validator: {
            if (!WwwValidator.validate(it)) {
                ['incorrectUrl']
            }
        }
        category nullable: false
        description maxSize: 1800, nullable: true, validator: { value, thisEvent ->
            if (!value && !thisEvent.more) {
                ['descriptionOrMoreRequired']
            }
        }
        pdtps minSize: 1, validator: { value, thisEvent ->
            def j, firstPdtp, secondPdtp
            def currentDate = currentDate.toDate()
            def validator = thisEvent.oneTimeType ? new OneTimeTypeDatesOverlapValidator() :
                new TmpTypeDatesOverlapValidator()
            for (int i = 0; i < value.size(); ++i) {
                firstPdtp = value[i]
                if (firstPdtp.toDate) {
                    if (firstPdtp.toDate < currentDate)  {
                        if (!firstPdtp.id || firstPdtp.isDirty()) {
                            thisEvent.errors.rejectValue("pdtps[$i].toDate", 'pdtp.toDate.toDateBeforeNow')
                        }
                    } else {
                        for (j = i + 1; j < value.size(); ++j) {
                            secondPdtp = value[j]
                            if (firstPdtp.fromDate && secondPdtp.fromDate && secondPdtp.toDate &&
                                validator.validate(firstPdtp, secondPdtp)) {
                                validator.rejectValue(firstPdtp, secondPdtp, thisEvent)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    static mapping = {
        version true
        pdtps cascade: 'all-delete-orphan'
    }

    def beforeInsert() {
        stampOwner()
    }

    def beforeUpdate() {
        stampOwner()
    }

    def beforeDelete() {
        stampOwner()
    }

    protected stampOwner() {
        institution.lastUpdated = new Date()
    }

    private static class OneTimeTypeDatesOverlapValidator {

        private static final dateFormatter = DateTimeFormat.forPattern(/dd'-'MM'-'yyyy/)
        private static final timeFormatter = DateTimeFormat.forPattern(/HH:mm/)

        def validate(firstPdtp, secondPdtp) {
            return firstPdtp.toDate == secondPdtp.toDate && firstPdtp.startTime == secondPdtp.startTime
        }

        def rejectValue(firstPdtp, secondPdtp, event) {
            event.errors.reject('pdtps.sameDateTime',
                (Object[])[dateFormatter.print(dateTime(secondPdtp.toDate)),
                    timeFormatter.print(dateTime(secondPdtp.startTime))].toArray(), 'The same date and time')
        }
    }

    private static class TmpTypeDatesOverlapValidator {

        private static final dateFormatter = DateTimeFormat.forPattern(/dd'-'MM'-'yyyy/)

        def validate(firstPdtp, secondPdtp) {
            return firstPdtp.fromDate <= secondPdtp.toDate && secondPdtp.fromDate <= firstPdtp.toDate
        }

        def rejectValue(firstPdtp, secondPdtp, event) {
            def firstFromDate, firstToDate, secondFromDate, secondToDate
            if (!secondPdtp.id || (secondPdtp.id && firstPdtp.id)) {
                firstFromDate = secondPdtp.fromDate
                firstToDate = secondPdtp.toDate
                secondFromDate = firstPdtp.fromDate
                secondToDate = firstPdtp.toDate
            } else {
                firstFromDate = firstPdtp.fromDate
                firstToDate = firstPdtp.toDate
                secondFromDate = secondPdtp.fromDate
                secondToDate = secondPdtp.toDate
            }
            event.errors.reject('pdtps.datesOverlap', (Object[])[dateFormatter.print(dateTime(firstFromDate)),
                dateFormatter.print(dateTime(firstToDate)), dateFormatter.print(dateTime(secondFromDate)),
                dateFormatter.print(dateTime(secondToDate))].toArray(), 'Two dates overlap')
        }
    }
}
