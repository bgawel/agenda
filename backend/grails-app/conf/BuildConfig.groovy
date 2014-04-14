grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        mavenRepo 'http://repo.spring.io/milestone'
        mavenRepo 'https://oss.sonatype.org/content/repositories/snapshots'
        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
        runtime "mysql:mysql-connector-java:5.1.30"
        // runtime 'org.postgresql:postgresql:9.3-1100-jdbc41'
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.50"

        // plugins for the compile step
        compile ":scaffolding:2.0.1"
        compile ":cache:1.1.1"
        compile ":cache-ehcache:1.0.1"
        compile ":joda-time:1.4"
        compile ":session-temp-files:1.0"
        compile ":spring-security-core:2.0-RC2"
        compile ":spring-security-rest:1.3.0.RC3", { //https://github.com/alvarosanchez/grails-spring-security-rest/issues/14
            excludes: ['spring-security-core', 'cors']
        }
        compile ":email-confirmation:2.0.8"
        compile ":greenmail:1.3.4"
        compile ":spring-events:1.2"
        compile ":cache-headers:1.1.6"
        compile ":console:1.3"
        compile ":grails-melody:1.50.0"
        //compile ":mongodb:1.3.1"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.7"
        //runtime ":hibernate4:4.3.4.2"

        // runtime ":database-migration:1.3.8"
        // runtime ":jquery:1.10.2.2"
        runtime ":resources:1.2.1"
        runtime ":cached-resources:1.0"
        runtime ":cors:1.1.4"
        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0.1"
        //runtime ":cached-resources:1.1"
        //runtime ":yui-minify-resources:0.1.5"

        provided ":codenarc:0.20"
    }
}
