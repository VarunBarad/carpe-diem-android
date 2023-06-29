package com.varunbarad.carpediem.android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.varunbarad.carpediem.android.databinding.ActivityRollProjectBinding
import org.threeten.bp.LocalDate

class RollProjectActivity : AppCompatActivity() {
	private lateinit var viewBinding: ActivityRollProjectBinding
	private var currentProject: Project? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewBinding = ActivityRollProjectBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)

		setSupportActionBar(viewBinding.toolbar)
	}

	override fun onStart() {
		super.onStart()

		viewBinding.buttonSlot03.setOnClickListener { rollProject(Slot.SLOT_03) }
		viewBinding.buttonSlot10.setOnClickListener { rollProject(Slot.SLOT_10) }
		viewBinding.buttonSlot30.setOnClickListener { rollProject(Slot.SLOT_30) }
		viewBinding.buttonMarkDone.setOnClickListener { markProjectDone() }
	}

	override fun onStop() {
		super.onStop()

		viewBinding.buttonSlot03.setOnClickListener(null)
		viewBinding.buttonSlot10.setOnClickListener(null)
		viewBinding.buttonSlot30.setOnClickListener(null)
		viewBinding.buttonMarkDone.setOnClickListener(null)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.options_roll_project, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.buttonAddProject -> {
				openAddProjectScreen()
				true
			}

			R.id.buttonListProjects -> {
				openListProjectsScreen()
				true
			}

			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun openAddProjectScreen() {
		val intent = Intent(this, AddProjectActivity::class.java).apply {
			putExtra(Intent.EXTRA_REFERRER, RollProjectActivity::class.java.simpleName)
		}
		startActivity(intent)
	}

	private fun openListProjectsScreen() {
		val intent = Intent(this, ListProjectsActivity::class.java).apply {
			putExtra(Intent.EXTRA_REFERRER, RollProjectActivity::class.java.simpleName)
		}
		startActivity(intent)
	}

	private fun rollProject(slot: Slot) {
		val slotString = when (slot) {
			Slot.SLOT_03 -> "03 mins"
			Slot.SLOT_10 -> "10 mins"
			Slot.SLOT_30 -> "30 mins"
		}
		viewBinding.textViewCurrentSlot.text = slotString

		val today = LocalDate.now()
		val storageHelper = StorageHelper(this)
		val availableProjects = storageHelper.getAllProjects().filter {
			val slotMatches = it.slot == slot
			val lastDoneBeforeToday = it.lastDoneOn == null || it.lastDoneOn < today
			slotMatches && lastDoneBeforeToday
		}
		when (availableProjects.size) {
			0 -> {
				Toast.makeText(this, "No project in slot $slotString", Toast.LENGTH_SHORT).show()
			}

			1 -> {
				val project = availableProjects.first()
				viewBinding.textViewRolledProject.text = project.name
				viewBinding.textViewCurrentSlot.text = slotString

				viewBinding.buttonMarkDone.isEnabled = true

				currentProject = project
			}

			else -> {
				val project = availableProjects.random()
				if (project.name != viewBinding.textViewRolledProject.text.toString()) {
					viewBinding.textViewRolledProject.text = project.name
					viewBinding.textViewCurrentSlot.text = slotString

					viewBinding.buttonMarkDone.isEnabled = true

					currentProject = project
				} else {
					rollProject(slot)
				}
			}
		}
	}

	private fun markProjectDone() {
		val project = currentProject
		if (project != null) {
			val updatedProject = project.copy(
				lastDoneOn = LocalDate.now(),
			)

			val storageHelper = StorageHelper(this)
			storageHelper.updateProject(
				projectId = project.id,
				updatedProject = updatedProject,
			)
			viewBinding.textViewRolledProject.text = ""
			viewBinding.textViewCurrentSlot.text = ""
			viewBinding.buttonMarkDone.isEnabled = false
			currentProject = null
		}
	}
}
