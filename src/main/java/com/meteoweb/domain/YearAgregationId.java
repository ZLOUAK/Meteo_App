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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

}
