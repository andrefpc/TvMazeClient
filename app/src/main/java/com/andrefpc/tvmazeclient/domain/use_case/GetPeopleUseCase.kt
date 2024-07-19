package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.data.exception.PeopleListNullException
import com.andrefpc.tvmazeclient.data.exception.PeopleListRequestException
import com.andrefpc.tvmazeclient.domain.repository.api.TvMazeRepository
import javax.inject.Inject

class GetPeopleUseCase @Inject constructor(
    private val tvMazeRepository: TvMazeRepository
) {
    @Throws(PeopleListNullException::class, PeopleListRequestException::class)
    suspend operator fun invoke(page: Int = 0, searchTerm: String? = null): List<Person> {
        when (
            val result = searchTerm?.let {
                tvMazeRepository.searchPeople(it)
            } ?: run {
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