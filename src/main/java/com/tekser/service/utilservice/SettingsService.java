package com.tekser.service.utilservice;

import com.tekser.domain.repositories.SettingRepository;
import com.tekser.domain.Settings;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private SettingRepository settingRepository;

    public SettingsService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public Settings findSettings() {
        return settingRepository.findById(0L).orElse(null);
    }

    public void save(Settings settings) {
        settingRepository.save(settings);
    }

}
