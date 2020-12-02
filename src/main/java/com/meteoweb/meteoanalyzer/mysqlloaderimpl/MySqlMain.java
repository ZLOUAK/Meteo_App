package com.meteoweb.meteoanalyzer.mysqlloaderimpl;

import org.apache.hadoop.util.ToolRunner;


public class MySqlMain {


    public static int runMysqlMR(String[] args) {
        int result = -1;
        try
        {
            result= ToolRunner.run(new MySqlDriver(), args);

            if(0 == result)
            { System.out.println("Data_Cube...ToMysql...Successfully");}
            else
            { System.out.println("Data_Cube...ToMysql...Failed"); }
        }
        catch(Exception exception)
        { exception.printStackTrace(); }
        return result;
    }

}
