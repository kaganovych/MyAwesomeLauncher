package com.example.myawesomelauncher.base.interactor

import io.reactivex.Completable

/**
 * Abstract class for a UseCase that returns an instance of a [Completable].
 */
abstract class CompletableUseCase<in Params> {

  /**
   * Builds a [Completable] which will be used when the current [CompletableUseCase] is executed.
   */
  protected abstract fun buildUseCaseObservable(params: Params): Completable

  fun execute(params: Params): Completable {
    return buildUseCaseObservable(params)
  }

}