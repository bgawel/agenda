import grails.util.Environment
import agenda.PdtpRandomQueryH2Service

beans = {
    switch(Environment.current) {
        case Environment.TEST:
            pdtpRandomQueryService(PdtpRandomQueryH2Service)
            break
        case Environment.DEVELOPMENT:
            pdtpRandomQueryService(PdtpRandomQueryH2Service)
            break
    }
}
