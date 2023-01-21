package nyp.sit.movieviewer.advanced

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import kotlinx.android.synthetic.main.activity_fav_movies.*
import kotlinx.coroutines.*

class FavMoviesActivity : AppCompatActivity() {
    var activityCoroutineScope:CoroutineScope? = null
    var dynamoDBMapper : DynamoDBMapper? = null

    var moviesAdapter : FavoriteMovieAdapter ?= null

    var currentMoviesList: FavoriteMovie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_movies)

        activityCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

        activityCoroutineScope?.launch() {
            try {
                //Initialize DynamoDBMapper
                val credentials : AWSCredentials = AWSMobileClient.getInstance().credentials
                val dynamoDBClient = AmazonDynamoDBClient(credentials)

                dynamoDBMapper = DynamoDBMapper.builder()
                    .dynamoDBClient(dynamoDBClient)
                    .awsConfiguration(
                        AWSMobileClient.getInstance().configuration
                    ).build()
                runRetrieveFavs()
            }catch (ex:Exception){
                Log.d("DynamoDB","Exception ${ex.message}")
            }
        }
    }

    fun runRetrieveFavs() {
        activityCoroutineScope?.launch() {
            try {
                //Retrieve any existing data from DynamoDBMapper table
                //Create the scan expression to retrieve only data created by the user
                val eav = HashMap<String, AttributeValue>()
                eav.put(":user", AttributeValue().withS(AWSMobileClient.getInstance().username))

                val queryExpression =
                    DynamoDBScanExpression().withFilterExpression("id = :user")
                        .withExpressionAttributeValues(eav)
                val itemList = dynamoDBMapper?.scan(FavoriteMovie::class.java, queryExpression)
                println("Item List Here ${itemList}")

                if (itemList?.size != 0 && itemList != null){
                    withContext(Dispatchers.Main){
                        val listView: ListView = findViewById(R.id.favList)
                        val list: MutableList<FavoriteMovie.MovieItems> = itemList[0].favMovie?.toMutableList()!!

                         moviesAdapter = FavoriteMovieAdapter(this@FavMoviesActivity, R.layout.card_items_movie, list)
                         listView.adapter = moviesAdapter

                    }
                }
            } catch (e: Exception) {
                Log.d("DynamoDB Retrieve", "Exception ${e.message}")
            }
        }
    }
}

