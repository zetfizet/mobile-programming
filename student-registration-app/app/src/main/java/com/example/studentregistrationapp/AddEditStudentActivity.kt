package com.example.studentregistration.ui.screens

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studentregistration.R
import com.example.studentregistration.data.entity.Student
import com.example.studentregistration.databinding.ActivityAddEditStudentBinding
import com.example.studentregistration.ui.viewmodel.StudentViewModel
import com.example.studentregistration.ui.viewmodel.StudentViewModelFactory
import com.example.studentregistration.utils.ValidationUtils
import com.google.android.material.snackbar.Snackbar

class AddEditStudentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STUDENT_ID = "extra_student_id"
        private const val NO_ID = -1
    }

    private lateinit var binding: ActivityAddEditStudentBinding
    private lateinit var viewModel: StudentViewModel
    private var studentId: Int = NO_ID
    private var existingStudent: Student? = null

    private val prodiList = listOf(
        "Teknik Informatika",
        "Sistem Informasi",
        "Ilmu Komputer",
        "Teknik Elektro",
        "Teknik Sipil",
        "Manajemen",
        "Akuntansi",
        "Hukum",
        "Psikologi",
        "Kedokteran"
    )

    private val semesterList = (1..14).map { "Semester $it" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentId = intent.getIntExtra(EXTRA_STUDENT_ID, NO_ID)
        val isEditMode = studentId != NO_ID

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (isEditMode) "Edit Mahasiswa" else "Tambah Mahasiswa"
        }

        setupViewModel()
        setupDropdowns()
        setupObservers()

        if (isEditMode) {
            loadStudentData()
        }

        binding.btnSave.setOnClickListener {
            if (validateForm()) {
                saveStudent()
            }
        }
    }

    private fun setupViewModel() {
        val factory = StudentViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.operationSuccess.observe(this) { success ->
            if (success == true) {
                viewModel.resetOperationStatus()
                finish()
            }
        }
    }

    private fun setupDropdowns() {
        // Prodi dropdown
        val prodiAdapter = ArrayAdapter(this, R.layout.item_dropdown, prodiList)
        binding.actvProdi.setAdapter(prodiAdapter)

        // Semester dropdown
        val semesterAdapter = ArrayAdapter(this, R.layout.item_dropdown, semesterList)
        binding.actvSemester.setAdapter(semesterAdapter)

        // Gender radio button - default Laki-laki
        binding.rbMale.isChecked = true
    }

    private fun loadStudentData() {
        viewModel.getStudentById(studentId).observe(this) { student ->
            student?.let {
                existingStudent = it
                binding.apply {
                    etNim.setText(it.nim)
                    etName.setText(it.name)
                    etEmail.setText(it.email)
                    etPhone.setText(it.phone)
                    actvProdi.setText(it.prodi, false)
                    actvSemester.setText("Semester ${it.semester}", false)
                    etAddress.setText(it.address)
                    if (it.gender == "Laki-laki") rbMale.isChecked = true
                    else rbFemale.isChecked = true
                }
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        binding.apply {
            // NIM
            val nim = etNim.text.toString().trim()
            if (!ValidationUtils.isValidNim(nim)) {
                tilNim.error = "NIM harus 8-12 digit angka"
                isValid = false
            } else tilNim.error = null

            // Nama
            val name = etName.text.toString().trim()
            if (!ValidationUtils.isValidName(name)) {
                tilName.error = "Nama minimal 3 karakter"
                isValid = false
            } else tilName.error = null

            // Email
            val email = etEmail.text.toString().trim()
            if (!ValidationUtils.isValidEmail(email)) {
                tilEmail.error = "Format email tidak valid"
                isValid = false
            } else tilEmail.error = null

            // Phone
            val phone = etPhone.text.toString().trim()
            if (!ValidationUtils.isValidPhone(phone)) {
                tilPhone.error = "No. HP tidak valid (10-13 digit, awali 08 atau +62)"
                isValid = false
            } else tilPhone.error = null

            // Prodi
            if (actvProdi.text.isNullOrBlank()) {
                tilProdi.error = "Pilih program studi"
                isValid = false
            } else tilProdi.error = null

            // Semester
            if (actvSemester.text.isNullOrBlank()) {
                tilSemester.error = "Pilih semester"
                isValid = false
            } else tilSemester.error = null

            // Alamat
            if (etAddress.text.isNullOrBlank()) {
                tilAddress.error = "Alamat tidak boleh kosong"
                isValid = false
            } else tilAddress.error = null
        }
        return isValid
    }

    private fun saveStudent() {
        val nim = binding.etNim.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val prodi = binding.actvProdi.text.toString().trim()
        val semesterText = binding.actvSemester.text.toString()
        val semester = semesterText.replace("Semester ", "").toIntOrNull() ?: 1
        val address = binding.etAddress.text.toString().trim()
        val gender = if (binding.rbMale.isChecked) "Laki-laki" else "Perempuan"

        val student = Student(
            id = existingStudent?.id ?: 0,
            nim = nim,
            name = name,
            email = email,
            phone = phone,
            prodi = prodi,
            semester = semester,
            address = address,
            gender = gender,
            registeredAt = existingStudent?.registeredAt ?: System.currentTimeMillis()
        )

        if (existingStudent != null) {
            viewModel.update(student)
        } else {
            viewModel.insert(student)
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