package com.meteoweb.meteoanalyzer.hbasefileloader;

import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import au.com.bytecode.opencsv.CSVParser;
import java.io.IOException;

/**
 * HBase bulk import example
 * <p>
 * Parses Facebook and Twitter messages from CSV files and outputs
 * <ImmutableBytesWritable, KeyValue>.
 * <p>
 * The ImmutableBytesWritable key is used by the TotalOrderPartitioner to map it
 * into the correct HBase table region.
 * <p>
 * The KeyValue value holds the HBase mutation information (column family,
 * column, and value)
 */
public class HBaseKVMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, KeyValue> {

    CSVParser csvParser = new CSVParser();
    String tableName = "";
    ImmutableBytesWritable hKey = new ImmutableBytesWritable();
    KeyValue kv;

    @Override
    protected void setup(Context context) {
        Configuration c = context.getConfiguration();
        tableName = c.get("hbase.table.name");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) {
        String[] lines;
        try {
            lines = csvParser.parseLine(value.toString());
            String[] values=lines[0].toString().split("\\s+");

                int i = 0;
                if (!values[0].equals("")) {
                    hKey.set(values[0].getBytes());

                }
                if (!values[1].equals("")) {
                   kv = new KeyValue(hKey.get(), HColumnEnum.SRV_COL_FAM.getColumnName(),
                        HColumnEnum.SRV_COL_LATITUDE.getColumnName(), values[1].getBytes());
                        context.write(hKey, kv);
                }
                if (!values[2].equals("")) {
                        kv = new KeyValue(hKey.get(), HColumnEnum.SRV_COL_FAM.getColumnName(),
                                HColumnEnum.SRV_COL_LONGITUDE.getColumnName(), values[2].getBytes());
                        context.write(hKey, kv);
                }

            if (!values[3].equals("") && !values[3].equals("-999.9")) {
                kv = new KeyValue(hKey.get(), HColumnEnum.SRV_COL_FAM.getColumnName(),
                        HColumnEnum.SRV_COL_ELEVATION.getColumnName(), values[3].getBytes());
                context.write(hKey, kv);
                }

            if (!values[4].equals("")  && values.length > 5 ){
                try {
                    kv = new KeyValue(hKey.get(), HColumnEnum.SRV_COL_FAM.getColumnName(),
                            HColumnEnum.SRV_COL_CITY.getColumnName(), (values[4]+" "+values[5]).getBytes());
                    context.write(hKey, kv);
                }catch (Exception e){  e.printStackTrace(); }

            }else {
                kv = new KeyValue(hKey.get(), HColumnEnum.SRV_COL_FAM.getColumnName(),
                        HColumnEnum.SRV_COL_CITY.getColumnName(),values[4].getBytes());
                context.write(hKey, kv);
                }

            } catch(InterruptedException | IOException e){
            e.printStackTrace();
        }
    }
}