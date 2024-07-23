package com.andrefpc.tvmazeclient.domain.exception

class EpisodesListNullException : Exception() {
    override val message: String
        get() = "Episode list not found"
}