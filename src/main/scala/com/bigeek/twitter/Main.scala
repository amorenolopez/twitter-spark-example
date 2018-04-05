package com.bigeek.twitter

import com.bigeek.twitter.services.TemporaryTables
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils



object Main {

  def main(args: Array[String]): Unit = {
    val runLocal = args.length > 0 && args(args.length-1).equals("L")

    var conf : SparkConf = new SparkConf()
    var spark: SparkSession = null

      if (runLocal) {
      conf = conf
        .setAppName("twitter-spark-example")
        .setMaster("local[*]")
       spark = SparkSession
          .builder()
          .appName("twitter-spark-example")
          .master("local[*]")
          .enableHiveSupport()
          .getOrCreate()

    } else {
      conf = conf
        .setAppName("twitter-spark-example")
        spark = SparkSession
          .builder()
          .appName("twitter-spark-example")
          .enableHiveSupport()
          .getOrCreate()
      }

    var ssc: StreamingContext = new StreamingContext(spark.sparkContext, Seconds(15))

    // Setting access token for twitter API
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey(System.getenv("CONSUMER_KEY"))
      .setOAuthConsumerSecret(System.getenv("CONSUMER_SECRET"))
      .setOAuthAccessToken(System.getenv("TOKEN_ACCESS"))
      .setOAuthAccessTokenSecret(System.getenv("TOKEN_SECRET"))

    val auth = new OAuthAuthorization(cb.build)
    val tweet = TwitterUtils.createStream(ssc, Some(auth))

    val temporalyTables = TemporaryTables(spark)

    spark.sqlContext.sql("select * from MAP_TABLE").show()
    // scalastyle:off
    tweet.print()
    // scalastyle:on


    ssc.start()
    ssc.awaitTermination()

  }

}
