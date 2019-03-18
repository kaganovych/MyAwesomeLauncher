package com.example.myawesomelauncher.base.mapper

import java.util.*

abstract class BaseNullableMapper<From, To> {

  abstract fun map(from: From?): To?

  fun map(froms: List<From?>): List<To?> {
    val result = ArrayList<To?>(froms.size)
    for (from in froms) {
      result.add(map(from))
    }
    return result
  }
}
