package com.tekser.configuration;

import com.tekser.domain.Role;
import com.tekser.domain.User;
import com.tekser.domain.Settings;
import com.tekser.service.RoleService;
import com.tekser.service.UserService;
import com.tekser.service.utilservice.SettingsService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private UserService userService;

    private RoleService roleService;

//    private SettingsService settingsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SetupDataLoader(UserService userService, RoleService roleService,
                           BCryptPasswordEncoder bCryptPasswordEncoder, SettingsService settingsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.settingsService = settingsService;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
//            if (alreadySetup || settingsService.findSettings().getFirstRun() == 0) {

                return;
        }

        //region Creating roles
        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        Role roleUser = createRoleIfNotFound("ROLE_USER");
        List<Role> adminRoles = Collections.singletonList(roleAdmin);
        List<Role> userRoles = Collections.singletonList(roleUser);
        //endregion


        //region Creating users
        createUserIfNotFound("admin@example.com", "Admin", "Admin", "admin", "admin", adminRoles);

        for (int i = 1; i < 5; i++) {
            createUserIfNotFound("user" + i + "@example.com", "User" + i, "User" + i, "user" + i, "user" + i, userRoles);
        }
        //endregion

//        settingsService.save(new Settings(0L,0));
        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = roleService.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleService.save(role);
        }
        return role;
    }

    @Transactional
    void createUserIfNotFound(final String email, String name,
                              String surname, String username,
                              String password, List<Role> userRoles) {
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setSurname(surname);
            user.setUsername(username);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setEmail(email);
            user.setRoles(userRoles);
            user.setEnabled(true);
            userService.save(user);
        }
    }
}
