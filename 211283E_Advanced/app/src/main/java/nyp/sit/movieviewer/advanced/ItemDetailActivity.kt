package nyp.sit.movieviewer.advanced

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.coroutines.*

class ItemDetailActivity : AppCompatActivity() {

    var activityCoroutineScope:CoroutineScope? = null
    var dynamoDBMapper : DynamoDBMapper? = null

     var currentFavMovie: FavoriteMovie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        activityCoroutineScope?.launch() {
            try {
                //Initialize DynamoDBMapper
                val credentials: AWSCredentials = AWSMobileClient.getInstance().credentials
                val dynamoDBClient = AmazonDynamoDBClient(credentials)

                dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(
                        AWSMobileClient.getInstance().configuration
                    ).build()

            } catch (e: Exception) {
                Log.d("DynamoDB", "Exception ${e.message}")
            }
        }

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

    private fun displayToast(message:String){
        Toast.makeText(this@ItemDetailActivity,message, Toast.LENGTH_LONG).show()
    }

    fun runAddFav(movie: FavoriteMovie.MovieItems) {
        activityCoroutineScope?.launch() {
            val eav = HashMap<String, AttributeValue>()
            eav.put(":user", AttributeValue().withS(AWSMobileClient.getInstance().username))

            val queryExpression =
                DynamoDBScanExpression().withFilterExpression("id = :user")
                    .withExpressionAttributeValues(eav)

            val itemList = dynamoDBMapper?.scan(FavoriteMovie::class.java, queryExpression)

            if (itemList?.size != 0 && itemList != null) {
                val existingFavMovies = itemList[0].favMovie!!
                if (!existingFavMovies.any { it.title == movie.title }) {
                    existingFavMovies.add(movie)
                    activityCoroutineScope?.launch {
                        dynamoDBMapper?.save(itemList[0])
                        this@ItemDetailActivity.runOnUiThread(java.lang.Runnable {
                            displayToast("Movie added successfully")
                        })
                    }
                }else{
                    this@ItemDetailActivity.runOnUiThread(java.lang.Runnable {
                        displayToast("Movie Already exist in database")
                    })
                }
            } else {
                currentFavMovie = FavoriteMovie()
                currentFavMovie?.apply {
                    id = AWSMobileClient.getInstance().username
                    favMovie = mutableListOf<FavoriteMovie.MovieItems>()
                }
                currentFavMovie?.favMovie?.add(movie)
                activityCoroutineScope?.launch() {
                    dynamoDBMapper?.save(currentFavMovie)
                    this@ItemDetailActivity.runOnUiThread(java.lang.Runnable {
                        displayToast("Movie added successfully")
                    })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addtofav,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miAddFavourite->{
                val movie = getMovie()
                runAddFav(movie)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMovie(): FavoriteMovie.MovieItems {
        val overview = intent.getStringExtra("overview")
        val release_date = intent.getStringExtra("release_date")
        val popularity = intent.getDoubleExtra("popularity",0.0)
        val vote_count = intent.getIntExtra("vote_count",0)
        val vote_average = intent.getDoubleExtra("vote_average",0.0)
        val language = intent.getStringExtra("language")
        val adult = intent.getBooleanExtra("adult",false)
        val video = intent.getBooleanExtra("video",false)
        val poster_path = intent.getStringExtra("poster_path")

        val id = intent.getIntExtra("id",0)
        val backdrop_path = intent.getStringExtra("backdrop_path")
        val genre_ids = intent.getStringExtra("genre_ids")
        val original_title = intent.getStringExtra("original_title")
        val title = intent.getStringExtra("title")

        val movie = FavoriteMovie.MovieItems(
            backdrop_path = backdrop_path!!,
            genre_ids = genre_ids!!,
            original_title = original_title!!,
            title = title!!,
            overview = overview!!,
            release_date = release_date!!,
            popularity = popularity,
            vote_count = vote_count,
            vote_average = vote_average,
            original_language = language!!,
            adult = adult,
            video = video,
            poster_path = poster_path!!
        )

        return movie
    }
}