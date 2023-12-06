package com.example.footballleague.data.model.competitions

import java.io.Serializable

data class CompetitionsListResponse(
    val count: Int,
    val competitions: List<Competitions>
)


data class Competitions(
    val id: Int,
    val name: String,
    val plan: String,
    val code:String,
    val numberOfAvailableSeasons: Int,
    val lastUpdated: String,
    val area: Area,
    val currentSeason: CurrentSeason,
    val emblem: String?,
    val type: String?
) : Serializable

data class Area(
    val id: Int,
    val name: String,
    val code: String,
    val flag: String?
) : Serializable

data class CurrentSeason(
    val id: Int,
    val startDate: String,
    val endDate: String,
    val currentMatchday: String?,
    val winner: Winner?
) : Serializable

data class Winner(
    val id: Int,
    val name: String,
    val shortName: String,
    val tla: String,
    val crest: String,
    val address: String,
    val website: String,
    val founded: String,
    val clubColors: String,
    val venue: String,
    val lastUpdated: String
) : Serializable