package nyp.sit.movieviewer.basic

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_view_list_of_movies.movielist
import nyp.sit.movieviewer.basic.data.DatabaseAdapter
import nyp.sit.movieviewer.basic.data.SimpleMovieSampleData
import nyp.sit.movieviewer.basic.entity.SimpleMovieItem

class SimpleViewListOfMoviesActivity : AppCompatActivity() {
    private var arrayAdapter: DatabaseAdapter? = null
    private val movie_name:ArrayList<SimpleMovieItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list_of_movies)
        getMovieName()
        arrayAdapter = DatabaseAdapter(this,R.layout.simpleitem,movie_name)
        movielist.adapter = arrayAdapter
        movielist.setOnItemClickListener { parent, view, position, id ->
            val movie:SimpleMovieItem = parent.getItemAtPosition(position) as SimpleMovieItem
            GoMovieDetail(movie)
        }
    }

    private fun GoMovieDetail(movie:SimpleMovieItem){
        val intent = Intent(this, SimpleItemDetailActivity::class.java)
        intent.putExtra("movie_name", movie.title)
        startActivity(intent)
    }

    private fun getMovieName(){
        for (movie in SimpleMovieSampleData.simpleMovieitemArray) {
            val name = movie.title.toString()
            movie_name.add(
                SimpleMovieItem(
                    null,
                    null,
                    null,
                    name
                )
            )
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miFavourite->{
                val intent = Intent(this@SimpleViewListOfMoviesActivity,FavMoviesActivity::class.java)
                startActivity(intent)
            }
            R.id.miLogout->{
                val intent = Intent(this@SimpleViewListOfMoviesActivity,LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
