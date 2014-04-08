package agenda

import spock.lang.Specification

class ExplorativeTests extends Specification {

    def toTime() {
        when:
        def s = '/d/'[0..-2]

        then:
        !s
    }
}
