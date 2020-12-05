package com.meteoweb.repository;

import com.meteoweb.domain.StationInformation;
import com.meteoweb.domain.YearAgregation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YearAgregationRepo extends JpaRepository<YearAgregation,String> {
}
