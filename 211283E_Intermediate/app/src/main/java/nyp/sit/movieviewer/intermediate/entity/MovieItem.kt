package nyp.sit.movieviewer.intermediate.entity

import androidx.room.*
import java.io.Serializable

//class MovieItem(
//    poster_path: String?, adult: Boolean?, overview: String?,
//    release_date: String?, genre_ids: String?, id: Int = 0,
//    original_title: String?, original_language: String?,
//    title: String?, backdrop_path: String?, popularity: Double = 0.0,
//    vote_count: Int = 0, video: Boolean?, vote_average: Double = 0.0
//) {
//
//    var id: Int = 0
//
//    var poster_path: String? = null
//
//    var adult: Boolean? = null
//
//    var overview: String? = null
//
//    var release_date: String? = null
//
//    var genre_ids: String? = null
//
//    var original_title: String? = null
//
//    var original_language: String? = null
//
//    var title: String? = null
//
//    var backdrop_path: String? = null
//
//    var popularity: Double = 0.0
//
//    var vote_count: Int = 0
//
//    var video: Boolean? = null
//
//    var vote_average: Double = 0.0
//
//    init {
//        this.poster_path = poster_path
//        this.adult = adult
//        this.overview = overview
//        this.release_date = release_date
//        this.genre_ids = genre_ids
//        this.id = id
//        this.original_title = original_title
//        this.original_language = original_language
//        this.title = title
//        this.backdrop_path = backdrop_path
//        this.popularity = popularity
//        this.vote_count = vote_count
//        this.video = video
//        this.vote_average = vote_average
//    }
//
//}

@Entity(tableName = "MovieItem_Table")
data class MovieItem(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "adult") val adult: Boolean,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "release_date") val release_date: String,
    @ColumnInfo(name = "genre_ids") val genre_ids: String,
    @ColumnInfo(name = "original_title") val original_title: String,
    @ColumnInfo(name = "original_language") val original_language: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "backdrop_path") val backdrop_path: String,
    @ColumnInfo(name = "popularity") val popularity: Double,
    @ColumnInfo(name = "vote_count") val vote_count: Int,
    @ColumnInfo(name = "video") val video: Boolean,
    @ColumnInfo(name = "vote_average") val vote_average: Double
)

