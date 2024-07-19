package com.andrefpc.tvmazeclient.data.exception

class ShowListNullException : Exception() {
    override val message: String
        get() = "Show list not found"
}