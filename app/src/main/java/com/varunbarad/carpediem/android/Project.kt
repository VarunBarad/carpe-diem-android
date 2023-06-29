package com.varunbarad.carpediem.android

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDate
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Project(
	@Json(name = "id") val id: UUID,
	@Json(name = "name") val name: String,
	@Json(name = "slot") val slot: Slot,
	@Json(name = "last_done_on") val lastDoneOn: LocalDate?,
) {
	companion object {
		@JvmStatic
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Project>() {
			override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
				return (oldItem.id == newItem.id)
			}

			override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
				return oldItem == newItem
			}
		}
	}
}
