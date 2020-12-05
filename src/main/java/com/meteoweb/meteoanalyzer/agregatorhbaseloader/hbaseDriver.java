package com.meteoweb.meteoanalyzer.agregatorhbaseloader;

import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class hbaseDriver extends Configured implements Tool
{
    @Override
    public int run(String[] args) throws Exception {
        int status =-1;
        HBaseConfiguration config = new HBaseConfiguration();
        new GenericOptionsParser(config, args).getRemainingArgs();
        // Load hbase-site.xml
        config.set("hbase.zookeeper.quorum", "hbase");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.master", "hdfs://localhost:9000/");
        config.set("hbase.table.name", "Data_Cube");
        config.set("StationFamily", "SLocation");
        config.set("StationFamily1","STemperature");
        config.set("hbase.fs.tmp.dir","/home/Hbase/HFiles");
        config.set("hbase.loadincremental.validate.hfile","false");

        Job job = new Job(config,"ExampleSummary");

        job.setMapperClass(hbaseMapper.class);
        // class that contains mapper and reducer
        job.setReducerClass(hbaseReducer.class);
        Scan scan = new Scan();
        scan.addFamily(HColumnEnum.SRV_COL_FAM1.getColumnName());
       // scan.setCaching(500);
        scan.setRaw(true);
// 1 is the default in Scan, which will be bad for MapReduce jobs
       // scan.setCacheBlocks(false);
// don't set to true for MR jobs
// set other scan attrs

        TableMapReduceUtil.initTableMapperJob(
                config.get("hbase.table.name"),
                // input table
                scan,
                // Scan instance to control CF and attribute selection
                hbaseMapper.class,
                // mapper class
                Text.class,
                // mapper output key
                Result.class,
                // mapper output value
                job);
        TableMapReduceUtil.initTableReducerJob(
                config.get("hbase.table.name"),
                // output table
                hbaseReducer.class,
                // reducer class
                job);
 // at least one, adjust as required

         status= job.waitForCompletion(true) ? 0 : -1;

        return status;
    }

}
