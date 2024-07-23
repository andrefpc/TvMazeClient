package com.andrefpc.tvmazeclient.domain.model

data class Show(
    var id: Int,
    var name: String,
    var image: Image?,
    var schedule: Schedule,
    var genres: ArrayList<String>,
    var summary: String,
    var premiered: String?,
    var ended: String?
)
