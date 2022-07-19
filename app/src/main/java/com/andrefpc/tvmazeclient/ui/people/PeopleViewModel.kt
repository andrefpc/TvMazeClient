package com.andrefpc.tvmazeclient.ui.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.data.ApiError
import com.andrefpc.tvmazeclient.data.ApiResult
import com.andrefpc.tvmazeclient.data.Person
import com.andrefpc.tvmazeclient.repositories.TvMazeRepository
import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.launch

class PeopleViewModel(
    private val dispatcher: CoroutineContextProvider,
    private val tvMazeRepository: TvMazeRepository
) : ViewModel() {

    private val _listPeople = MutableLiveData<List<Person>>()
    val listPeople: LiveData<List<Person>> get() = _listPeople

    private val _addToListPeople = MutableLiveData<List<Person>>()
    val addToListPeople: LiveData<List<Person>> get() = _addToListPeople

    private val _error = MutableLiveData<ApiError>()
    val error: LiveData<ApiError> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    var currentPage = 0
    var searching = false

    fun getPeople() {
        searching = false
        if (currentPage == 0) _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getPeople(currentPage)) {
                is ApiResult.Success -> {
                    if (currentPage == 0) _loading.postValue(false)
                    result.result?.let {
                        if (currentPage == 0) _listPeople.postValue(it)
                        else _addToListPeople.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    if (currentPage == 0) _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }

    fun searchPeople(term: String) {
        searching = true
        _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.searchPeople(term)) {
                is ApiResult.Success -> {
                    _loading.postValue(false)
                    result.result?.let {
                        _listPeople.postValue(it)
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                    }
                }
                is ApiResult.Error -> {
                    _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
            }
        }
    }

}