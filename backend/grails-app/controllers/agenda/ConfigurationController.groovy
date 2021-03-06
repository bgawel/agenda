package agenda

import static agenda.LocalContext.dateTimeToString
import static agenda.LocalContext.getCurrentDateTime

class ConfigurationController {

    static responseFormats = ['json']

    def now() {
        def config = [now: dateTimeToString(currentDateTime)]
        respond config
    }
}
