package com.sparkstudy.javademo;

/**
 * @author:wangchao
 * @version:1.0
 * @create: 2023-11-23 12:06
 * @Description:
 */
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.api.java.*;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.*;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.reflect.ClassTag$;

public class SparkSqlBroadcastVariableDemo {
    public static void main(String[] args) {
        // 创建SparkSession
        SparkSession spark = SparkSession.builder()
            .appName("SparkSqlBroadcastVariableDemo")
            .master("local[*]")
            .getOrCreate();

        // 创建一个较小的DataFrame，并将其作为广播变量
        Dataset<Row> smallerDataFrame = spark.createDataFrame(
            Arrays.asList(
                new JavaBean("A", 1),
                new JavaBean("B", 2),
                new JavaBean("C", 3)
            ),
            JavaBean.class
        );
        Broadcast<Dataset<Row>> broadcastVar = spark.sparkContext().broadcast(smallerDataFrame, ClassTag$.MODULE$.<Dataset<Row>>apply(Dataset.class));

        // 创建一个较大的DataFrame
        Dataset<Row> largerDataFrame = spark.createDataFrame(
            Arrays.asList(
                new JavaBean("A", "apple"),
                new JavaBean("B", "banana"),
                new JavaBean("C", "cherry"),
                new JavaBean("D", "durian")
            ),
            JavaBean.class
        );

        List<String> javaList = Arrays.asList("letter", "numberOrFruit");
        Seq<String> scalaSeq = JavaConverters.asScalaBufferConverter(javaList).asScala().toSeq();


        // 使用广播变量进行Join操作
        Dataset<Row> result = largerDataFrame.join(broadcastVar.value(), scalaSeq);

        // 显示结果
        result.show();

        // 关闭SparkSession
        spark.close();
    }

    public static class JavaBean implements Serializable {
        private String letter;
        private Object numberOrFruit;

        public JavaBean(String letter, Object numberOrFruit) {
            this.letter = letter;
            this.numberOrFruit = numberOrFruit;
        }

        public String getLetter() {
            return letter;
        }

        public Object getNumberOrFruit() {
            return numberOrFruit;
        }
    }
}