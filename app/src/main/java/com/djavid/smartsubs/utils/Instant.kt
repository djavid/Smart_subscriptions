package com.djavid.smartsubs.utils

/**
 * <ul>
 * <li>Immutability</li>
 * <li>Simpler storage (long+long VS Calendar)</li>
 * <li>(Hopefully) better API</li>
 * </ul>
 */
const val MILLISECONDS_IN_HOUR = 3_600_000
const val MILLISECONDS_IN_MINUTE = 60_000
const val MILLISECONDS_IN_SECOND = 1_000
const val SECONDS_IN_MINUTE = 60
const val MINUTES_IN_HOUR = 60
const val HOURS_IN_DAY = 24
const val BIGGEST_DIGIT = 9

data class Instant(
    /**
     * Time in milliseconds since the beginning of 1970 (epoch).
     */
    val utcEpochTime: Long,
    /**
     * The offset represents the amount of millis between the the time zone and the UTC time.
     * For example an offset of 3600 indicates the time zone is UTC+1
     * while an offset of -3600 indicates the time zone is UTC-1.
     */
    val timeZoneOffset: Int = 0
) : Comparable<Instant> {

    override fun compareTo(other: Instant) = compareValues(this.utcEpochTime, other.utcEpochTime)

    /**
     * Compare two instants and figure out their relative position on the timeline.
     * The comparison happens on the real timeline, so it's possible that the local time is sooner.
     * For example given {@code this} = 17:00 GMT+6 and {@code other} = 18:00 GMT+9
     * {@code this.isAfter(other)} is {@code true}
     * even though comparing 17:00 to 18:00 suggests otherwise.
     * This is because 17:00 GMT+6 is 11:00 UTC and 18:00 GMT+9 is 9:00 UTC.
     */
    fun isAfter(other: Instant) = this.compareTo(other) > 0

    /**
     * Compare two instants and figure out their relative position on the timeline.
     * The comparison happens on the real timeline, so it's possible that the local time is sooner.
     * For example given {@code this} = 18:00 GMT+9 and {@code other} = 17:00 GMT+6
     * {@code this.isBefore(other)} is {@code true}
     * even though comparing 18:00 to 17:00 suggests otherwise.
     * This is because 18:00 GMT+9 is 9:00 UTC and 17:00 GMT+6 is 11:00 UTC.
     */
    fun isBefore(other: Instant) = this.compareTo(other) < 0

    val hours: Int
        get() = ((utcEpochTime / MILLISECONDS_IN_HOUR) % HOURS_IN_DAY).toInt()

    val minutes: Int
        get() = ((utcEpochTime / MILLISECONDS_IN_MINUTE) % MINUTES_IN_HOUR).toInt()

    val seconds: Int
        get() = ((utcEpochTime / MILLISECONDS_IN_SECOND) % SECONDS_IN_MINUTE).toInt()

    val allHours: Int
        get() = (utcEpochTime / MILLISECONDS_IN_HOUR).toInt()

    val allHoursFloat: Float
        get() = (utcEpochTime / MILLISECONDS_IN_HOUR.toFloat())

    val allMinutes: Int
        get() = (utcEpochTime / MILLISECONDS_IN_MINUTE).toInt()

    val allSeconds: Int
        get() = (utcEpochTime / MILLISECONDS_IN_SECOND).toInt()

    val daytime: String
        get() {
            val buffer = StringBuilder()
            if (hours > 0) buffer.append(if (hours > BIGGEST_DIGIT) "$hours:" else "0$hours:")
            buffer.append(if (minutes > BIGGEST_DIGIT) "$minutes:" else "0$minutes:")
            buffer.append(if (seconds > BIGGEST_DIGIT) "$seconds" else "0$seconds")

            return buffer.toString()
        }

}