package com.varunbarad.carpediem.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.varunbarad.carpediem.android.databinding.ActivityListProjectsBinding

class ListProjectsActivity : AppCompatActivity() {
	private lateinit var viewBinding: ActivityListProjectsBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewBinding = ActivityListProjectsBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)

		setSupportActionBar(viewBinding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		if (savedInstanceState == null) {
			val initialFragment = supportFragmentManager.findFragmentByTag(
				ListProjects03Fragment.FRAGMENT_TAG,
			) ?: ListProjects03Fragment()

			supportFragmentManager
				.beginTransaction()
				.replace(
					R.id.container_fragment,
					initialFragment,
					ListProjects03Fragment.FRAGMENT_TAG,
				).commitAllowingStateLoss()
		}
	}

	override fun onStart() {
		super.onStart()

		viewBinding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
			when (menuItem.itemId) {
				R.id.slot03 -> {
					val fragment = supportFragmentManager.findFragmentByTag(
						ListProjects03Fragment.FRAGMENT_TAG,
					) ?: ListProjects03Fragment()

					supportFragmentManager
						.beginTransaction()
						.replace(
							R.id.container_fragment,
							fragment,
							ListProjects03Fragment.FRAGMENT_TAG,
						).commitAllowingStateLoss()

					return@setOnItemSelectedListener true
				}

				R.id.slot10 -> {
					val fragment = supportFragmentManager.findFragmentByTag(
						ListProjects10Fragment.FRAGMENT_TAG,
					) ?: ListProjects10Fragment()

					supportFragmentManager
						.beginTransaction()
						.replace(
							R.id.container_fragment,
							fragment,
							ListProjects10Fragment.FRAGMENT_TAG,
						).commitAllowingStateLoss()

					return@setOnItemSelectedListener true
				}

				R.id.slot30 -> {
					val fragment = supportFragmentManager.findFragmentByTag(
						ListProjects30Fragment.FRAGMENT_TAG,
					) ?: ListProjects30Fragment()

					supportFragmentManager
						.beginTransaction()
						.replace(
							R.id.container_fragment,
							fragment,
							ListProjects30Fragment.FRAGMENT_TAG,
						).commitAllowingStateLoss()

					return@setOnItemSelectedListener true
				}
			}

			return@setOnItemSelectedListener false
		}
	}

	override fun onStop() {
		super.onStop()

		viewBinding.bottomNavigationView.setOnItemSelectedListener(null)
	}
}
