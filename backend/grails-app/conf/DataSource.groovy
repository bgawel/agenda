dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'grails.plugin.cache.ehcache.hibernate.BeanEhcacheRegionFactory'
//    cache.region.factory_class = 'org.hibernate.cache.impl.NoCachingRegionFactory'
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = 'create-drop'
            dialect = org.hibernate.dialect.MySQL5InnoDBDialect
            driverClassName = 'com.mysql.jdbc.Driver'
            String host = System.getenv('OPENSHIFT_MYSQL_DB_HOST')
            String port = System.getenv('OPENSHIFT_MYSQL_DB_PORT')
            String dbName = System.getenv('OPENSHIFT_APP_NAME')
            url = "jdbc:mysql://$host:$port/$dbName?useUnicode=yes&characterEncoding=UTF-8"
            username = System.getenv('OPENSHIFT_MYSQL_DB_USERNAME')
            password = System.getenv('OPENSHIFT_MYSQL_DB_PASSWORD')
            properties {
                maxActive = 100
                maxIdle = 25
                minIdle = 2
                initialSize = 2
                minEvictableIdleTimeMillis = 60000
                timeBetweenEvictionRunsMillis = 60000
                numTestsPerEvictionRun=3
                maxWait = 10000
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=false
                validationQuery = '*//* ping *//*'
                jdbcInterceptors='ConnectionState'
             }
        }
    }
}
