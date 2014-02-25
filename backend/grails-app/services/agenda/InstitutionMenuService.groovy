package agenda

import org.apache.commons.beanutils.BeanComparator

class InstitutionMenuService {

    def allEntryId = 'all'
    def allEntryName = 'Wszyscy'
    def allEntryIndex = 0
    def entryComparator = new BeanComparator('name')

    def categoryMenuService

    def calculateBadgesPerCategory(events) {
        def categoryAllEntry = newCategoryAllEntry()
        def categories = [(categoryAllEntry.id): categoryAllEntry]
        events.each {
            increaseBadges(retrieveCategory(categories, it.catId), it)
            increaseBadges(categoryAllEntry, it)
        }
        makeBadgesPerCategory(categories)
    }

    private retrieveCategory(categories, categoryId) {
        def category = categories[categoryId]
        if (!category) {
            category = newCategoryEntry(categoryId)
            categories[categoryId] = category
        }
        category
    }

    private increaseBadges(category, event) {
        def inst = category.who[event.whoId]
        if (!inst) {
            inst = newInstEntry(event.whoId, event.whoName)
            category.who[event.whoId] = inst
        }
        inst.badge += 1
        category.badge += 1
        category.who[allEntryId].badge += 1
    }

    private makeBadgesPerCategory(categories) {
        def category, badgesPerCategory = []
        categoryMenuService.categories.entries.each {
            category = categories[it.id]
            if (category) {
                badgesPerCategory << [id: category.id, badge: category.badge,
                    who: toSortedByNameListOfInstitutions(category.who)]
            } else {
                badgesPerCategory << [id: it.id, badge: 0, who: [newInstAllEntry()]]
            }
        }
        badgesPerCategory
    }

    private toSortedByNameListOfInstitutions(mapOfInstitutions) {
        def allEntry = mapOfInstitutions.remove(allEntryId)
        def list = mapOfInstitutions.values().toList()
        Collections.sort(list, entryComparator)
        list.add(allEntryIndex, allEntry)
        list
    }

    private newCategoryAllEntry() {
        newCategoryEntry(categoryMenuService.allEntryId)
    }

    private newCategoryEntry(categoryId) {
        [id: categoryId, badge: 0, who: [(allEntryId): newInstAllEntry()]]
    }

    private newInstAllEntry() {
        newInstEntry(allEntryId, allEntryName)
    }

    private newInstEntry(id, name) {
        [id: id, name: name, badge: 0]
    }
}
