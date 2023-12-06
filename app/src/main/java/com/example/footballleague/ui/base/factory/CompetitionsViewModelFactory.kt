package com.example.footballleague.ui.base.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.footballleague.data.repos.competitions.CompetitionsListRepository
import com.example.footballleague.ui.viewmodels.CompetitionsListViewModel

class CompetitionsViewModelFactory constructor(val app: Application, private val repository: CompetitionsListRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CompetitionsListViewModel::class.java)) {
            CompetitionsListViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}