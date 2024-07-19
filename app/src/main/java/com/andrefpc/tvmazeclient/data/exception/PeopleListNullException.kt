package com.andrefpc.tvmazeclient.data.exception

class PeopleListNullException : Exception() {
    override val message: String
        get() = "People list not found"
}