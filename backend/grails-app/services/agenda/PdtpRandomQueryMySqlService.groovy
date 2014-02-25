package agenda

class PdtpRandomQueryMySqlService extends AbstractPdtpRandomQueryHqlService{

    @Override
    protected makeHqlForFindAllRandomNotFinished(dbDate, whereInst, whereCategory, whereExclude, joinEvent) {
        "select distinct o from $Pdtp.name o $joinEvent where o.toDate >= :date $whereInst $whereCategory $whereExclude order by rand()"
    }
}
