package com.andrefpc.tvmazeclient.util

import com.andrefpc.tvmazeclient.util.CoroutineContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineContextProvider(
    val testDispatcher: CoroutineContext = UnconfinedTestDispatcher()
) : CoroutineContextProvider() {
    override val Main: CoroutineContext get() = testDispatcher
    override val IO: CoroutineContext get() = testDispatcher
}