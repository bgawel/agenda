package agenda

import org.springframework.context.ApplicationEvent

class EventChangedEvent extends ApplicationEvent {

    EventChangedEvent(Map source) {
        super(source)
    }
}
