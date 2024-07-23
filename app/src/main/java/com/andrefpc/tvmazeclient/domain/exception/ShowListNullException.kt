package com.andrefpc.tvmazeclient.domain.exception

class ShowListNullException : Exception() {
    override val message: String
        get() = "Show list not found"
}