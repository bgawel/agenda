import org.apache.log4j.DailyRollingFileAppender

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}

grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// log4j configuration
log4j = {
    def conversionPattern = '[%d{ISO8601} %5p] %c - %m%n'

    // appender
    appenders {
        'null' name: 'stacktrace'   // switch off logging into stacktrace.log
        console name:'stdout', layout:pattern(conversionPattern: conversionPattern)

        environments {
            development {
                appender new DailyRollingFileAppender(
                    name: 'fileAppender',
                    datePattern: "'.'yyyy-MM-dd",
                    fileName: './logs/agenda.log',
                    layout: pattern(conversionPattern: conversionPattern)
                )
            }
            production {
                appender new DailyRollingFileAppender(
                    name: 'fileAppender',
                    datePattern: "'.'yyyy-MM-dd",
                    fileName: 'logs/agenda.log',
                    layout: pattern(conversionPattern: conversionPattern)
                )
            }
        }
    }

    error   'StackTrace'
    info    'grails.app.jobs'
    info    'agenda.security.RestAdminAuthenticationFilter'

    // loggers
    environments {
        test {
            all     'grails.app'
            debug   'org.hibernate.SQL'
            //trace   'org.hibernate.type'
        }
        development {
            debug  'grails.app.controllers'
            debug  'grails.app.services'
            debug  'grails.app.filters'
            debug  'org.hibernate.SQL'
            debug  'com.odobo.grails.plugin.springsecurity.rest'
            debug  'agenda.security'
            //debug  'org.grails.plugin.cachedresources'
            //debug  'org.grails.plugin.resource'
            //trace   'org.hibernate.type'
        }
        production {
            warn    'grails.app'
        }
    }

    // root section
    environments {
        test {
            root {
                error 'stdout'
            }
        }
        development {
            root {
                warn 'stdout', 'fileAppender'
            }
        }
        production {
            root {
                warn 'fileAppender'
            }
        }
    }
}

grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'agenda.Institution'
grails.plugin.springsecurity.userLookup.usernamePropertyName = 'email'
grails.plugin.springsecurity.userLookup.accountExpiredPropertyName = null
grails.plugin.springsecurity.userLookup.accountLockedPropertyName = null
grails.plugin.springsecurity.userLookup.passwordExpiredPropertyName = null
grails.plugin.springsecurity.userLookup.authority.className = 'agenda.security.Role'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'agenda.security.UserRole'
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.password.bcrypt.logrounds = 13
grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_ADMIN > ROLE_INST
'''
grails.plugin.springsecurity.apf.filterProcessesUrl = '/j_876_security_check'
// if you change username/password parameters, the default logging page won't work
grails.plugin.springsecurity.apf.usernameParameter = 'j_username'
grails.plugin.springsecurity.apf.passwordParameter = 'j_password'
def withoutRestSecurityFilters = 'JOINED_FILTERS,-restTokenValidationFilter,-restAdminAuthenticationFilter,-restAuthenticationFilter,-restLogoutFilter'
grails.plugin.springsecurity.filterChain.chainMap = [
    '/menu/**': withoutRestSecurityFilters,
    '/category/**': withoutRestSecurityFilters,
    '/evntProj/byDate/**': withoutRestSecurityFilters,
    '/evntProj/byEvent/**': withoutRestSecurityFilters,
    '/inst.*': withoutRestSecurityFilters,    // POST without id = sign up
    '/instProj/**': withoutRestSecurityFilters,
    '/config/**': withoutRestSecurityFilters,
    '/pwd/reset.*': withoutRestSecurityFilters,
    '/pwd/set.*': withoutRestSecurityFilters,
    '/confirm/**': withoutRestSecurityFilters,
    '/greenmail/**': withoutRestSecurityFilters,
    '/console/**': withoutRestSecurityFilters,
    '/login/**': withoutRestSecurityFilters,
    '/logout/**': withoutRestSecurityFilters,
    '/grails/**': withoutRestSecurityFilters,
    '/views/**': withoutRestSecurityFilters,
    '/img/**': withoutRestSecurityFilters,
    '/**': 'JOINED_FILTERS'
]
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
    '/grails/**': ['ROLE_ADMIN'],
    '/console/**': ['ROLE_ADMIN']
]

grails.plugin.springsecurity.rest.login.endpointUrl = '/xhr/login.json'
grails.plugin.springsecurity.rest.logout.endpointUrl = '/xhr/logout'
grails.plugin.springsecurity.rest.login.failureStatusCode = 403
grails.plugin.springsecurity.rest.token.validation.headerName = 'X-XSRF-TOKEN'
grails.plugin.springsecurity.rest.token.validation.endpointUrl = '/xhr/checkLogin.json'
grails.plugin.springsecurity.rest.login.useRequestParamsCredentials = false
grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.usernamePropertyName = 'email'
grails.plugin.springsecurity.rest.login.passwordPropertyName = 'password'

grails.resources.mappers.hashandcache.enabled = true
grails.gorm.failOnError = true
grails.app.context = '/'
grails.databinding.dateFormats = ["yyyy-MM-dd'T'HH:mm"]
grails.mail.default.from = 'agenda.wro@gmail.com'

agenda.adminMode = true
agenda.confirm.timeout = 1000*60*60*24*2L // 2 days
agenda.mail.admin = 'agenda.wro@gmail.com'
agenda.confirm.invalid.xhrMode = { render status: 410 }
agenda.confirm.invalid.redirect = [url: '/#/rc/invalid']
agenda.signup.adminconfirmed = [url: '/#/rc/signup/a']
agenda.signup.userconfirmed = [url: '/#/rc/signup/u']
agenda.setpwd.baseUri = { appUrlService -> appUrlService.makeServerUrl() + '/#/rc/setPwd' }
agenda.setpwd.confirmed = { render status: 200 }
environments {
    test {
        grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
    }
    development {
        grails.logging.jul.usebridge = true
        grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
        agenda.image.context = 'images'
        agenda.image.dir = 'web-app/images'
        cors.enabled = false
        if (cors.enabled) {
            def corsAllowOrigin = 'http://127.0.0.1:9000'
            agenda.confirm.invalid.redirect = [url: corsAllowOrigin + '/#/rc/invalid']
            agenda.signup.adminconfirmed = [url: corsAllowOrigin + '/#/rc/signup/a']
            agenda.signup.userconfirmed = [url: corsAllowOrigin + '#/rc/signup/u']
            agenda.setpwd.baseUri = { corsAllowOrigin + '/#/rc/setPwd' }
            cors.headers = [
                'Access-Control-Allow-Origin': corsAllowOrigin,
                'Access-Control-Allow-Headers': 'origin, authorization, accept, content-type, x-requested-with, ' +
                    grails.plugin.springsecurity.rest.token.validation.headerName.toLowerCase()
            ]
        }
    }
    production {
        grails.logging.jul.usebridge = false
        cors.enabled = false
        greenmail.disabled = true
        agenda.signup.usermustconfirm = true
        agenda.image.context = 'img'
        agenda.image.dir = System.getenv('OPENSHIFT_DATA_DIR') + '/images'
        grails {
            mail {
              host = "smtp.gmail.com"
              port = 465
              username = "agenda.wro@gmail.com"
              password = System.getenv('OPENSHIFT_SMTP_PASSWORD')
              props = ["mail.smtp.auth":"true",
                       "mail.smtp.socketFactory.port":"465",
                       "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                       "mail.smtp.socketFactory.fallback":"false"]
            }
         }
         grails.serverURL = 'http://agenda-dadasl.rhcloud.com'
    }
}
