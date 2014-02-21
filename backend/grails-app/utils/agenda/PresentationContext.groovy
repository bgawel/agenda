package agenda

import static agenda.LocalContext.dateTime
import static agenda.LocalContext.locale

import org.joda.time.format.DateTimeFormat

class PresentationContext {

    static final fullDateFormatter = DateTimeFormat.forPattern(/dd MMMM yyyy/).withLocale(locale)
    static final middleDateFormatter = DateTimeFormat.forPattern(/dd MMM yyyy/).withLocale(locale)
    static final shortDateFormatter = DateTimeFormat.forPattern(/dd MMM/).withLocale(locale)
    static final timeFormatter = DateTimeFormat.forPattern(/HH:mm/).withLocale(locale)

    static printFullJdkDate(jdkDate) {
        fullDateFormatter.print(dateTime(jdkDate))
    }

    static printShortJdkDate(jdkDate) {
        shortDateFormatter.print(dateTime(jdkDate))
    }

    static printMiddleJdkDate(jdkDate) {
        middleDateFormatter.print(dateTime(jdkDate))
    }

    static printJdkTime(jdkTime) {
        timeFormatter.print(dateTime(jdkTime))
    }

    static printShortDayOfWeek(date) {
        date.dayOfWeek().getAsShortText(locale)
    }

    static printShortDayOfWeekForJdkDate(jdkDate) {
        printShortDayOfWeek(dateTime(jdkDate))
    }

    private PresentationContext() { }
}
