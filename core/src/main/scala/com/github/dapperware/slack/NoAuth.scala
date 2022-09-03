package com.github.dapperware.slack

sealed trait NoAuth

case object NoAuth {
  private val instance: NoAuth = new NoAuth {}

}
