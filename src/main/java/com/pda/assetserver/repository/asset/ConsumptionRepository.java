package com.pda.assetserver.repository.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    @Query(value = "select c.*, a.product_id, a.p_type, a.user_id from consumption c " +
        "left join asset a on c.account_id = a.id "
        + "where a.user_id =:userId and c.date_time between :from and :to " +
        "order by c.date_time desc ", nativeQuery = true)
    List<Consumption> findByUserIdAndDateTimeBetween(@Param("userId") String userId, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
