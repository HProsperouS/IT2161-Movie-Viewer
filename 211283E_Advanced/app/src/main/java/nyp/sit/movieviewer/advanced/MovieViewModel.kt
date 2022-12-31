package nyp.sit.movieviewer.advanced

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import nyp.sit.movieviewer.advanced.entity.MovieItem

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // Using LiveData and caching what allMovies returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMovies: LiveData<List<MovieItem>> = repository.allMovies.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(movie: MovieItem) = viewModelScope.launch {
        repository.insert(movie)
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}