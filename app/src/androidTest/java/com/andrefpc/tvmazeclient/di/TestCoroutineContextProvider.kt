package com.andrefpc.tvmazeclient.di

import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext

class TestCoroutineContextProvider(
    private val testDispatcher: CoroutineContext = StandardTestDispatcher()
) : CoroutineContextProvider() {
    override val Main: CoroutineContext get() = testDispatcher
    override val IO: CoroutineContext get() = testDispatcher
}