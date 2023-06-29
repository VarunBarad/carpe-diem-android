package com.varunbarad.carpediem.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

	private fun rollProject(slot: Slot) {
		val slotString = when (slot) {
			Slot.SLOT_03 -> "03 mins"
			Slot.SLOT_10 -> "10 mins"
			Slot.SLOT_30 -> "30 mins"
		}
		viewBinding.textViewCurrentSlot.text = slotString

		val storageHelper = StorageHelper(this)
		val project = storageHelper.getAllProjects().filter { it.slot == slot }.randomOrNull()
		if (project != null) {
			viewBinding.textViewRolledProject.text = project.name
			viewBinding.textViewCurrentSlot.text = slotString
		} else {
			Toast.makeText(this, "No project in slot $slotString", Toast.LENGTH_SHORT).show()
		}
	}
}
