package agenda

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

class LocalContext {

    static final TIME_ZONE_ID = 'Europe/Warsaw'

    static final locale = new Locale('pl', 'PL')
    static final dateFormatter = DateTimeFormat.forPattern(/yyyy-MM-dd/).withLocale(locale)
    static final timeFormatter = DateTimeFormat.forPattern(/HH:mm/).withLocale(locale)
    static final dateTimeFormatter = DateTimeFormat.forPattern(/yyyy-MM-dd'T'HH:mm/).withLocale(locale)
    static final dateTimeZone = DateTimeZone.forID(TIME_ZONE_ID)

    static getCurrentDateTime() {
        dateTime(new Date())
    }

    static getCurrentDate() {
        dateTimeToDateOnly(currentDateTime)
    }

    static dateTime(jdkDate) {
        new DateTime(jdkDate, dateTimeZone)
    }

    static dateToString(date) {
        dateFormatter.print(date)
    }

    static stringToDate(dateAsString) {
        dateFormatter.parseDateTime(dateAsString)
    }

    static stringToDateTime(dateTimeAsString) {
        dateTimeFormatter.parseDateTime(dateTimeAsString)
    }

    static dateTimeToString(dateTime) {
        dateTimeFormatter.print(dateTime)
    }

    static dateTimeToDateOnly(dateTime) {
        new DateTime(dateTime.year, dateTime.monthOfYear, dateTime.dayOfMonth, 15, 0, dateTimeZone)
    }

    static dateTimeToTimeOnly(dateTime) {
        new DateTime(2014, 1, 1, dateTime.hourOfDay, dateTime.minuteOfHour, dateTimeZone)
    }

    static jdkDateTimeToDateOnly(jdkDateTime) {
        dateTimeToDateOnly(dateTime(jdkDateTime))
    }

    static jdkDateTimeToTimeOnly(jdkDateTime) {
        dateTimeToTimeOnly(dateTime(jdkDateTime))
    }

    static jdkDateAndTimeToString(jdkDate, jdkTime) {
        "${jdkDateToString(jdkDate)}T${jdkTimeToString(jdkTime)}"
    }

    static jdkDateToString(jdkDate) {
        dateFormatter.print(new DateTime(jdkDate, dateTimeZone))
    }

    static jdkTimeToString(jdkTime) {
        timeFormatter.print(new DateTime(jdkTime, dateTimeZone))
    }

    static jdkDateTimeToDateTime(jdkDate, jdkTime) {
        def date = dateTime(jdkDate)
        def time = dateTime(jdkTime)
        new DateTime(date.year, date.monthOfYear, date.dayOfMonth, time.hourOfDay, time.minuteOfHour, dateTimeZone)
    }

    private LocalContext() { }
}
