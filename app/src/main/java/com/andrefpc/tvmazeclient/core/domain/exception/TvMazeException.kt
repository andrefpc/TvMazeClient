package com.andrefpc.tvmazeclient.core.domain.exception

import com.andrefpc.tvmazeclient.core.data.ApiError

class PeopleListNullException: Exception()
class PeopleListRequestException(val apiError: ApiError): Exception()
class ShowListNullException: Exception()
class ShowListRequestException(val apiError: ApiError): Exception()
class CastListNullException: Exception()
class CastListRequestException(val apiError: ApiError): Exception()
class SeasonListNullException: Exception()
class SeasonListRequestException(val apiError: ApiError): Exception()
class EpisodesListNullException: Exception()
class EpisodesListRequestException(val apiError: ApiError): Exception()