package com.meteoweb.meteoanalyzer.mysqlloaderimpl;

import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;

public class MySqlDriver extends Configured implements Tool
{


    @Override
    public int run(String[] args) throws Exception {
        int status =-1;


        Scan scan = new Scan();
        scan.addFamily(HColumnEnum.SRV_COL_FAM2.getColumnName());
        scan.addFamily(HColumnEnum.SRV_COL_FAM.getColumnName());
        // scan.setCaching(500);
        scan.setRaw(true);
// 1 is the default in Scan, which will be bad for MapReduce jobs
       // scan.setCacheBlocks(false);
// don't set to true for MR jobs
// set other scan attrs

        Job job = MysqlConfiguration.job();

        TableMapReduceUtil.initTableMapperJob(
                job.getConfiguration().get("hbase.table.name"),
                // input table
                scan,
                // Scan instance to control CF and attribute selection
                MySqlMapper.class,
                // mapper class
                Text.class,
                // mapper output key
                Result.class,
                // mapper output value
                job);

        TableMapReduceUtil.initTableReducerJob(
                job.getConfiguration().get("hbase.table.name"),
                // output table
                MySqlReducer.class,
                // reducer class
                job);
 // at least one, adjust as required
         job.waitForCompletion(true);
         status= job.isSuccessful() ? 0 : -1;

        return status;
    }


}
