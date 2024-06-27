package com.andrefpc.tvmazeclient.ui.xml_based.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrefpc.tvmazeclient.core.data.ApiError
import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.data.ScreenState
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import com.andrefpc.tvmazeclient.core.util.CoroutineContextProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel used by the PeopleActivity
 */
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

    private val _showEmpty = MutableLiveData<Boolean>()
    val showEmpty: LiveData<Boolean> get() = _showEmpty

    var currentPage = 0
    var searching = false

    /**
    * Get the people list from the server
    */
    fun getPeople() {
        searching = false
        if (currentPage == 0) _loading.value = true
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.getPeople(currentPage)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        if(currentPage == 0){
                            if(it.isEmpty()){
                                _showEmpty.postValue(true)
                            }else{
                                _listPeople.postValue(it)
                                delay(1000)
                                _loading.postValue(false)
                            }
                        }
                        else {
                            _addToListPeople.postValue(it)
                        }
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                        if(currentPage == 0) _loading.postValue(false)
                    }
                }
                is ApiResult.Error -> {
                    if (currentPage == 0) _loading.postValue(false)
                    _error.postValue(result.apiError)
                }
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
        viewModelScope.launch(dispatcher.IO) {
            when (val result = tvMazeRepository.searchPeople(term)) {
                is ApiResult.Success -> {
                    result.result?.let {
                        if(it.isEmpty()){
                            _showEmpty.postValue(true)
                        }else{
                            _listPeople.postValue(it)
                            delay(1000)
                            _loading.postValue(false)
                        }
                    } ?: kotlin.run {
                        _error.postValue(ApiError())
                        _loading.postValue(false)
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