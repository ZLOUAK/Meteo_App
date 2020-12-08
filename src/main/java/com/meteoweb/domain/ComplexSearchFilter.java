package com.meteoweb.domain;

public class ComplexSearchFilter {

    String dimension1;

    String dimension2;

    String dimension3;

    public String getDimension3() {
        return dimension3;
    }

    public void setDimension3(String dimension3) {
        this.dimension3 = dimension3;
    }

    public ComplexSearchFilter() {
    }

    public ComplexSearchFilter(String dimension1, String dimension2, String filter, String agregation,String dimension3) {
        this.dimension1 = dimension1;
        this.dimension2 = dimension2;
        this.filter = filter;
        this.agregation = agregation;
        this.dimension3 = dimension3;
    }

    String filter;
    String agregation;

    public String getDimension1() {
        return dimension1;
    }

    public void setDimension1(String dimension1) {
        this.dimension1 = dimension1;
    }

    public String getDimension2() {
        return dimension2;
    }

    public void setDimension2(String dimension2) {
        this.dimension2 = dimension2;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getAgregation() {
        return agregation;
    }

    public void setAgregation(String agregation) {
        this.agregation = agregation;
    }

}
