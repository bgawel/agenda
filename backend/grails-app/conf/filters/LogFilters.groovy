package filters

import java.util.concurrent.atomic.AtomicLong

/**
 * Copied from @see http://grails.org/doc/2.1.0/guide/single.html#filterTypes
 * and changed slightly.
 */
class LogFilters {

    private static final AtomicLong REQUEST_NUMBER_COUNTER = new AtomicLong()
    private static final String START_TIME_ATTRIBUTE = 'Controller__START_TIME__'
    private static final String REQUEST_NUMBER_ATTRIBUTE = 'Controller__REQUEST_NUMBER__'

   def filters = {

      logFilter(controller: '*', action: '*') {

         before = {
            if (!log.debugEnabled) {
                return true
            }

            long start = System.currentTimeMillis()
            long currentRequestNumber = REQUEST_NUMBER_COUNTER.incrementAndGet()

            request[START_TIME_ATTRIBUTE] = start
            request[REQUEST_NUMBER_ATTRIBUTE] = currentRequestNumber

            log.debug "Received request #$currentRequestNumber: " +
               "$request.forwardURI from $request.remoteHost ($request.remoteAddr), " +
               "controller: $controllerName, action: $actionName, params: ${new TreeMap(params)}, " +
               "Ajax: $request.xhr"
         }

         after = { Map model ->
            if (!log.debugEnabled) {
                return true
            }

            long start = request[START_TIME_ATTRIBUTE]
            long end = System.currentTimeMillis()
            long requestNumber = request[REQUEST_NUMBER_ATTRIBUTE]

            def msg = "Generated model for request #$requestNumber: " +
                      "controller total time ${end - start}ms"
            if (log.traceEnabled) {
                log.trace msg + "; model: $model"
            } else {
                log.debug msg
            }
         }

         afterView = { Exception e ->

            if (!log.debugEnabled) {
                return true
            }

            long start = request[START_TIME_ATTRIBUTE]
            long end = System.currentTimeMillis()
            long requestNumber = request[REQUEST_NUMBER_ATTRIBUTE]

            log.debug "Processed view for request #$requestNumber: " +
                      "total time ${end - start}ms"
         }
      }
   }
}
