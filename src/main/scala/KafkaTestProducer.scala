package com.colobu.kafka

import java.io.FileInputStream
import java.util.Properties
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.log4j.Logger
import scala.util.Random


object KafkaTestProducer {
  var log = Logger.getLogger("KafkaTestProducer")
  def main(arg: Array[String]):Unit = {
    var callback = new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception) = {
          if(metadata != null){
            println("SUCCESS")
          }
      }
    }
    val rand = new Random()
    var messageId = 0
    val props = new Properties()
    props.load(new FileInputStream("src/main/resources/kafka-producer.properties"));
    val topic = "test"
    val producer = new KafkaProducer[Array[Byte], Array[Byte]](props)
    while(true){
      val key = rand.nextLong().toString
     //val msg = "{\"storytype\" : \"storytype"+key+"\"}"
      val msg = format("1", "storyType_1", "action_1")
      val record = new ProducerRecord[Array[Byte], Array[Byte]](topic, key.getBytes("utf-8"), msg.getBytes("utf-8"));
      println("Sending : "+ msg)
      producer.send(record, callback)
      messageId += 1
      Thread.sleep(1000L)
    }

    def format(activity:String, storyType:String, action: String): String ={
      val output = "{'activity_id':'"+activity+"','story_type':'"+storyType+"','action':'"+action+"'}"
      output
    }
  }
}