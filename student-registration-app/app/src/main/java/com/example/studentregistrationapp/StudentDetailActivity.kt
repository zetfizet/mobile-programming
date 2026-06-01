package com.example.studentregistration.ui.screens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studentregistration.databinding.ActivityStudentDetailBinding
import com.example.studentregistration.ui.viewmodel.StudentViewModel
import com.example.studentregistration.ui.viewmodel.StudentViewModelFactory
import com.example.studentregistration.utils.DateUtils

class StudentDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STUDENT_ID = "extra_student_id"
    }

    private lateinit var binding: ActivityStudentDetailBinding
    private lateinit var viewModel: StudentViewModel
    private var studentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentId = intent.getIntExtra(EXTRA_STUDENT_ID, -1)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Mahasiswa"
        }

        setupViewModel()
        loadStudentData()
    }

    private fun setupViewModel() {
        val factory = StudentViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
    }

    private fun loadStudentData() {
        viewModel.getStudentById(studentId).observe(this) { student ->
            student?.let {
                binding.apply {
                    // Avatar inisial
                    val initials = it.name.split(" ").take(2)
                        .joinToString("") { part -> part.first().uppercase() }
                    tvInitials.text = initials

                    val avatarColor = if (it.gender == "Laki-laki") {
                        getColor(com.example.studentregistration.R.color.avatar_male)
                    } else {
                        getColor(com.example.studentregistration.R.color.avatar_female)
                    }
                    cvAvatar.setCardBackgroundColor(avatarColor)

                    tvDetailName.text = it.name
                    tvDetailProdi.text = it.prodi
                    tvDetailNim.text = it.nim
                    tvDetailEmail.text = it.email
                    tvDetailPhone.text = it.phone
                    tvDetailSemester.text = "Semester ${it.semester}"
                    tvDetailGender.text = it.gender
                    tvDetailAddress.text = it.address
                    tvDetailRegistered.text = DateUtils.formatDate(it.registeredAt)

                    btnEdit.setOnClickListener { _ ->
                        val intent = Intent(this@StudentDetailActivity, AddEditStudentActivity::class.java)
                        intent.putExtra(AddEditStudentActivity.EXTRA_STUDENT_ID, it.id)
                        startActivity(intent)
                    }

                    btnDelete.setOnClickListener { _ ->
                        AlertDialog.Builder(this@StudentDetailActivity)
                            .setTitle("Hapus Mahasiswa")
                            .setMessage("Apakah Anda yakin ingin menghapus data ${it.name}?")
                            .setPositiveButton("Hapus") { _, _ ->
                                viewModel.delete(it)
                                finish()
                            }
                            .setNegativeButton("Batal", null)
                            .show()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}