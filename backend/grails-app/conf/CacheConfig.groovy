import agenda.Category
import agenda.security.Role

config = {

    defaults {
        eternal false
        overflowToDisk false
        maxElementsOnDisk 0
     }

    cache {
       name 'tokenStorage'
       maxElementsInMemory 100000
       timeToLiveSeconds 3600   // 1 hour
    }

    cache {
        name 'category'
        maxElementsInMemory 1
        timeToLiveSeconds 43200 // 12 hours
    }

    cache {
        name 'categoryMenu'
        maxElementsInMemory 1
        timeToLiveSeconds 43200 // 12 hours
    }

    cache {
        name 'weekMenu'
        maxElementsInMemory 1
        timeToLiveSeconds 86400 // 24 hours, see EvictCacheJob
    }

    domain {
        name Category
        maxElementsInMemory 20
        timeToLiveSeconds 43200 // 12 hours
    }

    domain {
        name Role
        eternal true
        maxElementsInMemory 2
    }

    cache {
        name 'inst'
        maxElementsInMemory 5
        timeToLiveSeconds 86400 // 24 hours
    }

    cache {
        name 'submittedEvents'
        maxElementsInMemory 5
        timeToLiveSeconds 86400 // 24 hours
     }

    cache {
        name 'eventByDate'
        maxElementsInMemory 10
        timeToLiveSeconds 86400 // 24 hours, see EvictCacheJob
    }

    cache {
        name 'eventByPdtp'
        maxElementsInMemory 100
        timeToLiveSeconds 86400 // 24 hours
    }

    cache {
        name 'newlyAdded'
        maxElementsInMemory 10
        timeToLiveSeconds 86400 // 24 hours
    }
}