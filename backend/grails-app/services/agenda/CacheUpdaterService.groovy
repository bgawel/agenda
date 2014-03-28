package agenda

class CacheUpdaterService {

    static transactional = false

    def grailsCacheManager

    def evictIfSavedEvent(event) {
        grailsCacheManager.getCache('eventByDate').clear()
        grailsCacheManager.getCache('newlyAdded').clear()
        if (log.debugEnabled) log.debug "Evicted cache because event $event.id was saved"
    }

    def evictIfUpdatedEvent(event) {
        grailsCacheManager.getCache('eventByDate').clear()
        grailsCacheManager.getCache('newlyAdded').clear()
        evictEventByPdtpCache(event)
        if (log.debugEnabled) log.debug "Evicted cache because event $event.id was updated"
    }

    def evictIfDeletedEvent(event) {
        grailsCacheManager.getCache('eventByDate').clear()
        grailsCacheManager.getCache('newlyAdded').clear()
        evictEventByPdtpCache(event)
        if (log.debugEnabled) log.debug "Evicted cache because event $event.id was deleted"
    }

    def evictIfUpdatedInst(instId) {
        clearAllRelatedToQueriedEvents()
        if (log.debugEnabled) log.debug "Evicted cache because inst $instId was updated"
    }

    def evictIfDeletedInst(instId) {
        clearAllRelatedToQueriedEvents()
        if (log.debugEnabled) log.debug "Evicted cache because inst $instId was deleted"
    }

    def evictDateDependantCaches() {
        grailsCacheManager.getCache('eventByDate').clear()
        grailsCacheManager.getCache('weekMenu').clear()
        if (log.debugEnabled) log.debug 'Evicted date dependant caches'
    }

    private evictEventByPdtpCache(event) {
        def eventByPdtpCache = grailsCacheManager.getCache('eventByPdtp')
        if (event) {
            event.pdtps.each {
                eventByPdtpCache.evict(it.id)
            }
        } else {
            eventByPdtpCache.clear()
        }
    }

    private clearAllRelatedToQueriedEvents() {
        grailsCacheManager.getCache('eventByDate').clear()
        grailsCacheManager.getCache('newlyAdded').clear()
        evictEventByPdtpCache()
    }
}
