package com.learnboot.dreamshopping.data;

import com.learnboot.dreamshopping.models.Role;
import com.learnboot.dreamshopping.models.User;
import com.learnboot.dreamshopping.repository.RoleRepository;
import com.learnboot.dreamshopping.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRole = Set.of("ROLE_ADMIN","ROLE_USER");
        createDefaultRoles(defaultRole);
        createDefaultUserIfNotExists();
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {

        Role role = roleRepository.findByName("ROLE_USER").get();

        for(int i=0;i<5;i++){
            String email =  "user"+i+"@gmail.com";
            if(!userRepository.existsByEmail(email)){
               User user = new User();
               user.setEmail(email);
               user.setFirstName("User"+i);
               user.setLastName("User"+i);
               user.setRoles(Set.of(role));
               user.setPassword(passwordEncoder.encode("123456"));
               userRepository.save(user);
            }
        }
    }

    private void createDefaultRoles(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }


    private void createDefaultAdminIfNotExists() {

        Role role = roleRepository.findByName("ROLE_ADMIN").get();

        for(int i=1;i<=2;i++){
            String email =  "admin"+i+"@gmail.com";
            if(!userRepository.existsByEmail(email)){
                User user = new User();
                user.setEmail(email);
                user.setFirstName("admin"+i);
                user.setLastName("admin"+i);
                user.setRoles(Set.of(role));
                user.setPassword(passwordEncoder.encode("123456"));
                userRepository.save(user);
            }
        }
    }
}
