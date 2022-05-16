package com.github.dapperware.slack

trait Bookmarks {

  def addBookmark(
    channel: String,
    title: String,
    `type`: String,
    emoji: Option[String] = None,
    entityId: Option[String] = None,
    link: Option[String] = None,
    parentId: Option[String] = None
  )

}
