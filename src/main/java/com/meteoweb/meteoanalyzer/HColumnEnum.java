package com.meteoweb.meteoanalyzer;

public enum HColumnEnum {
    SRV_COL_FAM ("SLocation".getBytes()),
    SRV_COL_FAM1 ("STemperature".getBytes()),
    SRV_COL_FAM2 ("SAgregation".getBytes()),
    SRV_COL_LONGITUDE ("longitude".getBytes()),
    SRV_COL_LATITUDE ("latitude".getBytes()),
    SRV_COL_ELEVATION ("elevation".getBytes()),
    SRV_COL_CITY ("city".getBytes()),
    SRV_COL_PERC ("Perception".getBytes()),
    SRV_COL_TMAX ("TMAX".getBytes()),
    SRV_COL_TMIN ("TMIN".getBytes()),
    SRV_COL_TAVG ("TAVG".getBytes()),
    SRV_COL_SNWD ("SNDW".getBytes()),
    SRV_COL_TOBS ("TOBS".getBytes()),
    SRV_COL_SNOW ("SNOW".getBytes()),
    SRV_COL_DATE ("DATE".getBytes());

    private final byte[] columnName;

    HColumnEnum (byte[] column) {
        this.columnName = column;
    }

    public byte[] getColumnName() {
        return this.columnName;
    }
}