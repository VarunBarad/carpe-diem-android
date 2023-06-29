package com.varunbarad.carpediem.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.varunbarad.carpediem.android.databinding.ActivityRollProjectBinding

class RollProjectActivity : AppCompatActivity() {
	private lateinit var viewBinding: ActivityRollProjectBinding

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
	}

	override fun onStop() {
		super.onStop()

		viewBinding.buttonSlot03.setOnClickListener(null)
		viewBinding.buttonSlot10.setOnClickListener(null)
		viewBinding.buttonSlot30.setOnClickListener(null)
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

			else -> super.onOptionsItemSelected(item)
		}
	}

	private fun openAddProjectScreen() {
		val intent = Intent(this, AddProjectActivity::class.java).apply {
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

		val storageHelper = StorageHelper(this)
		val availableProjects = storageHelper.getAllProjects().filter { it.slot == slot }
		when (availableProjects.size) {
			0 -> {
				Toast.makeText(this, "No project in slot $slotString", Toast.LENGTH_SHORT).show()
			}
			1 -> {
				val project = availableProjects.first()
				viewBinding.textViewRolledProject.text = project.name
				viewBinding.textViewCurrentSlot.text = slotString
			}
			else -> {
				val project = availableProjects.random()
				if (project.name != viewBinding.textViewRolledProject.text.toString()) {
					viewBinding.textViewRolledProject.text = project.name
					viewBinding.textViewCurrentSlot.text = slotString
				} else {
					rollProject(slot)
				}
			}
		}
	}
}
