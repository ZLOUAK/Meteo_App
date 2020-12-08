package com.meteoweb.service;

import com.meteoweb.domain.ComplexSearchFilter;
import com.meteoweb.domain.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComplexFilterService {

    @Autowired
    private EntityManager em;

    public List<QueryResult> complexSearch(ComplexSearchFilter searchFilter){

        StringBuilder customQuery = new StringBuilder();
        customQuery.append("SELECT e.")
                .append(searchFilter.getDimension1())
                .append(",e.")
                .append(searchFilter.getDimension2())
                .append(",e.");

        if (searchFilter.getDimension3()!= null){
            if(searchFilter.getAgregation().equals("tmax"))
                customQuery.append("summax");
            if(searchFilter.getAgregation().equals("tmin"))
                customQuery.append("summin");
            if(searchFilter.getAgregation().equals("tavg"))
                customQuery.append("sumavg");
        }else {
            customQuery.append(searchFilter.getAgregation());

        }

        customQuery.append(" FROM YearAgregation e");

        Query q = em.createNativeQuery(customQuery.toString());

        List<Object[]> results = q.getResultList();

        return doMapping(results);
    }

    public List<QueryResult> complexSearchFromStationInfo(ComplexSearchFilter searchFilter){

        StringBuilder customQuery = new StringBuilder();
        customQuery.append("SELECT e.")
                .append(searchFilter.getDimension1())
                .append(",e.")
                .append(searchFilter.getDimension2())
                .append(",e.")
                .append(searchFilter.getAgregation())
                .append(" FROM StationInformation e");

        Query q = em.createNativeQuery(customQuery.toString());

        List<Object[]> results = q.getResultList();

        return doMapping(results);
    }

    private List<QueryResult> doMapping(List<Object[]> results){

        List<QueryResult> queryResults = new ArrayList<QueryResult>();

        for (Object[] obj : results){

            queryResults.
                    add(new QueryResult(
                            (String)obj[0],
                            (String)obj[1],
                            (String)obj[2]
                    ));

        }


        return queryResults;
    }

}
