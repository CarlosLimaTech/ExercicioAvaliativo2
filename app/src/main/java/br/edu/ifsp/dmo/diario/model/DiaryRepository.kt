package br.edu.ifsp.dmo.diario.model

import androidx.lifecycle.LiveData

class DiaryRepository(private val diaryEntryDao: DiaryEntryDao) {

    val allEntries: LiveData<List<DiaryEntry>> = diaryEntryDao.getAllEntries()

    suspend fun insert(entry: DiaryEntry) {
        diaryEntryDao.insertEntry(entry)
    }

    suspend fun update(entry: DiaryEntry) {
        diaryEntryDao.updateEntry(entry)
    }

    suspend fun delete(entry: DiaryEntry) {
        diaryEntryDao.deleteEntry(entry)
    }
}
