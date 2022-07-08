package com.github.dapperware.slack

import zio._

/**
 * A base error type for slack related issues
 */
trait SlackException extends Exception

object SlackException {

  /**
   * Indicates that there was an error in attempting to decode a message from slack
   */
  case class DecodingError(message: String) extends SlackException

  /**
   * An error that was returned from Slack while trying to process a request
   */
  case class ResponseError(error: String) extends Exception(s"Slack API Error: $error") with SlackException

  case class ProtocolError(error: String) extends Exception(s"Slack protocol Error: $error") with SlackException

  /**
   * Indicates an error that occurred while attempting to make a network request
   * this usually indicates a problem with the local configuration or the that slack may be down or under maintenance
   */
  case class NetworkError(status: Int, innerThrowable: Option[Throwable] = None) extends SlackException

  case class RatelimitError(retryAfter: Option[Duration])
      extends Exception(s"Rate-limited try again after ${retryAfter.fold("some time")(_.render)}")

}
