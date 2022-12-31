package nyp.sit.movieviewer.advanced
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*
import nyp.sit.movieviewer.advanced.entity.MovieItem

@DynamoDBTable(tableName = "UserData")
class FavMovie {
    @DynamoDBAttribute(attributeName = "id")
    var id : String? = null

    @DynamoDBAttribute(attributeName = "favMovie")
    var favMovie : List<MovieItem>? = null

}