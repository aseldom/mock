package ru.checkdev.notification.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.notification.domain.Setting;
import ru.checkdev.notification.service.SettingService;

import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

    private final SettingService settings;

    public SettingController(final SettingService settings) {
        this.settings = settings;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public List<Setting> findAll() {
        return this.settings.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/find")
    public Object updateView(@RequestParam(required = false) Integer id) {
        return new Object() {
            public Setting.Key[] getKeys() {
                return Setting.Key.values();
            }

            public Setting getSetting() {
                return id != null ? settings.findById(id) : new Setting();
            }
        };
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/")
    public Setting create(@RequestBody Setting setting) {
        return this.settings.save(setting);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/")
    public boolean delete(@RequestParam int id) {
        return this.settings.delete(id);
    }
}
