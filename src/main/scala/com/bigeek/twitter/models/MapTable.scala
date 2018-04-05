package com.bigeek.twitter.models

case class MapTable(location : String,
                    text : String)

object MapTable {
  val TABLE_NAME = "MAP_TABLE"
}
