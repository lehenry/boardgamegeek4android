package com.boardgamegeek.entities

import com.boardgamegeek.provider.BggContract
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

data class PlayPlayerEntity(
        val name: String,
        val username: String,
        val startingPosition: String? = null,
        val color: String? = null,
        val score: String? = null,
        val rating: Double = 0.0,
        val userId: String? = null,
        val isNew: Boolean = false,
        val isWin: Boolean = false,
        val playId: Int = BggContract.INVALID_ID
) {
    private val format = NumberFormat.getInstance()

    val id: String
        get() = if (username.isBlank()) "P|$name" else "U|${username.toLowerCase(Locale.getDefault())}"

    val seat: Int
        get() = startingPosition?.toIntOrNull() ?: SEAT_UNKNOWN

    val numericScore: Double?
        get() = if (score == null)
            null
        else
            try {
                format.parse(score)?.toDouble()
            } catch (ex: ParseException) {
                null
            }

    val description: String = if (username.isBlank()) name else "$name ($username)"

    companion object {
        const val SEAT_UNKNOWN = -1
    }
}
