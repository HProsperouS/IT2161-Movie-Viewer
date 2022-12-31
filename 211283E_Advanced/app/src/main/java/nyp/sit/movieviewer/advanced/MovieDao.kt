package nyp.sit.movieviewer.advanced

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.advanced.entity.MovieItem

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: MovieItem)

    @Query("SELECT * FROM MovieItem_Table")
    fun getAll(): Flow<List<MovieItem>>

    @Query("DELETE FROM MovieItem_Table")
    suspend fun delete()
}