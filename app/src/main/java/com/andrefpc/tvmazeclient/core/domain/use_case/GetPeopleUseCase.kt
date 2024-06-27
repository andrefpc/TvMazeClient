package com.andrefpc.tvmazeclient.core.domain.use_case

import com.andrefpc.tvmazeclient.core.data.ApiResult
import com.andrefpc.tvmazeclient.core.data.Person
import com.andrefpc.tvmazeclient.core.domain.exception.PeopleListNullException
import com.andrefpc.tvmazeclient.core.domain.exception.PeopleListRequestException
import com.andrefpc.tvmazeclient.core.domain.repository.TvMazeRepository
import javax.inject.Inject

class GetPeopleUseCase @Inject constructor(
    private val tvMazeRepository: TvMazeRepository
) {
    @Throws(PeopleListNullException::class, PeopleListRequestException::class)
    suspend operator fun invoke(page: Int = 0, searchTerm: String? = null): List<Person> {
        when (
            val result = searchTerm?.let {
                tvMazeRepository.searchPeople(it)
            }?: run {
                tvMazeRepository.getPeople(page)
            }
        ) {
            is ApiResult.Success -> {
                result.result?.let { list ->
                    return list
                } ?: kotlin.run {
                    throw PeopleListNullException()
                }
            }
            is ApiResult.Error -> {
                throw PeopleListRequestException(result.apiError)
            }
        }
    }
}