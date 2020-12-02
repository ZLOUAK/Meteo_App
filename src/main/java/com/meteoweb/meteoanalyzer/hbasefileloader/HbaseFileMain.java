package com.meteoweb.meteoanalyzer.hbasefileloader;

import com.meteoweb.meteoanalyzer.FileYear;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;

public class HbaseFileMain {

    private final static String ROOTPATH = "/home/hadoop/Temperature/";

    public static void getFile(String year) throws IOException {

        URL website = new URL("https://www1.ncdc.noaa.gov/pub/data/ghcn/daily/by_year/"+year+".csv.gz");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(year+".csv.gz");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);


        { try (GZIPInputStream gis = new GZIPInputStream(
                    new FileInputStream(new File(year+".csv.gz").toPath().toFile()))) {

                Files.copy(gis,new File(ROOTPATH+year+".csv").toPath());

            } catch (
                    FileAlreadyExistsException e) {
            }
        }
    }

    public static int runHeFileLoader(String year) throws IOException {

        FileYear.YEAR = year;
        getFile(year);

        int result=-1;
        try
        {
            result= ToolRunner.run(new Configuration(), new HbaseFilesLoaderDriver(), null);
            if(0 == result)
            { System.out.println("Successfully...Data_Cube...Processing");}
            else
            { System.out.println("Failed...Data_Cube...Processing"); }
        }
        catch(Exception exception)
        { exception.printStackTrace();}

        return result;
    }

}