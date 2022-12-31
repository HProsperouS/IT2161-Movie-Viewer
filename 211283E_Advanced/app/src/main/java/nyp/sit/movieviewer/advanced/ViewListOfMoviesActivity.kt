package nyp.sit.movieviewer.advanced

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.amazonaws.mobile.client.UserStateDetails
import kotlinx.coroutines.*
import nyp.sit.movieviewer.advanced.entity.MovieItem
import java.lang.Exception

class ViewListOfMoviesActivity : AppCompatActivity() {
    private var currentPosition = 0

    val SHOW_BY_TOP_RATED = 1
    val SHOW_BY_POPULAR = 2

    private var displayType = SHOW_BY_TOP_RATED

    var moviesAdapter : MovieAdapter ?= null
    private var allMovies : List<MovieItem> ?= null

    var appCoroutineScope: CoroutineScope? = null

    private val movieViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MovieApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_list_of_movies)
        appCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        // Initialize AWS Mobile Client
        AWSMobileClient.getInstance().initialize(this,object : Callback<UserStateDetails> {

            override fun onResult(result: UserStateDetails?) {
                Log.d("Cognito", result?.userState?.name.toString())
            }

            override fun onError(e: Exception?) {
                Log.d("Cognito", "There is an error - ${e.toString()}")
            }

        })

        movieViewModel.allMovies.observe(this, Observer {
            val moviesConvertList = mutableListOf<MovieItem>()
            allMovies = it
            for (movie in it)
            {
                moviesConvertList.add(movie)
            }

            it?.let {
                moviesAdapter = MovieAdapter(this,R.layout.card_items_movie, moviesConvertList)
                val listView: ListView = findViewById(R.id.movielist)

                if (savedInstanceState != null) {
                    currentPosition = savedInstanceState.getInt("current_position", 0)
                }

                // Set the position of the ListView to the current position
                listView.setSelection(currentPosition)

                listView.adapter = moviesAdapter
                registerForContextMenu(listView)
                listView.setOnItemClickListener { adapterView, view, i, l ->
                    val movie: MovieItem = adapterView.getItemAtPosition(i) as MovieItem
                    GoMovieDetail(movie)
                }
            }
        })
    }

    private fun GoMovieDetail(movie: MovieItem){
        println(movie)
        val intent = Intent(this@ViewListOfMoviesActivity,ItemDetailActivity::class.java)
        intent.putExtra("overview", movie.overview )
        intent.putExtra("release_date", movie.release_date)
        intent.putExtra("popularity", movie.popularity )
        intent.putExtra("vote_count", movie.vote_count )
        intent.putExtra("vote_average", movie.vote_average )
        intent.putExtra("language", movie.original_language )
        intent.putExtra("adult", movie.adult)
        intent.putExtra("video", movie.video)
        intent.putExtra("poster_path", movie.poster_path)

        intent.putExtra("id", movie.id)
        intent.putExtra("backdrop_path", movie.backdrop_path)
        intent.putExtra("genre_ids", movie.genre_ids)
        intent.putExtra("original_title", movie.original_title)
        intent.putExtra("title", movie.title)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        loadMovieData(displayType)
    }

    fun loadMovieData(viewType: Int) {
        var showTypeStr: String? = null
        when (viewType) {
            SHOW_BY_TOP_RATED -> showTypeStr = NetworkUtils.TOP_RATED_PARAM
            SHOW_BY_POPULAR -> showTypeStr = NetworkUtils.POPULAR_PARAM
        }

        if (showTypeStr != null) {
            displayType = viewType
        }

        val movieJob = CoroutineScope(Job() + Dispatchers.IO).async() {
            val movieRequestUrl = NetworkUtils.buildUrl(showTypeStr!!, getString(R.string.moviedb_api_key))

            try
            {
                val jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl!!)

                val responseList = movieDBJsonUtils.getMovieDetailsFromJson(this@ViewListOfMoviesActivity, jsonMovieResponse!!)

                responseList
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                null
            }
        }

        GlobalScope.launch(Dispatchers.Main){
            val movieList = movieJob.await()
            if(movieList != null){

                for (movie in movieList){
                    movieViewModel.insert(
                        MovieItem(
                            0,
                            movie.poster_path,
                            movie.adult,
                            movie.overview,
                            movie.release_date,
                            movie.genre_ids,
                            movie.original_title,
                            movie.original_language,
                            movie.title,
                            movie.backdrop_path,
                            movie.popularity,
                            movie.vote_count,
                            movie.video,
                            movie.vote_average)
                    )
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortPopular -> {
                movieViewModel.delete()
                loadMovieData(SHOW_BY_POPULAR)
            }
            R.id.sortTopRated -> {
                movieViewModel.delete()
                loadMovieData(SHOW_BY_TOP_RATED)
            }
            R.id.signOut -> {
                runLoginOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val listView: ListView = findViewById(R.id.movielist)
        // Set the position of the ListView to the current position
        listView.setSelection(currentPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val listView: ListView = findViewById(R.id.movielist)
        // Save the current position of the ListView
        currentPosition = listView.firstVisiblePosition
        outState.putInt("current_position", currentPosition)
    }

    // Sign Out from Cognito and redirect the user back to login screen
    private fun runLoginOut() {
        appCoroutineScope?.launch {
            AWSMobileClient.getInstance()
                .signOut(SignOutOptions.builder().signOutGlobally(false).build())
                    object : Callback<Void>{
                        override fun onResult(result: Void?) {
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onError(e: Exception?) {
                            Log.e("SignOutError", "Exception: $e")
                        }
                    }
        }
    }
}

