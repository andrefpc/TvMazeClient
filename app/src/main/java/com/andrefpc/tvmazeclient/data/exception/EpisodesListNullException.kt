package com.andrefpc.tvmazeclient.data.exception

class EpisodesListNullException : Exception() {
    override val message: String
        get() = "Episode list not found"
}