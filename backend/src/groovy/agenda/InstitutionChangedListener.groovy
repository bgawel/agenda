package agenda

import org.springframework.context.ApplicationListener

class InstitutionChangedListener implements ApplicationListener<InstitutionChangedEvent> {

    def cacheUpdaterService

    @Override
    void onApplicationEvent(InstitutionChangedEvent event) {
        def reason = event.source
        if (reason.updated) {
            cacheUpdaterService.evictIfUpdatedInst(reason.instId)
        } else if (reason.deleted) {
            cacheUpdaterService.evictIfDeletedInst(reason.instId)
        }
    }
}
