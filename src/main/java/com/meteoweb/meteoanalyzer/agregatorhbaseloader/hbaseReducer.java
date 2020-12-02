package com.meteoweb.meteoanalyzer.agregatorhbaseloader;

import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import java.io.IOException;


public class hbaseReducer extends TableReducer<Text, Result, Text> {

    public static final byte[] CF = HColumnEnum.SRV_COL_FAM2.getColumnName();
    public static final byte[] CFSTEMPERATURE = HColumnEnum.SRV_COL_FAM1.getColumnName();
    public static final byte[] TMAX = HColumnEnum.SRV_COL_TMAX.getColumnName();
    public static final byte[] TMIN = HColumnEnum.SRV_COL_TMIN.getColumnName();
    public static final byte[] PERC = HColumnEnum.SRV_COL_PERC.getColumnName();
    public static final byte[] TAVG =  HColumnEnum.SRV_COL_TAVG.getColumnName();
    public static final byte[] SNOW =  HColumnEnum.SRV_COL_SNOW.getColumnName();
    public static final byte[] SNWD =  HColumnEnum.SRV_COL_SNWD.getColumnName();
    public static final byte[] TOBS =  HColumnEnum.SRV_COL_TOBS.getColumnName();


    public void reduce(Text key, Iterable<Result> values, Context context) throws InterruptedException, IOException {

           if(key.getLength()!=0)
            {int i = 0;

            int tMax = Integer.MIN_VALUE;
            int tMin = Integer.MAX_VALUE;
            int tAvg = 0;
            int snow = 0;
            int sNwd = 0;
            int tObs = 0;
            int pErc = 0;

            for (Result val : values){
                    

                if(val.getValue(CFSTEMPERATURE,TMAX)!=null) {

                    //TMAX
                    try  {

                        int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,TMAX)));
                        if(tmp>tMax)
                            tMax = tmp;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFSTEMPERATURE,TMIN)!=null) {

                    //TMIN
                    try  {

                        int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,TMIN)));
                        if(tmp<tMin)
                            tMin = tmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFSTEMPERATURE,TAVG)!=null) {
                    //TAVG
                    try  {
                    int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,TAVG)));
                    tAvg = +tmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFSTEMPERATURE,SNOW)!=null) {
                    //SNOW
                    try  {
                        int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,SNOW)));
                        snow = +tmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFSTEMPERATURE,SNWD)!=null) {
                    //SNDW
                    try  {
                        int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,SNWD)));
                        sNwd = +tmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFSTEMPERATURE,PERC)!=null) {
                    //PERC
                    try  {
                        int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,PERC)));
                        pErc = +tmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFSTEMPERATURE,TOBS)!=null) {
                    //TOBS
                    try  {
                        int tmp = Integer.parseInt(Bytes.toString(val.getValue(CFSTEMPERATURE,TOBS)));
                        tObs = +tmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            Put putMax = new Put(key.getBytes());
            if(tMax!=Integer.MIN_VALUE){
                putMax.addColumn(CF, TMAX,String.valueOf(tMax).getBytes());
            }else
                putMax.addColumn(CF, TMAX,String.valueOf(0).getBytes());
            if (key!=null) {
                    context.write(null,putMax);
            }

            Put putMin = new Put(key.getBytes());
            if(tMin!=Integer.MAX_VALUE){
               putMin.addColumn(CF, TMIN,String.valueOf(tMin).getBytes());
            }else
                    putMin.addColumn(CF, TMIN,String.valueOf(0).getBytes());
            if (key!=null) {
                    context.write(null,putMin);
            }

            Put putavg = new Put(key.getBytes());
            putavg.addColumn(CF, TAVG,String.valueOf(tAvg/30).getBytes());
            if (key!=null) {
               context.write(null,putavg);
            }

            Put putSnow = new Put(key.getBytes());
            putSnow.addColumn(CF, SNOW,String.valueOf(snow/30).getBytes());
            if (key!=null) {
              context.write(null,putSnow);
            }

            Put putSnwd = new Put(key.getBytes());
            putSnwd.addColumn(CF, SNWD,String.valueOf(sNwd/30).getBytes());
            if (key!=null) {
              context.write(null,putSnwd);
            }

            Put putPerc = new Put(key.getBytes());
            putPerc.addColumn(CF, PERC,String.valueOf(pErc/30).getBytes());
            if (key!=null) {
              context.write(null,putPerc);
            }

            Put putobs = new Put(key.getBytes());
            putobs.addColumn(CF, TOBS,String.valueOf(tObs/30).getBytes());
            if (key!=null) {
             context.write(null,putobs);
            }

            }


          }
        }
