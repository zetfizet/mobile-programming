package com.example.studentregistration.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregistration.data.entity.Student
import com.example.studentregistration.databinding.ItemStudentBinding

class StudentAdapter(
    private val onItemClick: (Student) -> Unit,
    private val onEditClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(
        private val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.apply {
                // Avatar inisial nama
                val initials = student.name.split(" ")
                    .take(2)
                    .joinToString("") { it.first().uppercase() }
                tvInitials.text = initials

                tvStudentName.text = student.name
                tvNim.text = "NIM: ${student.nim}"
                tvProdi.text = student.prodi
                tvSemester.text = "Semester ${student.semester}"
                tvEmail.text = student.email

                // Warna avatar berdasarkan gender
                val avatarColor = if (student.gender == "Laki-laki") {
                    root.context.getColor(com.example.studentregistration.R.color.avatar_male)
                } else {
                    root.context.getColor(com.example.studentregistration.R.color.avatar_female)
                }
                cvAvatar.setCardBackgroundColor(avatarColor)

                root.setOnClickListener { onItemClick(student) }
                btnEdit.setOnClickListener { onEditClick(student) }
                btnDelete.setOnClickListener { onDeleteClick(student) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Student, newItem: Student) = oldItem == newItem
    }
}