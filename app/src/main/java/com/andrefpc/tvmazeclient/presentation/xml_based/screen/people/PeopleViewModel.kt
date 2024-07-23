package com.andrefpc.tvmazeclient.presentation.xml_based.screen.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.presentation.model.handler.PeopleUseCaseHandler
import com.andrefpc.tvmazeclient.presentation.model.PersonViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PeopleActivity
 */
class PeopleViewModel(
    private val peopleHandler: PeopleUseCaseHandler
) : ViewModel() {
    private val _listPeople = MutableLiveData<List<PersonViewState>>()
    val listPeople: LiveData<List<PersonViewState>> get() = _listPeople

    private val _addToListPeople = MutableLiveData<List<PersonViewState>>()
    val addToListPeople: LiveData<List<PersonViewState>> get() = _addToListPeople

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _showEmpty = MutableLiveData<Boolean>()
    val showEmpty: LiveData<Boolean> get() = _showEmpty

    var currentPage = 0
    var searching = false

    /**
     * Exception handler for the coroutines
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        viewModelScope.launch {
            _error.postValue(e)
            if (currentPage == 0) _loading.postValue(false)
        }
    }

    /**
     * Get the people list from the server
     */
    fun getPeople() {
        searching = false
        if (currentPage == 0) _loading.value = true
        viewModelScope.launch(exceptionHandler) {
            val list = peopleHandler.getPeople(currentPage).map { PersonViewState(it) }
            if (currentPage == 0) {
                if (list.isEmpty()) {
                    _showEmpty.postValue(true)
                } else {
                    _listPeople.postValue(list)
                    delay(1000)
                    _loading.postValue(false)
                }
            } else {
                _addToListPeople.postValue(list)
            }
        }
    }

    /**
     * Search the people list from the server
     * @param [term] to be searched
     */
    fun searchPeople(term: String) {
        searching = true
        _loading.value = true
        viewModelScope.launch(exceptionHandler) {
            val list = peopleHandler.getPeople(searchTerm = term).map { PersonViewState(it) }
            if (list.isEmpty()) {
                _showEmpty.postValue(true)
            } else {
                _listPeople.postValue(list)
                delay(1000)
                _loading.postValue(false)
            }
        }
    }

}