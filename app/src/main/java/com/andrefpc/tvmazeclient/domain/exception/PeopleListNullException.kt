package com.andrefpc.tvmazeclient.domain.exception

class PeopleListNullException : Exception() {
    override val message: String
        get() = "People list not found"
}