package com.ws.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @Description SocketTextStreamWordCount
 * @Author Ws
 * @Date 2022-05-11 14:41
 * @Version V1.0
 */
public class SocketTextStreamWordCount {


    public static void main(String[] args) throws Exception {

        //设置环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //获取数据
        DataStream<String> text;

        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        try {
            final String host = parameterTool.get("host");

            final int port = Integer.parseInt(parameterTool.get("port"));

            text = env.socketTextStream(host, port);
        } catch (Exception e) {
            System.err.println("解析数据失败，采用默认链接");
            text = env.socketTextStream("127.0.0.1", 9003);
        }


        final SingleOutputStreamOperator<Tuple2<String, Integer>> counts = text.flatMap(new LineSplitter()).keyBy(data -> data.f0).sum(1);


        counts.print();

        env.execute("Java WordCount from SocketTextStream Example");


    }


}

final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
    @Override
    public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) {
        String[] tokens = s.toLowerCase().split(" ");

        for (String token : tokens) {
            if (token.length() > 0) {
                collector.collect(new Tuple2<>(token, 1));
            }
        }
    }
}