package com.meteoweb.meteoanalyzer.mysqlloaderimpl;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class MysqlConfiguration {


    static private HBaseConfiguration initialize() throws IOException {

        HBaseConfiguration config = new HBaseConfiguration();
        new GenericOptionsParser(config, null).getRemainingArgs();
        // Load hbase-site.xml
        config.set("hbase.zookeeper.quorum", "hbase");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.master", "hdfs://localhost:9000/");
        config.set("hbase.table.name", "Data_Cube");
        config.set("hbase.fs.tmp.dir","/home/Hbase/HFiles");
        config.set("hbase.loadincremental.validate.hfile","false");
        return config;
    }

    static Job job() throws IOException {

            HBaseConfiguration config = initialize();

            Job job = new Job(config,"MysqlLoader");
            job.setMapperClass(MySqlMapper.class);
            // class that contains mapper and reducer
            job.setReducerClass(MySqlReducer.class);

        return job;
    }
}