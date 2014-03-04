package agenda

class EventPresentationController {

    static responseFormats = ['json']

    def eventPresentationFacadeService

    def byDate() {
        respond eventPresentationFacadeService.showByDate(params.date, params.category, params.inst)
    }

    def byEvent() {
        respond eventPresentationFacadeService.showByPdtp(params.id as long)
    }

    def submitted() {
        respond eventPresentationFacadeService.submittedEvents(params.id as long)
    }
}
