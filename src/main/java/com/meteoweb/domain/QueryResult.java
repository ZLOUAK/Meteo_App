package com.meteoweb.domain;

public class QueryResult {

    String col;

    String row;

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    String val;

    public QueryResult(String row,String col, String val) {
        this.col = col;
        this.row = row;
        this.val = val;
    }
}
