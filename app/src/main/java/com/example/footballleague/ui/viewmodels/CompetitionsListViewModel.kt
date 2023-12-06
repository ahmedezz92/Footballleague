package com.example.footballleague.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.footballleague.data.model.competitions.CompetitionsListResponse
import com.example.footballleague.data.repos.competitions.CompetitionsListRepository
import com.example.footballleague.utils.*
import com.example.footballleague.utils.appcenter.sendEventData
import com.example.footballleague.utils.resource.Event
import com.example.footballleague.utils.resource.Resource
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.robolectric.RuntimeEnvironment.getApplication
import retrofit2.Response

class CompetitionsListViewModel constructor(
    private val competitionsListRepository: CompetitionsListRepository
) :
    ViewModel() {
    private val errorMessage = MutableLiveData<String>()
    val competitionsResponse = MutableLiveData<Event<Resource<CompetitionsListResponse>>>()
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getCompetitions() {
        competitionsResponse.postValue(Event(Resource.Loading()))
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = competitionsListRepository.getCompetitions()
            if (!response.isSuccessful || (response.code() != SUCCESS200 && response.code() != SUCCESS201))
                sendEventData(
                    "AllergiesViewModel",
                    response.raw().request.url.toString(),
                    response.raw().request.method,
                    response.raw().request.isHttps.toString(),
                    response.code().toString(),
                    bodyToString(response.raw().request.body).toString()
                )

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    competitionsResponse.postValue(handleSuccessResponse(response))
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }


    private fun handleSuccessResponse(response: Response<CompetitionsListResponse>): Event<Resource<CompetitionsListResponse>> {
        if (response.isSuccessful) {

            response.body()?.let { resultResponse ->
                if (resultResponse.competitions.isNotEmpty()) {
                    return Event(Resource.Success(resultResponse))
                } else {
                    return Event(Resource.EmptyData())
                }

            }
        }
        return Event(Resource.Error())
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        competitionsResponse.postValue(
            Event(Resource.Error())
        )

    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun cacheCompetitions(
        sharedPreferences: SharedPreferences,
        competitionsListResponse: CompetitionsListResponse
    ) {
        sharedPreferences.edit().putString(KEY_COMPETITION, Gson().toJson(competitionsListResponse))
            .apply()
    }

    private fun retrieveCachedCompetitions(sharedPreferences: SharedPreferences): CompetitionsListResponse? {
        val data = sharedPreferences.getString(KEY_COMPETITION, null) ?: return null
        return Gson().fromJson(data, CompetitionsListResponse::class.java)
    }

    fun getCompetitionsFromCache(sharedPreferences: SharedPreferences) {
        competitionsResponse.postValue(
            Event(
                Resource.CachedData(
                    retrieveCachedCompetitions(
                        sharedPreferences
                    )
                )
            )
        )
    }
}