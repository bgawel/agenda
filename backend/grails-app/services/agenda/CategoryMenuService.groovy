package agenda

import static agenda.PresentationContext.locale
import grails.plugin.cache.Cacheable

import javax.annotation.PostConstruct

class CategoryMenuService {

    static transactional = false

    def categoryQueryService

    def allEntryId = 'all'
    def activeIndex = 0
    def messageSource
    private allEntryName

    @Cacheable(value='categoryMenu', key='#root.methodName')
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

    @PostConstruct
    void init() {
        allEntryName = messageSource.getMessage('category.menu.all', null, locale)
    }
}
