package nyp.sit.movieviewer.basic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import nyp.sit.movieviewer.basic.data.DatabaseAdapter
import nyp.sit.movieviewer.basic.data.SimpleMovieDbHelper
import nyp.sit.movieviewer.basic.entity.SimpleMovieItem
import kotlinx.android.synthetic.main.activity_fav_movies.*

class FavMoviesActivity : AppCompatActivity() {
    private var arrayAdapter:DatabaseAdapter? = null
    private lateinit var sqliteHelper:SimpleMovieDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_movies)
        sqliteHelper = SimpleMovieDbHelper(this)
        getFavourites()
    }

    private fun getFavourites(){
        val favouriteList = sqliteHelper.getAll()
        arrayAdapter = DatabaseAdapter(this,R.layout.simpleitem,favouriteList)
        favouriteListView.adapter = arrayAdapter
    }
}