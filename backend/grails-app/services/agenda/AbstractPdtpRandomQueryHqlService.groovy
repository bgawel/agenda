package agenda

abstract class AbstractPdtpRandomQueryHqlService {

    def findAllNotFinishedRandomly(date, limitTo, categoryId=null, instId=null, notIn=null) {
        def dbDate = date.toDate()
        def params = [date: dbDate]
        def whereInst = instId ? "and e.institution.id = $instId" : ''
        def whereCategory = categoryId ? "and e.category.id = $categoryId" : ''
        def whereExclude
        if (notIn) {
            whereExclude = 'and o.id not in (:excludeIds)'
            params << [excludeIds: notIn]
        } else {
            whereExclude = ''
        }
        def joinEvent = categoryId || instId ? 'inner join o.event e' : ''
        Pdtp.executeQuery(makeHqlForFindAllRandomNotFinished(dbDate, whereInst, whereCategory, whereExclude, joinEvent),
            params, [max: limitTo])
    }

    protected abstract makeHqlForFindAllRandomNotFinished(dbDate, whereInst, whereCategory, whereExclude, joinEvent)
}
