package com.example.myawesomelauncher.base.interactor

import io.reactivex.Single

abstract class SingleUseCaseWithoutParams<T> {

  /**
   * Builds a [Single] which will be used when the current [SingleUseCase] is executed.
   */
  protected abstract fun buildUseCaseObservable(): Single<T>

  /**
   * Executes the current use case without subscription.
   * Handle it by yourself.
   */
  fun execute(): Single<T> {
    return buildUseCaseObservable()
  }
}