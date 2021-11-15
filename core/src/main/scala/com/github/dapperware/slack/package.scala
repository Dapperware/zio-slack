package com.github.dapperware

import zio.Has

package object slack extends WithAccess {
  type SlackError = Throwable
  type SlackEnv   = Has[SlackClient] with Has[AccessToken]
}
