package com.example.myawesomelauncher.base.interactor

import io.reactivex.Observable

abstract class ObservableUseCaseWithoutParams<T> {

  protected abstract fun buildUseCaseObservable(): Observable<T>

  /**
   * Executes the current use case without subscription.
   * Handle it by yourself.
   */
  fun execute(): Observable<T> {
    return buildUseCaseObservable()
  }
}