package agenda

import grails.plugin.cache.Cacheable

class CategoryQueryService {

    @Cacheable(value='category', key='#root.methodName')
    def getAll() {
        Category.all
    }
}
