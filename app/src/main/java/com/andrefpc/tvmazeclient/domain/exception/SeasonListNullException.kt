package com.andrefpc.tvmazeclient.domain.exception

class SeasonListNullException : Exception() {
    override val message: String
        get() = "Season list not found"
}