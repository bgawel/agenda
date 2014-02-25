package agenda

class EventPresentationFacadeService {

    def eventPresentationService
    def eventRecommendationService

    def showByDate(requestedDate, categoryId, instId) {
        eventPresentationService.showByDate(requestedDate) +
            [newest: eventRecommendationService.getNewlyAdded(categoryId, instId),
             soon: eventRecommendationService.getComingSoon(categoryId, instId)]
    }

    def showByPdtp(pdtpId) {
        eventPresentationService.showByPdtp(pdtpId)
    }

    def submittedEvents(instId) {
       eventPresentationService.submittedEvents(instId)
    }
}
