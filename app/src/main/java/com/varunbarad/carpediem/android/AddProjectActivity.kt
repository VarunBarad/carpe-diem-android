package com.varunbarad.carpediem.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.varunbarad.carpediem.android.databinding.ActivityAddProjectBinding

class AddProjectActivity : AppCompatActivity() {
	private lateinit var viewBinding: ActivityAddProjectBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewBinding = ActivityAddProjectBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)

		setSupportActionBar(viewBinding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onStart() {
		super.onStart()

		viewBinding.buttonAdd.setOnClickListener {
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

			// Add to database
			val storageHelper = StorageHelper(this)
			val newProject = storageHelper.addProject(projectName, slot)
			if (newProject != null) {
				Toast.makeText(this, "Project added", Toast.LENGTH_SHORT).show()
				this.finish()
			} else {
				Toast.makeText(this, "Problem adding project", Toast.LENGTH_SHORT).show()
			}
		}
	}
}
