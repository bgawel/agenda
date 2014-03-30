package agenda

class PdtpQueryService {

    def pdtpRandomQueryService

    def findAllNotFinishedFrom(date) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            ge('toDate', dbDate)
            order('event.id', 'asc')
            order('fromDate', 'asc')
            order('startTime', 'asc')
        }
    }

    /**
     * @param time TODO bgawel: time ignored for now
     */
    def findAllNotFinishedFrom(date, time) {
        findAllNotFinishedFrom(date)
    }

    /**
     * @param time TODO bgawel: time ignored for now
     */
    def findAllNotFinishedForEventFrom(eventId, date, time, pdtpIdToEscape=null) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            eq('event.id', eventId)
            if (pdtpIdToEscape) {
                ne('id', pdtpIdToEscape)
            }
            ge('toDate', dbDate)
            order('fromDate', 'asc')
            order('startTime', 'asc')
        }
    }

    def findAllFor(date) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            le('fromDate', dbDate)
            ge('toDate', dbDate)
            order('startTime', 'asc')
        }
    }

    /**
     * @param time TODO bgawel: time ignored for now
     */
    def findAllNotFinishedFor(date, time) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            le('fromDate', dbDate)
            ge('toDate', dbDate)
            order('startTime', 'asc')
        }
    }

    def findAllNewestFrom(date, limitTo, categoryId=null, instId=null) {
        def dbDate = date.toDate()
        Pdtp.withCriteria {
            ge('toDate', dbDate)
            if (instId) {
                event {
                    eq('institution.id', instId)
                }
            }
            if (categoryId) {
                event {
                    eq('category.id', categoryId)
                }
            }
            order('dateCreated', 'desc')
            maxResults(limitTo)
        }
    }

    def findAllNotFinishedRandomly(date, limitTo, categoryId=null, instId=null, notIn=null) {
        pdtpRandomQueryService.findAllNotFinishedRandomly(date, limitTo, categoryId, instId, notIn)
    }
}
