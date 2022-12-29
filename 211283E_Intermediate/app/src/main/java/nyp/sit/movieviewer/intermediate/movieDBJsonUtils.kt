package nyp.sit.movieviewer.intermediate

import android.content.Context
import nyp.sit.movieviewer.intermediate.entity.MovieItem
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class movieDBJsonUtils() {

    companion object {

        @Throws(JSONException::class)
        fun getMovieDetailsFromJson(context: Context, movieDetailsJsonStr: String): ArrayList<MovieItem>? {

            val parsedMovieData = ArrayList<MovieItem>()

            val movieDetails = JSONObject(movieDetailsJsonStr).getJSONArray("results")
            //Creates a range of values from 0 (inclusive) to movieDetails.length() (exclusive)
            for (i in 0 until movieDetails.length())
            {
                val poster_path: String
                val adult: Boolean
                val overview: String
                val release_date: String
                val genre_ids: String
                val id: Int
                val original_title: String
                val original_langauge: String
                val title: String
                val backdrop_path: String
                val popularity: Double
                val vote_count: Int
                val video: Boolean
                val vote_average: Double

                val movieItem = movieDetails.getJSONObject(i)

                id = movieItem.getInt("id")
                poster_path = movieItem.getString("poster_path")
                adult = movieItem.getBoolean("adult")
                overview = movieItem.getString("overview")
                release_date = movieItem.getString("release_date")
                genre_ids = movieItem.getString("genre_ids")
                original_title = movieItem.getString("original_title")
                original_langauge = movieItem.getString("original_language")
                title = movieItem.getString("title")
                backdrop_path = movieItem.getString("backdrop_path")
                popularity = movieItem.getDouble("popularity")
                vote_count = movieItem.getInt("vote_count")
                video = movieItem.getBoolean("video")
                vote_average = movieItem.getDouble("vote_average")

                parsedMovieData.add(MovieItem(
                    id = id,
                    poster_path = poster_path,
                    adult = adult,
                    overview = overview,
                    release_date = release_date,
                    genre_ids = genre_ids,
                    original_title = original_title,
                    original_language = original_langauge,
                    title = title,
                    backdrop_path = backdrop_path,
                    popularity = popularity,
                    vote_count = vote_count,
                    video = video,
                    vote_average = vote_average
                ))

            }
            return parsedMovieData
        }



    }

}




//val POSTER_PATH = "poster_path"
//val ADULT = "adult"
//val OVERVIEW = "overview"
//val RELEASE_DATE = "release_date"
//val GENRE_IDS = "genre_ids"
//val ID = "id"
//val ORIGINAL_TITLE = "original_title"
//val ORIGINAL_LANGUAGE = "original_language"
//val TITLE = "title"
//val BACKDROP_PATH = "backdrop_path"
//val POPULARITY = "popularity"
//val VOTE_COUNT = "vote_count"
//val VIDEO = "video"
//val VOTE_AVERAGE = "vote_average"