package agenda

import org.springframework.context.ApplicationEvent

class InstitutionChangedEvent extends ApplicationEvent {

    InstitutionChangedEvent(Map source) {
        super(source)
    }
}