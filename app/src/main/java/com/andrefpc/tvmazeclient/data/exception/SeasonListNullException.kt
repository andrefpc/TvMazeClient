package com.andrefpc.tvmazeclient.data.exception

class SeasonListNullException : Exception() {
    override val message: String
        get() = "Season list not found"
}