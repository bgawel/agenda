package agenda

class EventQueryService {

    def findAllByInstitution(instId) {
        Event.withCriteria {
            eq('institution.id', instId)
            order('lastUpdated', 'desc')
        }
    }
}
