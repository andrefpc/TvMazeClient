package com.andrefpc.tvmazeclient.presentation.xml_based.screen.person_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.Show
import com.andrefpc.tvmazeclient.domain.use_case.PersonDetailsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PersonActivity
 */
class PersonDetailsViewModel(
    private val personDetailsUseCase: PersonDetailsUseCase
) : ViewModel() {

    private val _listShows = MutableLiveData<List<Show>>()
    val listShows: LiveData<List<Show>> get() = _listShows

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _error.postValue(e)
        }
    }

    /**
     * Get the shows casted by a person in the server
     */
    fun getShows(id: Int) {
        viewModelScope.launch(exceptionHandler) {
            val list = personDetailsUseCase.getPersonShows(id)
            _listShows.postValue(list)
        }
    }
}