package com.meteoweb.meteoanalyzer.hbasefileloader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 Bulk import example<br>
 * <li>args[0]: HDFS input path
 * <li>args[1]: HDFS output path
 * <li>args[2]: HBase table name
 */
public class HbaseFilesLoaderDriver extends Configured implements Tool
{
    @Override
    public int run(String[] args) throws Exception {
        int status = -1,status1 =-1;
        HBaseConfiguration config = new HBaseConfiguration();
        new GenericOptionsParser(config, args).getRemainingArgs();
        // Load hbase-site.xml
        config.set("hbase.zookeeper.quorum", "hbase");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.master", "hdfs://localhost:9000/");
        config.set("hbase.table.name", "Data_Cube");
        config.set("StationFamily", "SLocation");
        config.set("StationFamily1","STemperature");
        config.set("StationFamily2","SAgregation");
        config.set("hbase.fs.tmp.dir","/home/Hbase/HFiles");
        config.set("hbase.loadincremental.validate.hfile","false");
        HBaseAdmin.available(config);
        Job job = Job.getInstance(config);
        job.setMapperClass(HBaseKVMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(KeyValue.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);

        Job job1 = Job.getInstance(config);
        job1.setNumReduceTasks(0);
        job1.setMapperClass(TempMapper.class);
        job1.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job1.setMapOutputValueClass(Put.class);
        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);
// settings and Hbase connexion
        Connection connection = ConnectionFactory.createConnection(config);
        Table table = connection.getTable(TableName.valueOf(config.get("hbase.table.name")));
        Admin admin = connection.getAdmin();
        RegionLocator regionLocator = connection.getRegionLocator(TableName.valueOf(config.get("hbase.table.name").getBytes()));
        boolean result1;
        try {
            result1 = admin.tableExists(TableName.valueOf(config.get("hbase.table.name")));
            if (result1) {
                System.out.println("Table Exist" + TableName.valueOf(config.get("hbase.table.name")));
                admin.disableTable(TableName.valueOf(config.get("hbase.table.name")));
                admin.deleteTable(TableName.valueOf(config.get("hbase.table.name")));
                HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(config.get("hbase.table.name")));
                descriptor.addFamily(new HColumnDescriptor(config.get("StationFamily")));
                descriptor.addFamily(new HColumnDescriptor(config.get("StationFamily1")));
                descriptor.addFamily(new HColumnDescriptor(config.get("StationFamily2")));
                // descriptor.(HTableDescriptor.)
                admin.createTable(descriptor);
                System.out.println("AFTER DELETE ==> Table Created" + TableName.valueOf(config.get("hbase.table.name")));
            } else {
                HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(config.get("hbase.table.name")));
                descriptor.addFamily(new HColumnDescriptor(config.get("StationFamily")));
                descriptor.addFamily(new HColumnDescriptor(config.get("StationFamily1")));
                descriptor.addFamily(new HColumnDescriptor(config.get("StationFamily2")));

                // descriptor.(HTableDescriptor.)
                admin.createTable(descriptor);
                System.out.println("Table Created" + TableName.valueOf(config.get("hbase.table.name")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
// Instantiating HTable class
        try {
//input & output paths
            Path dataPath1 = new Path("/home/hadoop/Stations");
            FileInputFormat.addInputPath(job, dataPath1);
            Path dataPath = new Path("/home/hadoop/Temperature");
            FileInputFormat.addInputPath(job1, dataPath);
            Path hfilePath = new Path("/home/hadoop/output/");
            FileOutputFormat.setOutputPath(job,hfilePath);
            Path hfilePath1 = new Path("/home/hadoop/output1/");
            FileOutputFormat.setOutputPath(job1,hfilePath1);

// Auto configure partitioner and reducer

            HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator);
            HFileOutputFormat2.setOutputPath(job, hfilePath);
            HFileOutputFormat2.setCompressOutput(job, true);
            HFileOutputFormat2.setOutputCompressorClass(job, SnappyCodec.class);

            HFileOutputFormat2.configureIncrementalLoad(job1, table, regionLocator);
            HFileOutputFormat2.setOutputPath(job1, hfilePath1);
            HFileOutputFormat2.setCompressOutput(job1, true);
            HFileOutputFormat2.setOutputCompressorClass(job1, SnappyCodec.class);

// change permissions so that HBase user can read it
            FileSystem fs =  FileSystem.get(config);
            FsPermission changedPermission=new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL);
            if(!fs.exists(hfilePath))
                fs.create(hfilePath);
            fs.setPermission(hfilePath, changedPermission);
            List<String> files = getAllFilePath(hfilePath, fs);
            for (String file : files){
                fs.setPermission(new Path(file), changedPermission);
                System.out.println("Changing permission for file " + file);
            }
            if(!fs.exists(hfilePath1))
                fs.create(hfilePath1);
            fs.setPermission(hfilePath1, changedPermission);
            List<String> files1 = getAllFilePath(hfilePath1, fs);
            for (String file : files1) {
                fs.setPermission(new Path(file), changedPermission);
                System.out.println("Changing permission for file " + file);
            }
//bulk load hbase files Importing the generated HFiles into a HBase table=
            LoadIncrementalHFiles loader = new LoadIncrementalHFiles(config);
            LoadIncrementalHFiles loaderLocation = new LoadIncrementalHFiles(config);

            FileSystem.get(config).delete(hfilePath, true);
            job.waitForCompletion(true);
            status = job.isSuccessful() ? 0 : -1;
            loaderLocation.doBulkLoad(hfilePath,admin , table,regionLocator);
//delete the hfiles


            FileSystem.get(config).delete(hfilePath1, true);
            job1.waitForCompletion(true);
            status1 = job1.isSuccessful() ? 0 : -1;
            loader.doBulkLoad(hfilePath1,admin , table,regionLocator);
//delete the hfiles


            return status ==0 ? status1 :-1;
        } finally {
            table.close();
            connection.close();
        }
    }


    public static List<String> getAllFilePath(Path filePath, FileSystem fs) throws IOException {
        List<String> fileList = new ArrayList<>();
        FileStatus[] fileStatus = fs.listStatus(filePath);
        for (FileStatus fileStat : fileStatus) {
            if (fileStat.isDirectory()) {
                fileList.add(fileStat.getPath().toString());
                fileList.addAll(getAllFilePath(fileStat.getPath(), fs));
            } else {
                fileList.add(fileStat.getPath().toString());
            }
        }
        return fileList;
    }
}