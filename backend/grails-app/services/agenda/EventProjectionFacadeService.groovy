package agenda

class EventProjectionFacadeService {

    def eventProjectionService
    def eventRecommendationService

    def showByDate(requestedDate, categoryId, instId) {
        eventProjectionService.showByDate(requestedDate) +
            [newest: eventRecommendationService.getNewlyAdded(categoryId, instId),
             soon: eventRecommendationService.getComingSoon(categoryId, instId)]
    }

    def showByPdtp(pdtpId) {
        eventProjectionService.showByPdtp(pdtpId)
    }

    def submittedEvents(instId) {
       eventProjectionService.submittedEvents(instId)
    }
}
