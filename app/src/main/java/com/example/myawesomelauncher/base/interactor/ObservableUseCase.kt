package com.example.myawesomelauncher.base.interactor

import io.reactivex.Observable

abstract class ObservableUseCase<T, in Params> {

  protected abstract fun buildUseCaseObservable(params: Params): Observable<T>

  /**
   * Executes the current use case without subscription.
   * Handle it by yourself.
   */
  fun execute(params: Params): Observable<T> {
    return buildUseCaseObservable(params)
  }
}