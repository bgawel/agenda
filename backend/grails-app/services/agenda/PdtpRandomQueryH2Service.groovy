package agenda

class PdtpRandomQueryH2Service extends AbstractPdtpRandomQueryHqlService {

    @Override
    protected makeHqlForFindAllRandomNotFinished(dbDate, whereInst, whereCategory, whereExclude, joinEvent) {
        // just for tests, no distinct because h2 fails
        "select o from $Pdtp.name o $joinEvent where o.toDate >= :date $whereInst $whereCategory $whereExclude order by rand()"
    }
}
