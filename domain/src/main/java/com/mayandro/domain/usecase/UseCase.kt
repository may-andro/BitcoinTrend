package com.mayandro.domain.usecase

import com.mayandro.common.network.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

abstract class UseCase<in P, O: Any> {
    abstract suspend fun run(param: P): Flow<NetworkStatus<O>>

    open operator fun invoke(
        scope: CoroutineScope,
        param: P,
        onResult: ((NetworkStatus<O>) -> Unit) = {}
    ) {
        scope.launch(Dispatchers.Main) {
            run(param)
                .flowOn(Dispatchers.IO)
                .collect {
                    onResult(it)
                }
        }
    }
}