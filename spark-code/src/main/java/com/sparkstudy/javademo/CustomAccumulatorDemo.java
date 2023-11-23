package com.sparkstudy.javademo;

/**
 * @author:wangchao
 * @version:1.0
 * @create: 2023-11-23 12:07
 * @Description:
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.AccumulatorV2;

public class CustomAccumulatorDemo {
    public static void main(String[] args) {
        //环境准备
        SparkConf sparkConf = new SparkConf().setAppName("java word count").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        //List<String> strings = Arrays.asList("hello java", "hello world", "hello spark");
        List<String> strings = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 6000000; i++) {
            int salt = random.nextInt(3000000);
            strings.add("asd"+salt+"fdksl");
        }

        JavaRDD<String> javaRDD = jsc.parallelize(strings);
        //创建累加器
        MyAccumulator myAccumulator = new MyAccumulator();
        //注册累加器
        jsc.sc().register(myAccumulator,"my");
        javaRDD.foreach(datas -> {
            //使用累加器
            myAccumulator.add(datas);
        });
        //获取结果
        Map<String, Integer> value = myAccumulator.value();
        value.forEach((k,v) -> {
            System.out.println(k + ":" + v);
        });
        while (true){}

    }
}

class MyAccumulator extends AccumulatorV2<String, Map<String, Integer>>{

    //定义输出类型变量
    private Map<String ,Integer> output = new HashMap<>();

    //是否为初始状态
    @Override
    public boolean isZero() {
        return this.output.isEmpty();
    }

    //复制累加器
    @Override
    public AccumulatorV2<String, Map<String, Integer>> copy() {
        MyAccumulator myAccumulator = new MyAccumulator();
        //将此累加器中的数据赋值给新创建的累加器
        myAccumulator.output = this.output;
        return myAccumulator;
    }

    //重置累加器
    @Override
    public void reset() {
        this.output.clear();
    }

    //累加器添加元素
    @Override
    public void add(String v) {
        String[] split = v.split(" ");
        for (String s : split){
            //存在则加一，不存在则为一
            int value = this.output.getOrDefault(s, 0) + 1;
            this.output.put(s,value);
        }
    }

    //合并累加器元素
    @Override
    public void merge(AccumulatorV2<String, Map<String, Integer>> other) {
        other.value().forEach((k,v) -> {
            if (this.output.containsKey(k)){
                Integer i1 = this.output.get(k);
                Integer i2 = other.value().get(k);
                this.output.put(k,i1+i2);
            }else {
                this.output.put(k,v);
            }
        });
    }

    //输出
    @Override
    public Map<String, Integer> value() {
        return this.output;
    }
}