package agenda

class PdtpQueryService {

    def findAllNotFinishedFrom(date) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            ge('toDate', dbDate)
            order('event.id', 'asc')
            order('fromDate', 'asc')
            order('time', 'asc')
        }
    }
    
    def findAllNotFinishedFrom(date, time) {
        // TODO bgawel: time ignored for now
        findAllNotFinishedFrom(date)
    }
    
    def findAllNotFinishedForEventFrom(eventId, date, time, pdtpIdToEscape=null) {
        def dbDate = date.toDate()
        // TODO bgawel: time ignored for now
        //def dbTime = time.toDate()
        Pdtp.withCriteria {
            eq('event.id', eventId)
            if (pdtpIdToEscape) {
                ne('id', pdtpIdToEscape)
            }
            ge('toDate', dbDate)
            order('fromDate', 'asc')
            order('time', 'asc')
        }
    }
    
    def findAllFor(date) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            le('fromDate', dbDate)
            ge('toDate', dbDate)
            order('time', 'asc')
        }
    }
    
    def findAllNotFinishedFor(date, time) {
        def dbDate = date.toDate()
        def dbTime = time.toDate()
        Pdtp.withCriteria {
            le('fromDate', dbDate)
            ge('toDate', dbDate)
            ge('time', dbTime)
            order('time', 'asc')
        }
    }
}
