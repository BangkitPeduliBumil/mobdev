package com.bangkit.pedulibumil.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.pedulibumil.history.AppDatabase
import com.bangkit.pedulibumil.history.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyDao = AppDatabase.getDatabase(application).historyDao()

    val allHistory: LiveData<List<HistoryEntity>> = historyDao.getAllHistory()

    fun insertHistory(history: HistoryEntity) {
        viewModelScope.launch {
            historyDao.insertHistory(history)
        }
    }
}
