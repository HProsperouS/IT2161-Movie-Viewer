package nyp.sit.movieviewer.intermediate

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.intermediate.entity.MovieItem

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: MovieItem)

    @Query("SELECT * FROM MovieItem_Table")
    fun getAll(): Flow<List<MovieItem>>

    @Query("DELETE FROM MovieItem_Table")
    suspend fun delete()
}