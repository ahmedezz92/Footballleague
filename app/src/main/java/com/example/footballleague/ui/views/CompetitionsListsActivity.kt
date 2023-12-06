package com.example.footballleague.ui.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.footballleague.utils.network.NetworkHelper
import com.example.footballleague.data.api.RetrofitService
import com.example.footballleague.data.model.competitions.Competitions
import com.example.footballleague.data.repos.competitions.CompetitionsListRepository
import com.example.footballleague.databinding.ActivityCompetitionsListBinding
import com.example.footballleague.ui.adapters.CompetitionAdapter
import com.example.footballleague.ui.adapters.OnCompetitionClickListener
import com.example.footballleague.ui.base.factory.CompetitionsViewModelFactory
import com.example.footballleague.ui.viewmodels.CompetitionsListViewModel
import com.example.footballleague.utils.KEY_COMPETITION_DETAILS
import com.example.footballleague.utils.KEY_SHARED_PREFERENCES_NAME
import com.example.footballleague.utils.app.AppController
import com.example.footballleague.utils.resource.Resource
import kotlinx.android.synthetic.main.activity_competitions_list.*
import org.robolectric.RuntimeEnvironment

class CompetitionsListsActivity : AppCompatActivity(), OnCompetitionClickListener {
    lateinit var binding: ActivityCompetitionsListBinding
    lateinit var competitionsListViewModel: CompetitionsListViewModel
    lateinit var adapter: CompetitionAdapter
    private lateinit var competitionsList: MutableList<Competitions>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompetitionsListBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(binding.root)
        initConnection()
        initSharedPrefs()
        initViews()
    }

    private fun initSharedPrefs() {
        // create master key
        val masterKey = MasterKey.Builder(
            this,
            MasterKey.DEFAULT_MASTER_KEY_ALIAS
        )
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        // Initialize/open an instance of EncryptedSharedPreferences on below line.
        sharedPreferences =
            EncryptedSharedPreferences.create(
                this,
                KEY_SHARED_PREFERENCES_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

    }

    private fun initViews() {
        if (NetworkHelper.hasInternetConnection(application as AppController))
            competitionsListViewModel.getCompetitions()
        else
            competitionsListViewModel.getCompetitionsFromCache(sharedPreferences)
        subscriberGetCompetitions()
    }

    private fun subscriberGetCompetitions() {
        competitionsList = emptyList<Competitions>().toMutableList()
        competitionsListViewModel.competitionsResponse.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.EmptyData -> {
                        progressBar.visibility = View.GONE

                    }
                    is Resource.Success, is Resource.CachedData -> {
                        progressBar.visibility = View.GONE
                        competitionsList = response.data!!.competitions.toMutableList()
                        if (competitionsList.isNotEmpty()) {
                            adapter =
                                CompetitionAdapter(competitionsList, this@CompetitionsListsActivity)
                            rvCompetitions.adapter = adapter
                            competitionsListViewModel.cacheCompetitions(
                                sharedPreferences,
                                response.data
                            )
                        }
                    }
                    is Resource.Error -> {
                        progressBar.visibility = View.GONE
                    }
                }
            }

        })
    }


    private fun initConnection() {
        val retrofitService = RetrofitService.getInstance()
        val repository = CompetitionsListRepository(retrofitService)
        competitionsListViewModel =
            ViewModelProvider(this, CompetitionsViewModelFactory(application, repository)).get(
                CompetitionsListViewModel::class.java
            )
    }

    override fun onCompetitionClick(competition: Competitions) {
        val intent = Intent(this, CompetitionDetailsActivity::class.java)
        intent.putExtra(KEY_COMPETITION_DETAILS, competition)
        startActivity(intent)
    }
}