package com.andrefpc.tvmazeclient.presentation.model

import com.andrefpc.tvmazeclient.domain.model.Show

data class PersonShowViewState(
    var show: ShowViewState
) {
    constructor(show: Show) : this(ShowViewState(show))
}
