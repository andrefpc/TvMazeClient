package com.andrefpc.tvmazeclient.presentation.xml_based.screen.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.use_case.PeopleUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PeopleActivity
 */
class PeopleViewModel(
    private val peopleUseCase: PeopleUseCase
) : ViewModel() {
    private val _listPeople = MutableLiveData<List<Person>>()
    val listPeople: LiveData<List<Person>> get() = _listPeople

    private val _addToListPeople = MutableLiveData<List<Person>>()
    val addToListPeople: LiveData<List<Person>> get() = _addToListPeople

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
            val list = peopleUseCase.getPeople(currentPage)
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
            val list = peopleUseCase.getPeople(searchTerm = term)
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