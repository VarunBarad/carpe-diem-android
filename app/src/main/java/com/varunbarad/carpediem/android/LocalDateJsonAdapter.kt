package com.varunbarad.carpediem.android

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDate
import java.util.UUID

class LocalDateJsonAdapter {
	@ToJson
	fun toJson(date: LocalDate): String {
		return date.toString()
	}

	@FromJson
	fun fromJson(dateString: String): LocalDate {
		return LocalDate.parse(dateString)
	}
}

class UUIDJsonAdapter {
	@ToJson
	fun toJson(uuid: UUID): String {
		return uuid.toString()
	}

	@FromJson
	fun fromJson(uuidString: String): UUID {
		return UUID.fromString(uuidString)
	}
}
