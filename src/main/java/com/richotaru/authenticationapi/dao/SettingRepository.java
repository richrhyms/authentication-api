package com.richotaru.authenticationapi.dao;
import com.richotaru.authenticationapi.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Setting findByName(String name);
}
