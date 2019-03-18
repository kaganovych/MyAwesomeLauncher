package com.example.myawesomelauncher.di

/**
 * Interface representing a contract for clients that contains a component for dependency injection.
 */
interface HasComponent<out C> {
  fun getComponent(): C
  fun initializeInjector()
}
