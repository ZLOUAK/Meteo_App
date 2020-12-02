package com.meteoweb.meteoanalyzer.hbasefileloader;

import com.meteoweb.meteoanalyzer.FileYear;
import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import au.com.bytecode.opencsv.CSVParser;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.Arrays;
import java.util.logging.Logger;

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
public class TempMapper extends Mapper <LongWritable, Text, ImmutableBytesWritable, Put>{
    CSVParser csvParser = new CSVParser();
    String tableName = "";
    ImmutableBytesWritable hKey = new ImmutableBytesWritable();
    Logger logger = Logger.getLogger(this.getClass().getName());
    static int amount = 0;

@Override
    protected void setup(Context context) {
        Configuration c = context.getConfiguration();
        tableName = c.get("hbase.table.name");
    }

    @Override
    public void map(LongWritable key, Text value, Context context) {

    String[] values;
    String[] stringValues;
    StringBuilder stringBuilder = new StringBuilder();

        try {
            values = csvParser.parseLine(value.toString());
            stringValues = Arrays.copyOf(values, values.length);
            if (value.getLength() == 0) {
                return;
            }
            String buildkey = String.valueOf(stringBuilder.append(stringValues[0]).append(stringValues[1]));
            if (stringValues[0] !=null)
            hKey.set(buildkey.getBytes());

            if (stringValues[1] !=null && !stringValues[1].equals("")) {

        if (stringValues[2] !=null && !stringValues[2].equals("")) {
          //  logger.info("amount of data is : "+amount++);

            if(stringValues[2].equals("PRCP")){
                Put HPut = new Put(hKey.get());
                HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_PERC.getColumnName(), stringValues[3].getBytes());
                context.write(hKey,HPut);
            }

           if(values[2].equals("TMAX")){
               Put HPut = new Put(hKey.get());
               HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_TMAX.getColumnName(), stringValues[3].getBytes());
               context.write(hKey,HPut);

               Put HPutYear = new Put(hKey.get());
               HPutYear.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                       HColumnEnum.SRV_COL_DATE.getColumnName(), FileYear.YEAR.getBytes());
               context.write(hKey,HPutYear);
           }

           if(values[2].equals("TMIN")){
               Put HPut = new Put(hKey.get());
               HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_TMIN.getColumnName(), stringValues[3].getBytes());
               context.write(hKey,HPut);
           }

           if(values[2].equals("TAVG")){
               Put HPut = new Put(hKey.get());
               HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                            HColumnEnum.SRV_COL_TAVG.getColumnName(), stringValues[3].getBytes());
               context.write(hKey,HPut);
           }

           if(values[2].equals("SNOW")){
               Put HPut = new Put(hKey.get());
               HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                            HColumnEnum.SRV_COL_SNOW.getColumnName(), stringValues[3].getBytes());
               context.write(hKey,HPut);
           }

           if(values[2].equals("SNWD")){
               Put HPut = new Put(hKey.get());
               HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                            HColumnEnum.SRV_COL_SNWD.getColumnName(), stringValues[3].getBytes());
               context.write(hKey,HPut);
           }

           if(values[2].equals("TOBS")){
               Put HPut = new Put(hKey.get());
               HPut.addColumn(HColumnEnum.SRV_COL_FAM1.getColumnName(),
                            HColumnEnum.SRV_COL_TOBS.getColumnName(), stringValues[3].getBytes());
               context.write(hKey,HPut);
           }

        }} }catch(Exception e){ e.printStackTrace();}

        }
}