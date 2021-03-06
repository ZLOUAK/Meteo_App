package com.meteoweb.web;

import com.meteoweb.domain.*;
import com.meteoweb.meteoanalyzer.FileYear;
import com.meteoweb.meteoanalyzer.agregatorhbaseloader.hbaseMain;
import com.meteoweb.meteoanalyzer.hbasefileloader.HbaseFileMain;
import com.meteoweb.meteoanalyzer.mysqlloaderimpl.MySqlMain;
import com.meteoweb.repository.QueryInterface;
import com.meteoweb.repository.StationInformationRepository;
import com.meteoweb.repository.YearAgregationRepo;
import com.meteoweb.service.AService;
import com.meteoweb.service.ComplexFilterService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping({ "/", "/index" })
public class StationInformationController {

    int max =2020;
    int min =1763;
    int val =0;
    private final static String ROOTPATH = "/home/hadoop/Temperature/";
    private final static String TEMPATH = "/home/hadoop/output/";
    private final static String TEM1PATH = "/home/hadoop/output1/";

    Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private AService aService;

    @Autowired
    private ComplexFilterService complexFilterService;

    @Autowired
    public StationInformationRepository stationInformationRepository;

    @Autowired
    private YearAgregationRepo yearAgregationRepo;

    @Autowired
    public QueryInterface queryInterface;


    @Autowired
    private EntityManager em;

    @GetMapping("/view")
    public String searchStationInformation(Model model,
                                           @RequestParam(name="page",defaultValue = "0") int page){
        Page<StationInformation> stationInformation =  stationInformationRepository.findAll(PageRequest.of(page,10000));
        model.addAttribute("stationInformation", stationInformation);
        return "view";
    }

    @GetMapping("/search")
    private String search(@RequestParam(value = "search", required = false) String search, Model model){

        TypedQuery<StationInformation> query = (TypedQuery<StationInformation>) em.createQuery(search);

        List<StationInformation> stationInformations = query.getResultList();

        //SELECT e FROM  StationInformation e
        if(stationInformations==null)
            model.addAttribute("stationInformation", Arrays.asList());
        else
            model.addAttribute("stationInformation",stationInformations);

        return "view";
    }

    @PostMapping("/generate")
    private String generate(@ModelAttribute Ayear aYear, Model model) throws IOException {

        /*if(year.length()==4){
            if ( year<min || max<year)){
                model.addAttribute("result","Plese Enter Year 1763=>2020");
                return "DataYearErr";
            }else
            try */

     for (int year=aYear.getStart();year<=aYear.getEnd();year++){

        try {
            if(HbaseFileMain.runHeFileLoader (String.valueOf(year))==0)
                try {
                    FileUtils.cleanDirectory(new File(ROOTPATH));
                    if(hbaseMain.runAggregatorHbaseLoader(null)==0)
                        try {
                            if(MySqlMain.runMysqlMR(null)==0)
                                aService.yearAgregation();
                        }catch (Exception e) {
                            e.printStackTrace();
                            model.addAttribute("result","Failed Mysql Run");
                            return "DataError";
                        }
                }catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("result","Failed Agregation Run");
                    return "DataError";
                }
        }catch (Exception e){
            e.printStackTrace();
            return "DataNetErr";
        }
     }

        model.addAttribute("result","SUCCESS Mysql Run");
        return "view";
    }


    @GetMapping("/")
    private String homeAnalyser(Model model){
        return "AnalyserHome";
    }

    @GetMapping("/Data")
    private String DataInformation(Model model){
        model.addAttribute("aYear", new Ayear());
        return "Data";
    }

    @GetMapping("/dimensions")
    private String getDimensions(Model model) throws IOException {

        model.addAttribute("searchFilter",new ComplexSearchFilter());

        model.addAttribute("dimensions1",Arrays.asList("YEAR","MONTH"));

        model.addAttribute("dimensions2",Arrays.asList("STATION","ELEVATION","LATITUDE","LONGITUDE","CITY"));

        model.addAttribute("agregations",Arrays.asList("AVG","SUM"
                ,"MIN","MAX"));
        model.addAttribute("mesures",Arrays.asList("TAVG","TMIN","TMAX"));

        return "dimensions";
    }

    @PostMapping("/dimensions")
    private String getByDimensions(@ModelAttribute ComplexSearchFilter searchFilter, Model model) throws IOException
    {

        List<QueryResult> results = null;


        if(searchFilter.getDimension3().equals("SUM")){
            results =complexFilterService.complexSearch(
                    new ComplexSearchFilter(
                            doMap(searchFilter.getDimension1()),
                            doMap(searchFilter.getDimension2()),
                            null,
                            doMap(searchFilter.getAgregation()),
                            doMap(searchFilter.getDimension3())));
        }else{
            if(searchFilter.getDimension1().equals("MONTH")){
                results =complexFilterService.complexSearchFromStationInfo(
                        new ComplexSearchFilter(
                                doMap(searchFilter.getDimension1()),
                                doMap(searchFilter.getDimension2()),
                                null,
                                doMap(searchFilter.getAgregation()),
                                null));
            } else{
                results =complexFilterService.complexSearch(
                        new ComplexSearchFilter(
                                doMap(searchFilter.getDimension1()),
                                doMap(searchFilter.getDimension2()),
                                null,
                                doMap(searchFilter.getAgregation()),
                                null));
            }
        }

        List<String> rows = results.stream().map(QueryResult::getRow)
                .distinct().sorted()
                .collect(Collectors.toList());

        Map<String, List<QueryResult>> colsTmp = results.stream()
                .collect(Collectors.groupingBy(
                        QueryResult::getCol
                ));


        Map<String,List<QueryResult>> cols = new HashMap<>();

        for (String col : colsTmp.keySet()){
            cols.put(col, colsTmp.get(col).stream()
                                .sorted(Comparator.comparing(QueryResult::getRow))
                                .collect(Collectors.toList()));
        }


        model.addAttribute("rows", rows);
        model.addAttribute("cols",cols);

        return "dimensions";
    }


    @GetMapping("/allTmax")
    public String allTmax(Model model){
        List<StationInformation> stationInformation = queryInterface.getTmax();
        model.addAttribute("stationInformation", stationInformation);
        return "allTmax";
    }

    @GetMapping("/allTmin")
    public String allTmin(Model model){
        List<StationInformation> stationInformation = queryInterface.getTmin();
        model.addAttribute("stationInformation", stationInformation);
        return "allTmin";
    }

    @GetMapping("/allTavg")
    public String allTavg(Model model){
        List<StationInformation> stationInformation = queryInterface.getTavg();
        model.addAttribute("stationInformation", stationInformation);
        return "allTavg";
    }
    @GetMapping("/allInone")
    private String allInone(Model model,
                           @RequestParam(name="page",defaultValue = "0") int page){
        Page<StationInformation> stationInformation =  stationInformationRepository.findAll(PageRequest.of(page,10000));
        model.addAttribute("stationInformation", stationInformation);
        return "allInone";
    }

    @GetMapping("/information")
    private String info(Model model){
        return "information";
    }

    private String doMap(String value){

        switch (value){
            case "STATION" : return "id";
            default: return value.toLowerCase();
        }

    }

}

class QueryResult2D{

    String col;

    String val;


    public QueryResult2D(String col, String val) {
        this.col = col;
        this.val = val;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}