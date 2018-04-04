package com.bigeek.twitter

import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.twitter.TwitterUtils



object Main {

  def main(args: Array[String]): Unit = {

    val runLocal = args.length > 0 && args(args.length-1).equals("L")

    var conf : SparkConf = new SparkConf()

    if (runLocal) {
      conf = conf
        .setAppName("twitter-spark-example")
        .setMaster("local[*]")

    } else {
      conf = conf
        .setAppName("twitter-spark-example")
    }

    var ssc: StreamingContext = new StreamingContext(conf, Seconds(15))

    // Setting access token for twitter API
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true).setOAuthConsumerKey("")
      .setOAuthConsumerSecret("")
      .setOAuthAccessToken("")
      .setOAuthAccessTokenSecret("")

    val auth = new OAuthAuthorization(cb.build)
    val tweets = TwitterUtils.createStream(ssc, Some(auth))

    // scalastyle:off
    tweets.print()
    // scalastyle:on

    ssc.start()
    ssc.awaitTermination()

  }

}
