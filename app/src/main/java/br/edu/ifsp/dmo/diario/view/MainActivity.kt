package br.edu.ifsp.dmo.diario.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo.diario.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.diario.viewModel.DiaryViewModel
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var diaryViewModel: DiaryViewModel
    private lateinit var diaryAdapter: DiaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        setupFab()
    }

    private fun setupRecyclerView() {
        diaryAdapter = DiaryAdapter { entry ->
            val intent = Intent(this, AddDiaryEntryActivity::class.java)
            intent.putExtra("diaryEntry", entry as Serializable)
            startActivity(intent)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = diaryAdapter
        }
    }



    private fun observeViewModel() {
        diaryViewModel.allEntries.observe(this) { entries ->
            diaryAdapter.submitList(entries)
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddDiaryEntryActivity::class.java)
            startActivity(intent)
        }
    }
}
