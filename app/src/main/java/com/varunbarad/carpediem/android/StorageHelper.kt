package com.varunbarad.carpediem.android

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.util.UUID

class StorageHelper(
	private val context: Context,
) {
	companion object {
		private const val CURRENT_VERSION = 1

		private const val KEY_DATABASE_VERSION = "database-version"
		private const val KEY_DATABASE = "database"
	}

	private val sharedPreferences =
		context.getSharedPreferences("carpe-diem-database", Context.MODE_PRIVATE)
	private val moshi = Moshi.Builder()
		.add(LocalDateJsonAdapter())
		.add(UUIDJsonAdapter())
		.build()
	private val projectListAdapter = moshi.adapter<List<Project>>(
		Types.newParameterizedType(
			List::class.java,
			Project::class.java,
		),
	)

	init {
		// Initialise database here
		var databaseVersion: Int = sharedPreferences.getInt(KEY_DATABASE_VERSION, 0)
		while (databaseVersion < CURRENT_VERSION) {
			migrateDatabase(databaseVersion)
			databaseVersion = sharedPreferences.getInt(KEY_DATABASE_VERSION, 0)
		}
	}

	private fun migrateDatabase(fromVersion: Int) {
		when (fromVersion) {
			0 -> {
				// Migrate from version 0 to version 1
				with(sharedPreferences.edit()) {
					putString(KEY_DATABASE, "[]")
					putInt(KEY_DATABASE_VERSION, 1)
					apply()
				}
			}
		}
	}

	fun getAllProjects(): List<Project> {
		val database = sharedPreferences.getString(KEY_DATABASE, "[]")!!
		return projectListAdapter.fromJson(database)!!
	}

	fun addProject(projectName: String, slot: Slot): Project? {
		val existingProjects = getAllProjects()
		val matchingProject = existingProjects.find { it.slot == slot && it.name == projectName }

		if (matchingProject != null) {
			return matchingProject
		} else {
			val newProject = Project(
				id = UUID.randomUUID(),
				name = projectName,
				slot = slot,
				lastDoneOn = null,
			)
			val updatedProjects = existingProjects + newProject
			sharedPreferences.edit()
				.putString(KEY_DATABASE, projectListAdapter.toJson(updatedProjects))
				.apply()

			return getAllProjects().find { it.slot == slot && it.name == projectName }
		}
	}

	fun updateProject(projectId: UUID, updatedProject: Project): Project? {
		val existingProjects = getAllProjects()
		val updatedProjects = existingProjects.map { if (it.id == projectId) updatedProject else it }
		sharedPreferences.edit()
			.putString(KEY_DATABASE, projectListAdapter.toJson(updatedProjects))
			.apply()

		return getAllProjects().find { it.id == projectId }
	}

	fun deleteProject(projectId: UUID) {
		val existingProjects = getAllProjects()
		val updatedProjects = existingProjects.filter { it.id != projectId }
		sharedPreferences.edit()
			.putString(KEY_DATABASE, projectListAdapter.toJson(updatedProjects))
			.apply()
	}

	fun getProject(projectId: UUID): Project? {
		return getAllProjects().find { it.id == projectId }
	}
}
