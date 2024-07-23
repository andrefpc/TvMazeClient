package com.andrefpc.tvmazeclient.domain.use_case

import com.andrefpc.tvmazeclient.domain.exception.PeopleListNullException
import com.andrefpc.tvmazeclient.domain.exception.PeopleListRequestException
import com.andrefpc.tvmazeclient.domain.model.ApiResult
import com.andrefpc.tvmazeclient.domain.model.Person
import com.andrefpc.tvmazeclient.domain.repository.api.PersonRepository
import javax.inject.Inject

class GetPeopleUseCase @Inject constructor(
    private val personRepository: PersonRepository
) {
    @Throws(PeopleListNullException::class, PeopleListRequestException::class)
    suspend operator fun invoke(page: Int = 0, searchTerm: String? = null): List<Person> {
        when (
            val result = searchTerm?.let {
                personRepository.searchPeople(it)
            } ?: run {
                personRepository.getPeople(page)
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