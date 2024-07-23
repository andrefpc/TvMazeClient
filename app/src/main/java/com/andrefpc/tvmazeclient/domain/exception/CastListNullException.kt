package com.andrefpc.tvmazeclient.domain.exception

class CastListNullException : Exception() {
    override val message: String
        get() = "Cast list not found"
}