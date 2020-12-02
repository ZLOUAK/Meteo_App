package com.meteoweb.repository;

import com.meteoweb.domain.StationInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueryInterface extends JpaRepository<StationInformation,String> {

    @Query(value="SELECT * FROM `StationInformation` WHERE (id,`date`, `latitude`, `longitude`, `elevation`,`city`,`tmax`) IN (SELECT id,`date`, `latitude`, `longitude`, `elevation`,`city`,MAX(`tmax`) FROM `StationInformation` where tmax not in (0) GROUP by id order by tmax DESC) GROUP by id",nativeQuery=true)
    public List<StationInformation> getTmax();

    @Query(value="SELECT * FROM `StationInformation` WHERE (id,`date`, `latitude`, `longitude`, `elevation`,`city`,`tmin`) IN (SELECT id,`date`, `latitude`, `longitude`, `elevation`,`city`,MIN(`tmin`) FROM `StationInformation` where tmin not in (0) GROUP by id order by tmin DESC) GROUP by id",nativeQuery=true)
    public List<StationInformation> getTmin();

    @Query(value="SELECT * FROM `StationInformation` WHERE (id,`date`, `latitude`, `longitude`, `elevation`,`city`,`tavg`) IN (SELECT id,`date`, `latitude`, `longitude`, `elevation`,`city`,MAX(`tavg`) FROM `StationInformation` where tavg not in (0) GROUP by id order by tavg DESC) GROUP by id",nativeQuery=true)
    public List<StationInformation> getTavg();
}
