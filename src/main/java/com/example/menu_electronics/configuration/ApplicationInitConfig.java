package com.example.menu_electronics.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.example.menu_electronics.constant.Constants;
import com.example.menu_electronics.dto.request.ProductInitRequest;
import com.example.menu_electronics.entity.Category;
import com.example.menu_electronics.entity.Product;
import com.example.menu_electronics.entity.Selection;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.repository.CategoryRepository;
import com.example.menu_electronics.repository.ProductRepository;
import com.example.menu_electronics.repository.SelectionRepository;
import com.example.menu_electronics.service.ProductService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.menu_electronics.constant.PredefinedRole;
import com.example.menu_electronics.entity.authen.Role;
import com.example.menu_electronics.entity.authen.User;
import com.example.menu_electronics.repository.authenRepository.RoleRepository;
import com.example.menu_electronics.repository.authenRepository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final List<String> LIST_CATEGORY = List.of("Coffee", "Tea", "Freeze","Frost");

    @NonFinal
    static final List<String> LIST_SELCTION = List.of("Get a spoon", "Get a cup", "Get ice","Get a straw","Get a chair");

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository, SelectionRepository selectionRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.CUSTOMER_ROLE)
                        .description("Customer role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                LocalDateTime orderTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .gender("MALE")
                        .email("hoangtanh21102002@gmail.com")
                        .status("Active")
                        .name("ADMIN")
                        .isCustomer(false)
                        .created(orderTime.format(formatter))
                        .updated(orderTime.format(formatter))
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }

            LIST_CATEGORY.forEach(item->{
                if(categoryRepository.findByName(item).isEmpty()) {
                    Category category = new Category();
                    category.setName(item);
                    category.setDescription("");
                    LocalDateTime orderTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    category.setCreated(orderTime.format(formatter));
                    category.setUpdated(orderTime.format(formatter));
                    category = categoryRepository.save(category);
                    List<ProductInitRequest> requests = Constants.LIST_PRODUCT_INIT.stream()
                            .filter(product -> product.getNameCategory().equals(item))
                            .toList();
                   if(!requests.isEmpty()){
                       for(ProductInitRequest request : requests) {
                           Optional<Product> product = productRepository.findByName(request.getName());
                           if(product.isPresent()) break;
                           try {
                               productService.saveProductInit(request, category);
                           } catch (Exception e) {
                               throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
                           }
                       }
                   }
                }
            });

            LIST_SELCTION.forEach(item->{
                if(selectionRepository.findByName(item).isEmpty()) {
                    Selection selection = new Selection();
                    selection.setName(item);
                    LocalDateTime orderTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                    selection.setCreated(orderTime.format(formatter));
                    selection.setUpdated(orderTime.format(formatter));
                    selectionRepository.save(selection);
                }
            });

            log.info("Application initialization completed .....");
        };
    }
}
