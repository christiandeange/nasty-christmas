package com.deange.nastychristmas.ui.workflow

import kotlin.reflect.KClass

abstract class ViewEnvironmentKey<T : Any>(
  val type: KClass<T>
) {
  abstract val default: T

  override fun equals(other: Any?): Boolean {
    return type == (other as? ViewEnvironmentKey<*>)?.type
  }

  override fun hashCode(): Int {
    return type.hashCode()
  }

  override fun toString(): String {
    return "ViewEnvironmentKey($type)-${super.toString()}"
  }
}
