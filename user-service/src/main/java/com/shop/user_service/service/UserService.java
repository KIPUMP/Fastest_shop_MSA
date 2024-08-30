package com.shop.user_service.service;

import com.shop.user_service.constant.UserRoleEnum;
import com.shop.user_service.dto.LoginRequestDto;
import com.shop.user_service.dto.SignupRequestDto;
import com.shop.user_service.entity.User;
import com.shop.user_service.jwt.JwtUtil;
import com.shop.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String email = requestDto.getEmail();
        String phoneNumber = requestDto.getPhoneNumber();
        String address = requestDto.getAddress();

        Optional<User> checkUserId = userRepository.findByUserId(userId);
        Optional<User> checkUserEmail = userRepository.findByEmail(email);
        Optional<User> checkUserPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);


        if(checkUserId.isPresent()){
            throw new IllegalArgumentException("중복된 ID가 존재합니다");
        }
        if(checkUserPhoneNumber.isPresent()){
            throw new IllegalArgumentException("중복된 전화번호가 존재합니다");
        }
        if(checkUserEmail.isPresent()){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException(("관리자 암호가 틀려 등록이 불가능합니다 "));
            }
            role = UserRoleEnum.ADMIN;
        }
        String encodedPassword = passwordEncoder.encode(password);
        String encodeUserName = passwordEncoder.encode(username);
        String encodeAddress = passwordEncoder.encode(address);

        User user = new User(userId, encodeUserName, encodedPassword, email, phoneNumber, encodeAddress, role);
        userRepository.save(user);
    }

    public String login(LoginRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String password = requestDto.getPassword();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지 않습니다"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다");
        }

        return jwtUtil.createToken(user.getUserId(), UserRoleEnum.valueOf(user.getRole().toString()));

    }


}