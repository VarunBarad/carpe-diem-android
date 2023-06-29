package com.varunbarad.carpediem.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.varunbarad.carpediem.android.databinding.ListItemProjectBinding

class ProjectsListAdapter : ListAdapter<Project, ProjectsListAdapter.ViewHolder>(
	Project.DIFF_CALLBACK,
) {
	var editProjectListener: ((Project) -> Unit)? = null
	var deleteProjectListener: ((Project) -> Unit)? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			viewBinding = ListItemProjectBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false,
			),
			editButtonClickListener = this::editProjectClicked,
			deleteButtonClickListener = this::deleteProjectClicked,
		)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	private fun editProjectClicked(project: Project) {
		editProjectListener?.invoke(project)
	}

	private fun deleteProjectClicked(project: Project) {
		deleteProjectListener?.invoke(project)
	}

	class ViewHolder(
		private val viewBinding: ListItemProjectBinding,
		private val editButtonClickListener: (Project) -> Unit,
		private val deleteButtonClickListener: (Project) -> Unit,
	): RecyclerView.ViewHolder(viewBinding.root) {
		fun bind(project: Project) {
			viewBinding.textViewProjectName.text = project.name

			viewBinding.buttonEditProject.setOnClickListener { editButtonClickListener(project) }
			viewBinding.buttonDeleteProject.setOnClickListener { deleteButtonClickListener(project) }
		}
	}
}
