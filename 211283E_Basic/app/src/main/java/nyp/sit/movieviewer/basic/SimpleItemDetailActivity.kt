package nyp.sit.movieviewer.basic

import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import nyp.sit.movieviewer.basic.data.DatabaseAdapter
import nyp.sit.movieviewer.basic.entity.SimpleMovieItem
import nyp.sit.movieviewer.basic.data.SimpleMovieSampleData
import kotlinx.android.synthetic.main.simple_activity_item_detail.*
import nyp.sit.movieviewer.basic.data.SimpleMovieDbHelper

class SimpleItemDetailActivity : AppCompatActivity() {
    private lateinit var sqliteHelper: SimpleMovieDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_activity_item_detail)
        sqliteHelper = SimpleMovieDbHelper(this)
        val movie = getMovie()
        movie_title.setText(movie.title)
        movie_overview.setText(movie.overview)
        movie_release_date.setText(movie.release_date)
        movie_langauge.setText(movie.original_langauge)
    }

    private fun getMovie(): SimpleMovieItem {
        val name = intent.getStringExtra("movie_name")
        val movie = retrieveMovieByName(name)
        return movie
    }

    fun retrieveMovieByName(name:String?): SimpleMovieItem {

        var overview = ""
        var release_date = ""
        var original_language = ""

        for (movie in SimpleMovieSampleData.simpleMovieitemArray) {
            if (movie.title == name){
                overview = movie.overview.toString()
                release_date = movie.release_date.toString()
                original_language = movie.original_langauge.toString()
            }
        }

       val movie = SimpleMovieItem(
                        overview = overview,
                        release_date = release_date,
                        original_langauge = original_language,
                        title = name
        )

        return movie
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@SimpleItemDetailActivity, SimpleViewListOfMoviesActivity::class.java)
        startActivity(intent)
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addtofav,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAddFavourite->{
                val name = intent.getStringExtra("movie_name")
                val movie = retrieveMovieByName(name)
                sqliteHelper.insertFav(movie)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
