package com.meteoweb.meteoanalyzer.agregatorhbaseloader;

import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class hbaseMapper extends TableMapper<Text, Result> {

    public static final byte[] CF = HColumnEnum.SRV_COL_FAM1.getColumnName();
    public static final byte[] AVG = HColumnEnum.SRV_COL_TAVG.getColumnName();
    public static final byte[] TMIN = HColumnEnum.SRV_COL_TMIN.getColumnName();
    public static final byte[] TMAX = HColumnEnum.SRV_COL_TMAX.getColumnName();
    public static final byte[] SNOW  = HColumnEnum.SRV_COL_SNOW.getColumnName();
    public static final byte[] SNWD = HColumnEnum.SRV_COL_SNWD.getColumnName();
    public static final byte[] TOBS = HColumnEnum.SRV_COL_TOBS.getColumnName();
    public static final byte[] PERC = HColumnEnum.SRV_COL_PERC.getColumnName();


    Text hkey = new Text();
    KeyValue kv;

    @Override
    protected void setup(Mapper.Context context) {
    }
    private final Text text = new Text();
    public void map(ImmutableBytesWritable row, Result value, Context context) {

        if(value.getColumnCells(CF, TMAX).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,TMAX));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_TMAX.getColumnName(),val.getBytes());
                context.write(new Text(key),value);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    if(value.getColumnCells(CF, AVG).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,AVG));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_TAVG.getColumnName(),val.getBytes());
                context.write(new Text(key),value);

                  } catch (Exception e) {
                e.printStackTrace();
            }
    }

    if(value.getColumnCells(CF, TMIN).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,TMIN));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_TMIN.getColumnName(),val.getBytes());
                context.write(new Text(key),value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    if(value.getColumnCells(CF,PERC).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,PERC));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_PERC.getColumnName(),val.getBytes());
                context.write(new Text(key),value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    if(value.getColumnCells(CF,SNOW).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,SNOW));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_SNOW.getColumnName(),val.getBytes());
                context.write(new Text(key),value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    if(value.getColumnCells(CF,SNWD).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,SNWD));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_SNWD.getColumnName(),val.getBytes());
                context.write(new Text(key),value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    if(value.getColumnCells(CF,TOBS).size() !=0) {
            try  {
                String key = Bytes.toString(value.getRow()).substring(0,17);
                String val = Bytes.toString(value.getValue(CF,TOBS));
                kv = new KeyValue(key.getBytes(), HColumnEnum.SRV_COL_FAM1.getColumnName(),
                        HColumnEnum.SRV_COL_TOBS.getColumnName(),val.getBytes());
                context.write(new Text(key),value);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    }

}