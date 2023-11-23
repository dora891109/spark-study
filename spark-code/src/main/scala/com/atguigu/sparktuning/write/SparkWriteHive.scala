package com.atguigu.sparktuning.write


import com.atguigu.sparktuning.bean.CoursePay
import com.atguigu.sparktuning.utils.InitUtil
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object SparkWriteHive {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("PartitionDemo")
      .set("spark.sql.autoBroadcastJoinThreshold", "-1") //为了演示效果，先禁用了广播join
      .set("spark.sql.parquet.writeLegacyFormat", "true")
      .set("spark.sql.sources.partitionOverwriteMode", "dynamic")
      .set("hive.exec.dynamic.partition.mode", "nonstrict")
      .setMaster("local[*]")
    val sparkSession: SparkSession = InitUtil.initSparkSession(sparkConf)

    /*val t1 = sparkSession.sql(" select * from sparktuning.course_pay limit 100  ")
    t1.show()
     val t2 = sparkSession.sql(" select count(1) from sparktuning.course_shopping_cart  ")
    t2.show();
     val t3 = sparkSession.sql(" select count(1) from sparktuning.sale_course  ")
    t3.show(); */

    val course_shopping_cart = sparkSession.
      sql("analyze table sparktuning.course_shopping_cart compute statistics")

    course_shopping_cart.show()
    /*  val course_shopping_cart = sparkSession.
      sql("select * from sparktuning.course_shopping_cart")
    course_shopping_cart.write.mode(SaveMode.Overwrite).saveAsTable("sparktuning.course_shopping_cart_tmp")

    val course_pay = sparkSession.
      sql("select * from sparktuning.course_pay")
    course_pay.write.mode(SaveMode.Overwrite).saveAsTable("sparktuning.course_pay_tmp")

    //查询出三张表 并进行join 插入到最终表中
   val course_pay = sparkSession.
    sql("select  get_json_object(content, '$.createtime') as createtime,get_json_object(content, '$.discount') as discount, " +
      "get_json_object(content, '$.dn') as dn,get_json_object(content, '$.dt') as dt, get_json_object(content, '$.orderid') as orderid," +
      "get_json_object(content, '$.paymoney') as paymoney" +
      " from sparktuning.tmp_course_pay  ")

    //   course_pay.write.mode(SaveMode.Overwrite).saveAsTable("sparktuning.course_pay")

    val course_shopping_cart = sparkSession.
      sql("select  get_json_object(content, '$.courseid') as courseid,get_json_object(content, '$.coursename') as coursename, " +
        "get_json_object(content, '$.createtime') as createtime,get_json_object(content, '$.discount') as discount, get_json_object(content, '$.dn') as dn," +
        "get_json_object(content, '$.dt') as dt,get_json_object(content, '$.orderid') as orderid,get_json_object(content, '$.sellmoney') as sellmoney " +
        " from sparktuning.tmp_course_shopping_cart ")

    course_shopping_cart.write.mode(SaveMode.Overwrite).saveAsTable("sparktuning.course_shopping_cart")

    val sale_course = sparkSession.
      sql("select  get_json_object(content, '$.chapterid') as chapterid,get_json_object(content, '$.chaptername') as chaptername, " +
        "get_json_object(content, '$.courseid') as courseid,get_json_object(content, '$.coursemanager') as coursemanager, get_json_object(content, '$.coursename') as coursename," +
        "get_json_object(content, '$.dn') as dn,get_json_object(content, '$.dt') as dt, get_json_object(content, '$.edusubjectid') as edusubjectid," +
        "get_json_object(content, '$.edusubjectname') as edusubjectname,get_json_object(content, '$.majorid') as majorid, get_json_object(content, '$.majorname') as majorname," +
        "get_json_object(content, '$.money') as money,get_json_object(content, '$.pointlistid') as pointlistid, get_json_object(content, '$.status') as status," +
        "get_json_object(content, '$.teacherid') as teacherid,get_json_object(content, '$.teachername') as teachername" +
        " from sparktuning.tmp_salecourse  ")

    sale_course.write.mode(SaveMode.Overwrite).saveAsTable("sparktuning.sale_course") */

  }
}
