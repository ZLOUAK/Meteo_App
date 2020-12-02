package com.meteoweb.service;

import com.meteoweb.domain.StationInformation;
import com.meteoweb.domain.YearAgregation;
import com.meteoweb.repository.StationInformationRepository;
import com.meteoweb.repository.YearAgregationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AService {

    @Autowired
    private StationInformationRepository repository;

    @Autowired
    private YearAgregationRepo yearAgregationRepo;

    public void yearAgregation(){

        Map<String, List<StationInformation>> sis= repository.findAll().stream()
                .map(e->substringId(e))
        .collect(Collectors.groupingBy(
                StationInformation::getId
        ));

        List<YearAgregation> yearAgregations = new ArrayList<YearAgregation>();

        for (String id : sis.keySet()) {

            List<StationInformation> tmp = sis.get(id);

            YearAgregation yearAgregation = new YearAgregation();

            int tMax = tmp.stream()
                    .map(e->Integer.parseInt(e.getTmax()))
                    .reduce(Integer::sum).get();

            int tMin = tmp.stream()
                    .map(e->Integer.parseInt(e.getTmin()))
                    .reduce(Integer::sum).get();

            int tAvg = tmp.stream()
                    .map(e->Integer.parseInt(e.getTavg()))
                    .reduce(Integer::sum).get();

            yearAgregation.setId(id);
            yearAgregation.setTmax(String.valueOf(tMax/12));
            yearAgregation.setTmin(String.valueOf(tMin/12));
            yearAgregation.setTavg(String.valueOf(tAvg/12));

            if (sis.get(id).get(0).getCity()!=null)
                yearAgregation.setCity(String.valueOf(sis.get(id).get(0).getCity()));

            if (sis.get(id).get(0).getLatitude()!=null)
                yearAgregation.setLatitude(String.valueOf(sis.get(id).get(0).getLatitude()));

            if (sis.get(id).get(0).getLongitude()!=null)
                yearAgregation.setLongitude(String.valueOf(sis.get(id).get(0).getLongitude()));

            if (sis.get(id).get(0).getElevation()!=null)
                yearAgregation.setElevation(String.valueOf(sis.get(id).get(0).getElevation()));

            if (sis.get(id).get(0).getPerception()!=null)
                yearAgregation.setPerception(String.valueOf(sis.get(id).get(0).getPerception()));

            if (sis.get(id).get(0).getSnow()!=null)
                yearAgregation.setSnow(String.valueOf(sis.get(id).get(0).getSnow()));

            if (sis.get(id).get(0).getSnwd()!=null)
                yearAgregation.setSnwd(String.valueOf(sis.get(id).get(0).getSnwd()));

            if (sis.get(id).get(0).getTobs()!=null)
                yearAgregation.setTobs(String.valueOf(sis.get(id).get(0).getTobs()));

            if (sis.get(id).get(0).getDate()!=null)
                yearAgregation.setDate(String.valueOf(sis.get(id).get(0).getDate().substring(0,4)));

            System.out.println(yearAgregation);
            //yearAgregationRepo.save(yearAgregation);



        }

        System.out.println("Job Finished");
    }


    private StationInformation substringId(StationInformation stationInformation){
        stationInformation.setId(
                stationInformation.getId().substring(0,11)
        );

        return stationInformation;
    }

}
