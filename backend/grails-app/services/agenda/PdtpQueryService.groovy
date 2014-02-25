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

    def findAllNotFinishedFrom(date, time) {
        // TODO bgawel: time ignored for now
        findAllNotFinishedFrom(date)
    }

    def findAllNotFinishedForEventFrom(eventId, date, time, pdtpIdToEscape=null) {
        def dbDate = date.toDate()
        // TODO bgawel: time ignored for now
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

    def findAllNotFinishedFor(date, time) {
        def dbDate = date.toDate()
        def dbTime = time.toDate()
        Pdtp.withCriteria {
            le('fromDate', dbDate)
            ge('toDate', dbDate)
            ge('startTime', dbTime)
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
