package agenda

class CategoryController {

    static responseFormats = ['json']

    def categoryQueryService

    def all() {
        // valid for 1h; compare with CacheConfig (cache = 'category' valid for 12 hours)
        cache shared:true, validFor:3600
        respond categoryQueryService.all
    }
}
