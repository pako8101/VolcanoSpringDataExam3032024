package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Volcano;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VolcanoRepository extends JpaRepository<Volcano,Long> {
    Optional<Volcano> findByName(String name);
//@Query(value = "select v from  Volcano  as v where v.Elevation >= 3000" +
//        " and v.lastEruption is not null  order by v.Elevation desc ")
@Query(value = "SELECT v.name, c.name, v.lastEruption " +
        "FROM Volcano v " +
        "JOIN v.country c " +
        "WHERE v.isActive = true " +
        "AND v.Elevation > 3000 " +
        "AND v.lastEruption IS NOT NULL " +
        "ORDER BY v.Elevation DESC")
Set<Volcano> findAllByElevationAboveAndLastEruption();
}
