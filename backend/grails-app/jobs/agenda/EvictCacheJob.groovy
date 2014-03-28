package agenda

import agenda.LocalContext

class EvictCacheJob {

    static triggers = {
        cron name: 'evictCache', cronExpression: '59 59 23 * * ?',    // 23:59:59
            timeZone: TimeZone.getTimeZone(LocalContext.TIME_ZONE_ID)
    }

    def group = 'EvictCacheJob'

    def cacheUpdaterService

    def execute() {
        if (log.infoEnabled) log.info "$group: starting job to evict date dependent cache..."
        cacheUpdaterService.evictDateDependantCaches()
        if (log.infoEnabled) log.info "$group: done"
    }
}
