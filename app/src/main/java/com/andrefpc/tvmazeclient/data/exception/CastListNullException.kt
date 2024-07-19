package com.andrefpc.tvmazeclient.data.exception

class CastListNullException : Exception() {
    override val message: String
        get() = "Cast list not found"
}