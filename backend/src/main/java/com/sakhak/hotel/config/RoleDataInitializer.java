package com.sakhak.hotel.config;

import com.sakhak.hotel.roles.Role;
import com.sakhak.hotel.roles.RoleRepository;
import com.sakhak.hotel.users.User;
import com.sakhak.hotel.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RoleDataInitializer {

    @Bean
    public CommandLineRunner initDefaultRolesAndSuperAdmin(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            createRoleIfMissing(roleRepository, "CUSTOMER", "Customer account");
            createRoleIfMissing(roleRepository, "ADMIN", "Administrator");
            createRoleIfMissing(roleRepository, "SUPERADMIN", "Super administrator");
            createRoleIfMissing(roleRepository, "EMPLOYEE", "Employee");
            createRoleIfMissing(roleRepository, "STAFF", "Staff");

            if (!userRepository.existsByUsername("superadmin")) {
                Role superAdminRole = roleRepository.findByName("SUPERADMIN")
                        .orElseThrow(() -> new RuntimeException("SUPERADMIN role not found"));

                User superAdmin = new User();
                superAdmin.setUsername("superadmin");
                superAdmin.setEmail("superadmin@hotel.com");
                superAdmin.setPassword(passwordEncoder.encode("SuperAdmin123!"));
                superAdmin.setRole(superAdminRole);
                superAdmin.setEnabled(true);
                userRepository.save(superAdmin);
            }
        };
    }

    private void createRoleIfMissing(RoleRepository roleRepository, String name, String description) {
        if (!roleRepository.existsByName(name)) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}
