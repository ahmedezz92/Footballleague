package com.example.footballleague.ui.views

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.footballleague.R
import com.example.footballleague.data.model.competitions.Competitions
import com.example.footballleague.databinding.ActivityCompetitionDetailsBinding
import com.example.footballleague.utils.KEY_COMPETITION_DETAILS
import com.example.footballleague.utils.loadSvgUrl
import com.example.footballleague.utils.loadUrl
import kotlinx.android.synthetic.main.activity_competition_details.*

class CompetitionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompetitionDetailsBinding
    private lateinit var competition: Competitions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompetitionDetailsBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(binding.root)
        val bundle = intent.extras
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= 33) {
                competition =
                    bundle.getSerializable(KEY_COMPETITION_DETAILS, Competitions::class.java)!!
            } else
                competition = bundle.getSerializable(KEY_COMPETITION_DETAILS) as Competitions
            setViews(competition)
        }
    }

    private fun setViews(competition: Competitions) {
        tvAreaCode.text = competition.area.code
        tvAreaName.text = competition.area.name
        when {
            competition.area.flag != null -> ivAreaFlag.loadSvgUrl(competition.area.flag)
        }

        tvCompetitionName.text = competition.name
        tvCompetitionType.text = competition.type
        competition.emblem?.let {
            ivCompetitionEmblem.loadUrl(competition.emblem)
        }

        tvStartDate.text =
            String.format(getString(R.string.label_start_date), competition.currentSeason.startDate)
        tvEndDate.text =
            String.format(getString(R.string.label_end_date), competition.currentSeason.endDate)

        tvWinnerTeam.text =
            String.format(
                getString(R.string.label_winner),
                competition.currentSeason.winner?.name
                    ?: getString(R.string.label_not_available)
            )

        tvAvailableSeasons.text = String.format(
            getString(R.string.label_available_seasons),
            competition.numberOfAvailableSeasons
        )

    }

}