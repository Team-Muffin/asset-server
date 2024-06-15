package com.pda.assetserver.repository.asset;

import com.pda.assetserver.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<? extends Asset> findByUser(User user);
}
