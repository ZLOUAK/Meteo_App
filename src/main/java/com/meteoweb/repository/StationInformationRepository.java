package com.meteoweb.repository;

import com.meteoweb.domain.StationInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationInformationRepository extends JpaRepository<StationInformation,String> {

    List<StationInformation> findByIdContaining(String id);

    List<StationInformation> findByDateContaining(String date);

}
