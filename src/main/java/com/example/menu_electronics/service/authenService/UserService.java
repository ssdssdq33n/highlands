package com.example.menu_electronics.service.authenService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import com.example.menu_electronics.dto.request.authenRequest.AuthenticationRequest;
import com.example.menu_electronics.dto.response.authenResponse.AuthenticationResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.menu_electronics.constant.PredefinedRole;
import com.example.menu_electronics.dto.request.authenRequest.UserCreationRequest;
import com.example.menu_electronics.dto.request.authenRequest.UserUpdateRequest;
import com.example.menu_electronics.dto.response.authenResponse.UserResponse;
import com.example.menu_electronics.entity.authen.Role;
import com.example.menu_electronics.entity.authen.User;
import com.example.menu_electronics.exception.AppException;
import com.example.menu_electronics.exception.ErrorCode;
import com.example.menu_electronics.mapper.authenMapper.UserMapper;
import com.example.menu_electronics.repository.authenRepository.RoleRepository;
import com.example.menu_electronics.repository.authenRepository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JavaMailSender javaMailSender;
    SpringTemplateEngine templateEngine;
    AuthenticationService authenticationService;

    @Value("${spring.mail.username}")
    @NonFinal
    String from;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(UserCreationRequest request) {
        Optional<User> userCheck = userRepository.findByUsername(request.getUsername());
        Optional<User> userCheckEmail = userRepository.findByEmail(request.getEmail());
        if (userCheck.isPresent() || userCheckEmail.isPresent()) throw new AppException(ErrorCode.USER_EXISTED);
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(request.getEmail()).matches()) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        User user = userMapper.toUser(request);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String password = generateRandomString(8);
        user.setPassword(passwordEncoder.encode(password));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setCreated(orderTime.format(formatter));
        user.setUpdated(orderTime.format(formatter));
        user.setIsCustomer(false);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("name", request.getName());
            context.setVariable("username", user.getUsername());
            context.setVariable("password", password);
            context.setVariable("proton", "IPOS " + year);
            context.setVariable("headerTab", "Your account has been successfully activated in the system:");
            String html = templateEngine.process("welcome-email", context);

            helper.setTo(request.getEmail());
            helper.setText(html, true);
            helper.setSubject("Your account");
            helper.setFrom(from);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.SEND_EMAIL_ERROR);
        }
        var userCreationReponse = userMapper.toUserResponse(user);
        return userCreationReponse;
    }

//    @PreAuthorize("hasRole('CUSTOMER')")
    public AuthenticationResponse createCustomer(UserCreationRequest request) {
        Optional<User> userCheck = userRepository.findByUsername(request.getUsername());
        Optional<User> userCheckEmail = userRepository.findByEmail(request.getEmail());
        if (userCheck.isPresent() || userCheckEmail.isPresent()) throw new AppException(ErrorCode.USER_EXISTED);
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if (!pattern.matcher(request.getEmail()).matches()) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        User user = userMapper.toUser(request);
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.CUSTOMER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setCreated(orderTime.format(formatter));
        user.setUpdated(orderTime.format(formatter));
        user.setStatus("Active");
        user.setIsCustomer(true);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        var userCreationReponse = userMapper.toUserResponse(user);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getUsername(), user.getPassword());
        return authenticationService.authenticateSignUp(user);
    }

    public UserResponse forgotPassword(String email) {
        Optional<User> userCheck = userRepository.findByEmail(email);
        if (!userCheck.isPresent()) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        User user = userCheck.get();
        String password = generateRandomString(8);
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);
        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("name", user.getName());
            context.setVariable("username", user.getUsername());
            context.setVariable("password", password);
            context.setVariable("proton", "IPOS " + year);
            context.setVariable("headerTab", "Your account has been successfully activated in the system:");
            String html = templateEngine.process("welcome-email", context);

            helper.setTo(user.getEmail());
            helper.setText(html, true);
            helper.setSubject("Your account");
            helper.setFrom(from);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.SEND_EMAIL_ERROR);
        }
        return userMapper.toUserResponse(user);
    }

    public String sendEmailPayment(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        try {
            helper.setTo(user.getEmail());
            helper.setText("Payment Success", true);
            helper.setSubject("Highland Coffee");
            helper.setFrom(from);
            javaMailSender.send(message);
            return "Send success";
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.SEND_EMAIL_ERROR);
        }
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findById(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        LocalDateTime orderTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        userMapper.updateUser(user, request);
        //        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //        var roles = roleRepository.findAllById(request.getRoles());
        //        user.setRoles(new HashSet<>(roles));
        user.setUpdated(orderTime.format(formatter));
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER','CUSTOMER')")
    public UserResponse updatePasswordUser(String userId, String passwordOld, String password) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(passwordOld, user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER','CUSTOMER')")
    public UserResponse updateNameUser(String userId, String name) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setName(name);
        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (user.getUsername().equals("admin")) throw new AppException(ErrorCode.UNAUTHENTICATED);
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(Boolean isCustomer) {
        log.info("In method get Users");
        return userRepository.findByIsCustomer(isCustomer).stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public static String generateRandomString(int length) {
        if (length < 2) {
            throw new IllegalArgumentException(
                    "Length must be at least 2 to include both uppercase and lowercase characters.");
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Add one random uppercase character
        sb.append((char) (random.nextInt(26) + 'A'));

        // Add one random lowercase character
        sb.append((char) (random.nextInt(26) + 'a'));

        // Add remaining characters
        for (int i = 2; i < length; i++) {
            int rand = random.nextInt(3);
            if (rand == 0) {
                sb.append((char) (random.nextInt(26) + 'A')); // Uppercase
            } else if (rand == 1) {
                sb.append((char) (random.nextInt(26) + 'a')); // Lowercase
            } else {
                sb.append((char) (random.nextInt(10) + '0')); // Digit
            }
        }

        // Shuffle the characters to make the string more random
        char[] charArray = sb.toString().toCharArray();
        for (int i = charArray.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = charArray[index];
            charArray[index] = charArray[i];
            charArray[i] = temp;
        }

        return new String(charArray);
    }
}
