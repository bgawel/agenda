package agenda

class CategoryMenuService {

    static transactional = false
    
    def allEntryId = 'all'
    def allEntryName = 'Wszystko'
    def activeIndex = 0
    
    def categoryQueryService
    
    def getCategories() {
        def entries = []
        entries << allEntry
        entries += dbEntries
        [entries: entries, activeIndex: activeIndex]
    }
    
    private getDbEntries() {
        categoryQueryService.all.collect { makeEntry(it.id, it.name) }
    }
    
    private getAllEntry() {
        makeEntry(allEntryId, allEntryName)
    }
    
    private makeEntry(id, name) {
        [id: id, name: name]
    }
}
