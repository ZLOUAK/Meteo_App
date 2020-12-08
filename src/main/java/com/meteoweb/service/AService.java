package com.meteoweb.service;

import com.meteoweb.domain.StationInformation;
import com.meteoweb.domain.YearAgregation;
import com.meteoweb.meteoanalyzer.FileYear;
import com.meteoweb.repository.StationInformationRepository;
import com.meteoweb.repository.YearAgregationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AService {

    @Autowired
    private StationInformationRepository repository;

    @Autowired
    private YearAgregationRepo yearAgregationRepo;

    public void yearAgregation(){

        Map<String, List<StationInformation>> sis= repository.findByYear(FileYear.YEAR).stream()
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
                    .reduce(Integer::max).get();

            int sMax = tmp.stream()
                    .map(e->Integer.parseInt(e.getTmax()))
                    .reduce(Integer::sum).get();

            int tMin = tmp.stream()
                    .map(e->Integer.parseInt(e.getTmin()))
                    .reduce(Integer::min).get();

            int sMin = tmp.stream()
                    .map(e->Integer.parseInt(e.getTmin()))
                    .reduce(Integer::sum).get();

            int tAvg = tmp.stream()
                    .map(e->Integer.parseInt(e.getTavg()))
                    .reduce(Integer::sum).get();

            yearAgregation.setId(id);
            yearAgregation.setTmax(String.valueOf(tMax));
            yearAgregation.setTmin(String.valueOf(tMin));
            yearAgregation.setTavg(String.valueOf(tAvg/12));

            yearAgregation.setSummax(String.valueOf(sMax));
            yearAgregation.setSummin(String.valueOf(sMin));
            yearAgregation.setSumavg(String.valueOf(tAvg));

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

            if (sis.get(id).get(0).getYear()!=null)
                yearAgregation.setYear(String.valueOf(sis.get(id).get(0).getYear().substring(0,4)));

            yearAgregationRepo.save(yearAgregation);

        }

        System.out.println("Job Finished");
    }


    private StationInformation substringId(StationInformation stationInformation){

       StationInformation si = new StationInformation();
       si.setId(stationInformation.getId().substring(0,11));
       si.setTmax(stationInformation.getTmax());
       si.setYear(stationInformation.getYear());
       si.setMonth(stationInformation.getMonth());
       si.setTmin(stationInformation.getTmin());
       si.setTavg(stationInformation.getTavg());
       si.setCity(stationInformation.getCity());
       si.setLatitude(stationInformation.getLatitude());
       si.setLongitude(stationInformation.getLongitude());
       si.setElevation(stationInformation.getElevation());
       si.setTobs(stationInformation.getTobs());
       si.setSnow(stationInformation.getSnow());
       si.setSnwd(stationInformation.getSnwd());
       si.setPerception(stationInformation.getPerception());

        return si;
    }

}
