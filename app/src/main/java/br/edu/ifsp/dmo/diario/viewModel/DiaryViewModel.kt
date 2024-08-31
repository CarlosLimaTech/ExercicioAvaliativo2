package br.edu.ifsp.dmo.diario.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo.diario.model.DiaryEntry
import br.edu.ifsp.dmo.diario.model.DiaryRepository
import br.edu.ifsp.dmo.diario.model.DiaryDatabase
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DiaryRepository
    val allEntries: LiveData<List<DiaryEntry>>

    init {
        val diaryDao = DiaryDatabase.getDatabase(application).diaryEntryDao()
        repository = DiaryRepository(diaryDao)
        allEntries = repository.allEntries
    }

    fun insert(entry: DiaryEntry) = viewModelScope.launch {
        repository.insert(entry)
    }

    fun update(entry: DiaryEntry) = viewModelScope.launch {
        repository.update(entry)
    }

    fun delete(entry: DiaryEntry) = viewModelScope.launch {
        repository.delete(entry)
    }
}
