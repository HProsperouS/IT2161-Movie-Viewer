package nyp.sit.movieviewer.intermediate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        val overview = intent.getStringExtra("overview")
        val release_date = intent.getStringExtra("release_date")
        val popularity = intent.getDoubleExtra("popularity",0.0)
        val vote_count = intent.getIntExtra("vote_count",0)
        val vote_average = intent.getDoubleExtra("vote_average",0.0)
        val language = intent.getStringExtra("language")
        val adult = intent.getBooleanExtra("adult",false)
        val video = intent.getBooleanExtra("video",false)
        val poster_path = intent.getStringExtra("poster_path")

        movie_overview.text = "$overview"
        movie_release_date.text = "${release_date}"
        movie_popularity.text = "${popularity}"
        movie_vote_count.text = "${vote_count}"
        movie_vote_avg.text = "${vote_average}"
        movie_language.text = "${language}"
        movie_is_adult.text = "${adult}"
        movie_hasvideo.text = "${video}"

        Picasso.get().load("https://image.tmdb.org/t/p/original/${poster_path}").into(posterIV)

    }

}
