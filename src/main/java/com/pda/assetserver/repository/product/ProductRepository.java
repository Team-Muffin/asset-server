package com.pda.assetserver.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select count(*) from product p where p.p_type = :pType", nativeQuery = true)
    Long countByPType(@Param("pType") String pType);
}
