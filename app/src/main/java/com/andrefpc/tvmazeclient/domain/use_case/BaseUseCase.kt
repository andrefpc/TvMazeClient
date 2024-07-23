package com.andrefpc.tvmazeclient.domain.use_case

abstract class BaseUseCase<T, in P> where T: Any {
    abstract suspend fun invoke(params: P): T
}