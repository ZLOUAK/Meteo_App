package com.meteoweb.domain;

import javax.persistence.Id;
import java.io.Serializable;

public class YearAgregationId implements Serializable {

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    String year;

}
