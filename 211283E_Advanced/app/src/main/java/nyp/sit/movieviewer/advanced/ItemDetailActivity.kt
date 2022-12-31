package nyp.sit.movieviewer.advanced

import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.config.AWSConfiguration
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.entity.MovieItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ItemDetailActivity : AppCompatActivity() {

    var activityCoroutineScope:CoroutineScope? = null
    var dynamoDBMapper : DynamoDBMapper? = null
    var currentFavMovie: FavMovie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        activityCoroutineScope?.launch() {
            //Initialize DynamoDBMapper
            val credentials : AWSCredentials = AWSMobileClient.getInstance().credentials
            val dynamoDBClient = AmazonDynamoDBClient(credentials)

            dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(
                    AWSMobileClient.getInstance().configuration
                ).build()

        }
//        catch(e:Exception){
//            Log.d("DynamoDB", "Exception ${e.message}")
//        }

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

    fun runCurrentFavs(v: View) {

        activityCoroutineScope?.launch() {
            //Retrieve any existing data from DynamoDBMapper table
            //Create the scan expression to retrieve only data created by the user
            val eav = HashMap<String,AttributeValue>()
            eav.put(":val1",AttributeValue().withS(AWSMobileClient.getInstance().username))

            val queryExpression =
                DynamoDBScanExpression().withFilterExpression("id = :val1")
                    .withExpressionAttributeValues(eav)
            val itemList = dynamoDBMapper?.scan(FavMovie::class.java,queryExpression)
            //For each item retrieved, assign the data to a variable in the activity.
            // Create a loop to printout the data of each note into a string.
            // If no item exist, create a new FavMovie

            if (itemList?.size != 0 && itemList != null){
                for (i in itemList.iterator()){
                    currentFavMovie = i
                }
            }
        }

    }

    fun runAddFav(movieItem: MovieItem) {
        var newFavMovie = FavMovie()
        newFavMovie.id = AWSMobileClient.getInstance().username
        newFavMovie.favMovie = listOf(movieItem)
        //Make use of DynamoDBMapper to save NotesDO to DynamoDB table.
        activityCoroutineScope?.launch() {

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

    private fun getMovie():MovieItem{
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

        val movie = MovieItem(
            id = id,
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