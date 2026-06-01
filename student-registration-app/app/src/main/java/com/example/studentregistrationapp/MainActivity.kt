package com.example.studentregistration.ui.screens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentregistration.R
import com.example.studentregistration.data.entity.Student
import com.example.studentregistration.databinding.ActivityMainBinding
import com.example.studentregistration.ui.adapter.StudentAdapter
import com.example.studentregistration.ui.viewmodel.StudentViewModel
import com.example.studentregistration.ui.viewmodel.StudentViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        setupViewModel()
        setupRecyclerView()
        setupFab()
        observeData()
    }

    private fun setupViewModel() {
        val factory = StudentViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[StudentViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            onItemClick = { student ->
                val intent = Intent(this, StudentDetailActivity::class.java)
                intent.putExtra(StudentDetailActivity.EXTRA_STUDENT_ID, student.id)
                startActivity(intent)
            },
            onEditClick = { student ->
                val intent = Intent(this, AddEditStudentActivity::class.java)
                intent.putExtra(AddEditStudentActivity.EXTRA_STUDENT_ID, student.id)
                startActivity(intent)
            },
            onDeleteClick = { student ->
                showDeleteConfirmation(student)
            }
        )

        binding.rvStudents.apply {
            this.adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupFab() {
        binding.fabAddStudent.setOnClickListener {
            startActivity(Intent(this, AddEditStudentActivity::class.java))
        }
    }

    private fun observeData() {
        viewModel.displayedStudents.observe(this) { students ->
            adapter.submitList(students)
            updateEmptyState(students.isEmpty())
        }

        viewModel.totalStudents.observe(this) { count ->
            binding.tvTotalStudents.text = "Total: $count Mahasiswa"
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvStudents.visibility = android.view.View.GONE
            binding.layoutEmpty.visibility = android.view.View.VISIBLE
        } else {
            binding.rvStudents.visibility = android.view.View.VISIBLE
            binding.layoutEmpty.visibility = android.view.View.GONE
        }
    }

    private fun showDeleteConfirmation(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Mahasiswa")
            .setMessage("Apakah Anda yakin ingin menghapus data ${student.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.delete(student)
                Snackbar.make(
                    binding.root,
                    "${student.name} berhasil dihapus",
                    Snackbar.LENGTH_LONG
                ).setAction("Urungkan") {
                    viewModel.insert(student)
                }.show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Cari nama, NIM, prodi..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_stats -> {
                startActivity(Intent(this, StatisticsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}