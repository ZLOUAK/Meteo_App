package com.meteoweb.repository;

import com.meteoweb.domain.YearAgregation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YearAgregationRepo extends JpaRepository<YearAgregation,String> {

   /* List<YearAgregation> complexSearch(
            String filter,
            String dimension1,
            String dimension2,
            String agregation);*/


}
