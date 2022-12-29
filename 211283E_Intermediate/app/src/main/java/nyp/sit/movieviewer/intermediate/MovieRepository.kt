package nyp.sit.movieviewer.intermediate

import android.support.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import nyp.sit.movieviewer.intermediate.entity.MovieItem

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies = movieDao.getAll()

    suspend fun insert(movie: MovieItem) {
        movieDao.insertMovie(movie)
    }

    suspend fun delete() {
        movieDao.delete()
    }

}