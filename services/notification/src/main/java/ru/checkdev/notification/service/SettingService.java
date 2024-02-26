package ru.checkdev.notification.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.Setting;
import ru.checkdev.notification.repository.SettingRepository;

import java.util.List;

/**
 * @author olegbelov
 * @since 21.12.2016
 */
@Service
public class SettingService {

    private final SettingRepository settings;

    @Autowired
    public SettingService(SettingRepository settings) {
        this.settings = settings;
    }

    public List<Setting> findAll() {
        return Lists.newArrayList(this.settings.findAll());
    }

    public Setting save(Setting setting) {
        return this.settings.save(setting);
    }

    public Setting findByKey(Setting.Key key) {
        return this.settings.findByKey(key);
    }

    public Setting findById(int id) {
        return this.settings.findById(id).get();
    }

    public boolean delete(int id) {
        var settings = new Setting();
        settings.setId(id);
        this.settings.delete(settings);
        return true;
    }
}
