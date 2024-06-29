package com.andrefpc.tvmazeclient.core.domain.exception

import com.andrefpc.tvmazeclient.core.data.ApiError

class PeopleListNullException : Exception() {
    override val message: String
        get() = "People list not found"
}

class PeopleListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "People list not found"
}

class ShowListNullException : Exception() {
    override val message: String
        get() = "Show list not found"
}

class ShowListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Show list not found"
}

class CastListNullException : Exception() {
    override val message: String
        get() = "Cast list not found"
}

class CastListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Cast list not found"
}

class SeasonListNullException : Exception() {
    override val message: String
        get() = "Season list not found"
}

class SeasonListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Season list not found"
}

class EpisodesListNullException : Exception() {
    override val message: String
        get() = "Episode list not found"
}

class EpisodesListRequestException(val apiError: ApiError) : Exception() {
    override val message: String
        get() = apiError.message ?: "Episode list not found"
}