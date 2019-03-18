package com.example.myawesomelauncher.base.interactor

import io.reactivex.Completable
import io.reactivex.Single

abstract class CompletableUseCaseWithoutParams {

  /**
   * Builds a [Single] which will be used when the current [SingleUseCase] is executed.
   */
  protected abstract fun buildUseCaseObservable(): Completable

  /**
   * Executes the current use case without subscription.
   * Handle it by yourself.
   */
  fun execute(): Completable {
    return buildUseCaseObservable()
  }
}
