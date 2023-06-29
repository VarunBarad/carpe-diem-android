package com.varunbarad.carpediem.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.varunbarad.carpediem.android.databinding.FragmentListProjects03Binding

class ListProjects10Fragment : Fragment() {
	companion object {
		const val FRAGMENT_TAG = "ListProjects10Fragment"
	}

	private lateinit var viewBinding: FragmentListProjects03Binding

	private val projectsListAdapter: ProjectsListAdapter = ProjectsListAdapter()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewBinding = FragmentListProjects03Binding.inflate(inflater, container, false)

		val recyclerViewLayoutManager = LinearLayoutManager(
			viewBinding.recyclerViewProjects.context,
			LinearLayoutManager.VERTICAL,
			false,
		)
		viewBinding.recyclerViewProjects.layoutManager = recyclerViewLayoutManager
		viewBinding.recyclerViewProjects.addItemDecoration(
			DividerItemDecoration(
				viewBinding.recyclerViewProjects.context,
				recyclerViewLayoutManager.orientation,
			),
		)
		viewBinding.recyclerViewProjects.adapter = projectsListAdapter

		return viewBinding.root
	}

	override fun onStart() {
		super.onStart()

		updateListItems()

		projectsListAdapter.editProjectListener = this::editProjectListener
		projectsListAdapter.deleteProjectListener = this::deleteProjectListener
	}

	override fun onStop() {
		super.onStop()

		projectsListAdapter.editProjectListener = null
		projectsListAdapter.deleteProjectListener = null
	}

	private fun updateListItems() {
		val storageHelper = StorageHelper(this.requireContext())
		val projects =
			storageHelper.getAllProjects().filter { it.slot == Slot.SLOT_10 }.sortedBy { it.name }
		projectsListAdapter.submitList(projects)
	}

	private fun editProjectListener(project: Project) {
		EditProjectActivity.start(
			context = this.requireContext(),
			projectId = project.id,
		)
	}

	private fun deleteProjectListener(project: Project) {
		val storageHelper = StorageHelper(this.requireContext())
		storageHelper.deleteProject(projectId = project.id)

		updateListItems()
	}
}
