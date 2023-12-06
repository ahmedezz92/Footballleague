package com.example.footballleague.data.repos.competitions

import com.example.footballleague.data.api.RetrofitService

class CompetitionsListRepository constructor(private val retrofitService: RetrofitService){
    suspend fun getCompetitions() = retrofitService.getCompetitionsList()
}