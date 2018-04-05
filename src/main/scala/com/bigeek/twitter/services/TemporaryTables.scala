package com.bigeek.twitter.services

import com.bigeek.twitter.models.MapTable
import twitter4j.Status

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.ReceiverInputDStream


case class TemporaryTables (spark: SparkSession) {

  import spark.sqlContext.implicits._

  def getMapTable(tweets: ReceiverInputDStream[Status]): Unit = {
    tweets
      .map(tweet => MapTable(tweet.getText(), tweet.getText()))
      .foreachRDD{rdd => rdd.toDF().createOrReplaceTempView(MapTable.TABLE_NAME)}
  }
}

