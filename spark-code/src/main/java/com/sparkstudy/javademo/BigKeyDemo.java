package com.sparkstudy.javademo;

/**
 * @author:wangchao
 * @version:1.0
 * @create: 2023-11-23 12:06
 * @Description:
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class BigKeyDemo {
    public static void main(String[] args) {
        // 创建SparkSession
        SparkSession spark = SparkSession.builder()
            .appName("SparkSqlBroadcastVariableDemo")
            .master("local[*]")
            .getOrCreate();
/*
        List<String> list=new ArrayList<>();

        for (int i = 10; i < 10000; i++) {
            Random random=new Random();
            list.add(random.nextInt(i)+"aa");
        }*/
        List<Person> persons = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        );
        Dataset<Person> dataFrame = spark.createDataset(persons, Encoders.bean(Person.class));
        dataFrame.createOrReplaceTempView("list");

        spark.sql("select name,age from list").show();

       /* Map<Integer,Integer> map=new HashMap<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Integer integer = list.get(i)%3;
            if(map.containsKey(integer)){
                Integer cnt = map.get(integer);
                map.put(integer,++cnt);
            }else {
                map.put(integer,1);
            }
        }

        for(Map.Entry<Integer,Integer> m:map.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
        }*/

    }
    static   class Person {
        private String name;
        private int age;

           public Person(String name, int age) {
               this.name = name;
               this.age = age;
           }
           // Getters, Setters, and Constructors omitted for brevity
    }
}