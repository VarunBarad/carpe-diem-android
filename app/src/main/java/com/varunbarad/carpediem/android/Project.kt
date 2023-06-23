package com.varunbarad.carpediem.android

import java.util.UUID

data class Project(
	val id: UUID,
	val name: String,
	val slot: Slot,
)
