package com.meteoweb.meteoanalyzer.agregatorhbaseloader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

public class hbaseMain {


    public static int runAggregatorHbaseLoader(String[] args) {
        int result = -1;
        try
        {
            result= ToolRunner.run(new Configuration(), new hbaseDriver(), args);
            if(0 == result)
            { System.out.println("Data_Cube...Agregation...Successfully");}
            else
            { System.out.println("Data_Cube...Agregation...Failed"); }
        }
        catch(Exception exception)
        { exception.printStackTrace();}

        return result;
    }

}
