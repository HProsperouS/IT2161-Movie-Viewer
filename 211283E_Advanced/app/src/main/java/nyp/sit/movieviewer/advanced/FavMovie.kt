package nyp.sit.movieviewer.advanced
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*
import nyp.sit.movieviewer.advanced.entity.MovieItem

@DynamoDBTable(tableName = "UserData")
class FavoriteMovie {
    @DynamoDBHashKey(attributeName = "id")
    var id : String? = null

    @DynamoDBAttribute(attributeName = "favMovie")
    var favMovie : MutableList<MovieItems>? = null

    @DynamoDBDocument
    class MovieItems(
        @DynamoDBAttribute(attributeName = "poster_path")
        var poster_path: String? = null,

        @DynamoDBAttribute(attributeName = "adult")
        var adult: Boolean? = null,

        @DynamoDBAttribute(attributeName = "overview")
        var overview: String? = null,

        @DynamoDBAttribute(attributeName = "release_date")
        var release_date: String? = null,

        @DynamoDBAttribute(attributeName = "genre_ids")
        var genre_ids: String? = null,

        @DynamoDBAttribute(attributeName = "original_title")
        var original_title: String? = null,

        @DynamoDBAttribute(attributeName = "original_language")
        var original_language: String? = null,

        @DynamoDBAttribute(attributeName = "title")
        var title: String? = null,

        @DynamoDBAttribute(attributeName = "backdrop_path")
        var backdrop_path: String? = null,

        @DynamoDBAttribute(attributeName = "popularity")
        var popularity: Double = 0.0,

        @DynamoDBAttribute(attributeName = "vote_count")
        var vote_count: Int = 0,

        @DynamoDBAttribute(attributeName = "video")
        var video: Boolean? = null,

        @DynamoDBAttribute(attributeName = "vote_average")
        var vote_average: Double = 0.0
    )

}