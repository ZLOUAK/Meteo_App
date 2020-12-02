package com.meteoweb.meteoanalyzer.mysqlloaderimpl;

import com.meteoweb.meteoanalyzer.FileYear;
import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

public class MySqlMapper extends TableMapper<Text, Result> {

    public static final byte[] CF = HColumnEnum.SRV_COL_FAM.getColumnName();
    public static final byte[] LG = HColumnEnum.SRV_COL_LONGITUDE.getColumnName();
    public static final byte[] LA = HColumnEnum.SRV_COL_LATITUDE.getColumnName();
    public static final byte[] EL = HColumnEnum.SRV_COL_ELEVATION.getColumnName();
    public static final byte[] CI = HColumnEnum.SRV_COL_CITY.getColumnName();
    public static String YEAR = FileYear.YEAR;


    @Override
    protected void setup(Context context) {

    }
    public void map(ImmutableBytesWritable row, Result value, Context context) {

        if(     value.getColumnCells(CF,LG).size()==0 &&
                value.getColumnCells(CF,LA).size()==0 &&
                value.getColumnCells(CF,EL).size()==0 &&
                value.getColumnCells(CF,CI).size()==0)

        {

            try  {
                String key = Bytes.toString(value.getRow());
                context.write(new Text(key),value);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else{
            try  {

                for (int i=1;i<=9;i++){
                    String key = Bytes.toString(value.getRow())+YEAR+"0"+i;
                    context.write(new Text(key),value);
                }
                String key = Bytes.toString(value.getRow())+YEAR+"10";
                context.write(new Text(key),value);

                String key1 = Bytes.toString(value.getRow())+YEAR+"11";
                context.write(new Text(key1),value);

                String key2 = Bytes.toString(value.getRow())+YEAR+"12";
                context.write(new Text(key2),value);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}