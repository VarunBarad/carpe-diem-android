package com.varunbarad.carpediem.android

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDate
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Project(
	@Json(name = "id") val id: UUID,
	@Json(name = "name") val name: String,
	@Json(name = "slot") val slot: Slot,
	@Json(name = "last_done_on") val lastDoneOn: LocalDate?,
)
