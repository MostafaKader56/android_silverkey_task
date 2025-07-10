package com.silverkey.task.utils

import android.content.Context
import com.silverkey.task.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object RelativeTimeHelper {
    private val now by lazy { Date() }
    private val formatter by lazy { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US) }

    fun getRelativeTime(context: Context, isoDate: String): String {
        return try {
            val pastDate =
                formatter.parse(isoDate) ?: return context.getString(R.string.invalid_date)

            val diffInMillis = now.time - pastDate.time

            val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            val months = days / 30
            val years = days / 365

            fun getString(singular: Int, plural: Int, value: Long): String {
                return context.getString(if (value == 1L) singular else plural, value)
            }

            when {
                seconds < 60 -> getString(
                    R.string.time_ago_second,
                    R.string.time_ago_seconds,
                    seconds
                )

                minutes < 60 -> getString(
                    R.string.time_ago_minute,
                    R.string.time_ago_minutes,
                    minutes
                )

                hours < 24 -> getString(R.string.time_ago_hour, R.string.time_ago_hours, hours)
                days < 30 -> getString(R.string.time_ago_day, R.string.time_ago_days, days)
                days < 365 -> getString(R.string.time_ago_month, R.string.time_ago_months, months)
                else -> getString(R.string.time_ago_year, R.string.time_ago_years, years)
            }

        } catch (e: Exception) {
            context.getString(R.string.invalid_date)
        }
    }
}