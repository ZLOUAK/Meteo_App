package com.meteoweb.meteoanalyzer.mysqlloaderimpl;

import com.meteoweb.domain.StationInformation;
import com.meteoweb.meteoanalyzer.HColumnEnum;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MySqlReducer extends TableReducer<Text, Result, Text> {

    public static final byte[] CFLOCATION = HColumnEnum.SRV_COL_FAM.getColumnName();
    public static final byte[] CFAGREGATION = HColumnEnum.SRV_COL_FAM2.getColumnName();
    public static final byte[] TMAX = HColumnEnum.SRV_COL_TMAX.getColumnName();
    public static final byte[] TMIN = HColumnEnum.SRV_COL_TMIN.getColumnName();
    public static final byte[] PERC = HColumnEnum.SRV_COL_PERC.getColumnName();
    public static final byte[] TAVG =  HColumnEnum.SRV_COL_TAVG.getColumnName();
    public static final byte[] SNOW =  HColumnEnum.SRV_COL_SNOW.getColumnName();
    public static final byte[] SNWD =  HColumnEnum.SRV_COL_SNWD.getColumnName();
    public static final byte[] TOBS =  HColumnEnum.SRV_COL_TOBS.getColumnName();
    public static final byte[] LG = HColumnEnum.SRV_COL_LONGITUDE.getColumnName();
    public static final byte[] LA = HColumnEnum.SRV_COL_LATITUDE.getColumnName();
    public static final byte[] EL = HColumnEnum.SRV_COL_ELEVATION.getColumnName();
    public static final byte[] CI = HColumnEnum.SRV_COL_CITY.getColumnName();
    CRUDOperations crudOperations = new CRUDOperations();

    public void reduce(Text key, Iterable<Result> values, Context context) throws InterruptedException, IOException {

        StationInformation stationInformation = new StationInformation();
        stationInformation.setId(key.toString().substring(0,11));
        stationInformation.setYear(key.toString().substring(11,15));
        stationInformation.setMonth(key.toString().substring(11,15)+"_"+key.toString().substring(15,17));



        for (Result val : values){
                    

                if(val.getValue(CFAGREGATION,TMAX)!=null) {

                    //TMAX
                    try  {

                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,TMAX));
                        stationInformation.setTmax(tmp);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFAGREGATION,TMIN)!=null) {

                    //TMIN
                    try  {

                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,TMIN));
                        stationInformation.setTmin(tmp);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFAGREGATION,TAVG)!=null) {
                    //TAVG
                    try  {

                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,TAVG));
                        stationInformation.setTavg(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFAGREGATION,SNOW)!=null) {
                    //SNOW
                    try  {

                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,SNOW));
                        stationInformation.setSnow(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFAGREGATION,SNWD)!=null) {
                    //SNDW
                    try  {

                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,SNWD));
                        stationInformation.setSnwd(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFAGREGATION,PERC)!=null) {
                    //PERC
                    try  {
                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,PERC));
                        stationInformation.setPerception(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFAGREGATION,TOBS)!=null) {
                    //TOBS
                    try  {
                        String tmp = Bytes.toString(val.getValue(CFAGREGATION,TOBS));
                        stationInformation.setTobs(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFLOCATION,LA)!=null) {
                    //LATITUDE
                    try  {
                        String tmp = Bytes.toString(val.getValue(CFLOCATION,LA));
                        stationInformation.setLatitude(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFLOCATION,LG)!=null) {
                    //LONGITUDE
                    try  {
                        String tmp = Bytes.toString(val.getValue(CFLOCATION,LG));
                        stationInformation.setLongitude(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFLOCATION,EL)!=null) {
                    //ELEVATION
                    try  {
                        String tmp = Bytes.toString(val.getValue(CFLOCATION,EL));
                        stationInformation.setElevation(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(val.getValue(CFLOCATION,CI)!=null) {
                    //CITY
                    try  {
                        String tmp = Bytes.toString(val.getValue(CFLOCATION,CI));
                        stationInformation.setCity(tmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        if(stationInformation.getTmax()!=null)
            crudOperations.insertEntity(stationInformation);
    }
}

