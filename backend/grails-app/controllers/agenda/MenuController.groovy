package agenda

import static agenda.LocalContext.dateTimeZone
import static agenda.LocalContext.getCurrentDateTime

import org.joda.time.DateTime

class MenuController {

    static responseFormats = ['json']

    def weekMenuService
    def categoryMenuService

    def week() {
        // valid until 00:00:10; compare with EvictCacheJob that starts 23:59:59
        cache shared:true, validUntil:afterMidnight
        respond weekMenuService.week
    }

    def categories() {
        // valid for 1h; compare with CacheConfig (cache = 'categoryMenu' valid for 12 hours)
        cache shared:true, validFor:3600
        respond categoryMenuService.categories
    }

    private getAfterMidnight() {
        def current = currentDateTime
        new DateTime(current.year, current.monthOfYear, current.dayOfMonth, 0, 0, 10, dateTimeZone).plusDays(1).toDate()
    }
}
