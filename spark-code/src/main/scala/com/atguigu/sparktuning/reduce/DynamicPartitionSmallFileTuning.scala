package com.atguigu.sparktuning.reduce

import java.time.LocalDateTime

import com.atguigu.sparktuning.utils.InitUtil
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object DynamicPartitionSmallFileTuning {


  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("DynamicPartitionSmallFileTuning")
      .set("spark.sql.shuffle.partitions", "36")
      .setMaster("local[*]") //TODO 要打包提交集群执行，注释掉
    val sparkSession: SparkSession = InitUtil.initSparkSession(sparkConf)

   /* println(LocalDateTime.now())
    sparkSession.sql(
      """
        |CREATE TABLE if not exists `sparktuning`.`dynamic_csc` (
        |  `courseid` string,
        |  `coursename` STRING,
        |  `createtime` STRING,
        |  `discount` STRING,
        |  `orderid` STRING,
        |  `sellmoney` STRING,
        |  `dt` STRING,
        |  `dn` STRING)
        |USING parquet
        |PARTITIONED BY (dt, dn)
      """.stripMargin)*/
/*

    sparkSession.sql(
      """ select dt,dn from sparktuning.course_shopping_cart group by dt,dn""").show()*/

     /* println(LocalDateTime.now())
      sparkSession.sql(
        """
          |insert overwrite sparktuning.dynamic_csc partition(dt,dn)
          |select * from sparktuning.course_shopping_cart
        """.stripMargin)
*/
      println(LocalDateTime.now())
         // TODO 非倾斜分区写入
         sparkSession.sql(
           """
             |insert overwrite sparktuning.dynamic_csc partition(dt,dn)
             |select * from sparktuning.course_shopping_cart
             |where dt!='20190722' and dn!='webA'
             |distribute by dt,dn
           """.stripMargin)

         // TODO 倾斜分区打散写入
         sparkSession.sql(
           """
             |insert overwrite sparktuning.dynamic_csc partition(dt,dn)
             |select * from sparktuning.course_shopping_cart
             |where dt='20190722' and dn='webA'
             |distribute by cast(rand() * 5 as int)
           """.stripMargin)

             println(LocalDateTime.now())


    //    while (true) {}
  }
}
