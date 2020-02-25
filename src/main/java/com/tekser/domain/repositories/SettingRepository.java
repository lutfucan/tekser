package com.tekser.domain.repositories;

import com.tekser.domain.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Settings, Long> {

}
