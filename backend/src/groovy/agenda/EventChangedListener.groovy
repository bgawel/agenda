package agenda

import org.springframework.context.ApplicationListener

class EventChangedListener implements ApplicationListener<EventChangedEvent> {

    def cacheUpdaterService

    @Override
    void onApplicationEvent(EventChangedEvent event) {
        def reason = event.source
        if (reason.saved) {
            cacheUpdaterService.evictIfSavedEvent(reason.event)
        } else if (reason.updated) {
            cacheUpdaterService.evictIfUpdatedEvent(reason.event)
        } else if (reason.deleted) {
            cacheUpdaterService.evictIfDeletedEvent(reason.event)
        }
    }
}
