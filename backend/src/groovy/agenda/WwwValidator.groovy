package agenda

import org.apache.commons.validator.UrlValidator

class WwwValidator {

    private static final SCHEMES = ['http', 'https']
    private static final urlValidator = new UrlValidator((String[])SCHEMES.toArray())

    static validate(value) {
        if (value) {
            def checkValue = value
            if (!startWithScheme(checkValue)) {
                checkValue = "${SCHEMES[0]}://$checkValue"
            }
            return urlValidator.isValid(checkValue)
        }
        true
    }

    static startWithScheme(value) {
        if (value) {
            for (int i = 0; i < SCHEMES.size(); ++i) {
                if (value.startsWith(SCHEMES[i])) {
                    return true
                }
            }
        }
        false
    }
}
