package com.example.studentregistration.ui.screens

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.studentregistration.databinding.ActivityStatisticsBinding
import com.example.studentregistration.ui.viewmodel.StudentViewModel
import com.example.studentregistration.ui.viewmodel.StudentViewModelFactory

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var viewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Statistik Mahasiswa"
        }

        setupViewModel()
        observeData()
    }

    private fun setupViewModel() {
        val factory = StudentViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
    }

    private fun observeData() {
        viewModel.totalStudents.observe(this) { total ->
            binding.tvTotalCount.text = total.toString()
        }

        viewModel.allStudents.observe(this) { students ->
            // Hitung statistik gender
            val maleCount = students.count { it.gender == "Laki-laki" }
            val femaleCount = students.count { it.gender == "Perempuan" }
            binding.tvMaleCount.text = maleCount.toString()
            binding.tvFemaleCount.text = femaleCount.toString()

            // Prodi terbanyak
            val prodiStats = students.groupBy { it.prodi }
                .mapValues { it.value.size }
                .entries
                .sortedByDescending { it.value }

            val sb = StringBuilder()
            prodiStats.forEach { (prodi, count) ->
                sb.appendLine("• $prodi: $count mahasiswa")
            }
            binding.tvProdiStats.text = if (sb.isEmpty()) "Belum ada data" else sb.toString()

            // Semester stats
            val semesterStats = students.groupBy { it.semester }
                .mapValues { it.value.size }
                .entries
                .sortedBy { it.key }

            val semSb = StringBuilder()
            semesterStats.forEach { (semester, count) ->
                semSb.appendLine("• Semester $semester: $count mahasiswa")
            }
            binding.tvSemesterStats.text = if (semSb.isEmpty()) "Belum ada data" else semSb.toString()
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