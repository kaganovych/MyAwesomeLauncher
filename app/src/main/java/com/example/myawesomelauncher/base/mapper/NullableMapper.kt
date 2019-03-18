package com.example.myawesomelauncher.base.mapper

import java.util.*

abstract class NullableMapper<From, To> : BaseNullableMapper<From, To>() {

  abstract fun reverse(to: To?): From?

  fun reverse(tos: List<To?>): List<From?> {
    val result = ArrayList<From?>(tos.size)
    for (to in tos) {
      result.add(reverse(to))
    }
    return result
  }
}