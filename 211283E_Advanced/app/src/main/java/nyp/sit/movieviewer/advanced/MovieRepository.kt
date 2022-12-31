package nyp.sit.movieviewer.advanced

import nyp.sit.movieviewer.advanced.entity.MovieItem

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies = movieDao.getAll()

    suspend fun insert(movie: MovieItem) {
        movieDao.insertMovie(movie)
    }

    suspend fun delete() {
        movieDao.delete()
    }

}
