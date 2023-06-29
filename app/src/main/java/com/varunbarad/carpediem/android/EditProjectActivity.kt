package com.varunbarad.carpediem.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.varunbarad.carpediem.android.databinding.ActivityEditProjectBinding
import java.util.UUID

class EditProjectActivity : AppCompatActivity() {
	companion object {
		private const val EXTRA_PROJECT_ID = "project-id"

		@JvmStatic
		fun start(context: Context, projectId: UUID) {
			context.startActivity(Intent(context, EditProjectActivity::class.java).apply {
				putExtra(EXTRA_PROJECT_ID, projectId.toString())
			})
		}
	}

	private lateinit var viewBinding: ActivityEditProjectBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewBinding = ActivityEditProjectBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)

		setSupportActionBar(viewBinding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		showInitialStoredValues()
	}

	private fun getStoredPassedProject(): Project? {
		val projectId = intent.getStringExtra(EXTRA_PROJECT_ID)?.let { UUID.fromString(it) }
		if (projectId == null) {
			throw IllegalArgumentException("No project-id passed to edit-project screen")
		} else {
			val storageHelper = StorageHelper(this)
			return storageHelper.getProject(projectId)
		}
	}

	private fun showInitialStoredValues() {
		val project = getStoredPassedProject()
		if (project != null) {
			viewBinding.inputNameEditText.setText(project.name)
			when (project.slot) {
				Slot.SLOT_03 -> viewBinding.radioButtonSlot03.isChecked = true
				Slot.SLOT_10 -> viewBinding.radioButtonSlot10.isChecked = true
				Slot.SLOT_30 -> viewBinding.radioButtonSlot30.isChecked = true
			}
		} else {
			throw IllegalArgumentException("No project with passed project-id found")
		}
	}

	override fun onStart() {
		super.onStart()

		viewBinding.buttonSave.setOnClickListener {
			// Check if name is not empty and clean it
			val projectName = viewBinding.inputNameEditText.text?.toString()?.trim()
			if (projectName.isNullOrBlank()) {
				viewBinding.inputNameLayout.error = "Name cannot be empty"
				return@setOnClickListener
			} else {
				viewBinding.inputNameLayout.error = null
			}

			// Check if slot is not empty
			val slot: Slot = when (viewBinding.radioGroup.checkedRadioButtonId) {
				viewBinding.radioButtonSlot03.id -> Slot.SLOT_03
				viewBinding.radioButtonSlot10.id -> Slot.SLOT_10
				viewBinding.radioButtonSlot30.id -> Slot.SLOT_30
				else -> {
					Toast.makeText(this, "Please select a slot", Toast.LENGTH_SHORT).show()
					return@setOnClickListener
				}
			}

			val currentStoredProject = getStoredPassedProject() ?: throw IllegalArgumentException(
				"No project with passed project-id found",
			)

			// Add to database
			val storageHelper = StorageHelper(this)
			val updatedProject = currentStoredProject.copy(
				name = projectName,
				slot = slot,
			)
			val storedUpdatedProject = storageHelper.updateProject(
				projectId = currentStoredProject.id,
				updatedProject = updatedProject,
			)
			if (storedUpdatedProject != null) {
				Toast.makeText(this, "Project updated", Toast.LENGTH_SHORT).show()
				this.finish()
			} else {
				Toast.makeText(this, "Problem updating project", Toast.LENGTH_SHORT).show()
			}
		}
	}

	override fun onStop() {
		super.onStop()

		viewBinding.buttonSave.setOnClickListener(null)
	}
}
