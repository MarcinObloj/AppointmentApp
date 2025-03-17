package com.financial.experts.database.postgres.repository;

import com.financial.experts.database.postgres.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationsRepository extends JpaRepository<Locations, Long> {

//    @Query("SELECT l FROM Locations l WHERE l.city = :city AND l.country = :country")
//    List<Locations> findByCityAndCountry(@Param("city") String city, @Param("country") String country);
}