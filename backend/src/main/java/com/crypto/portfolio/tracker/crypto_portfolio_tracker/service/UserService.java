package com.crypto.portfolio.tracker.crypto_portfolio_tracker.service;

import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.UserLoginDto;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.dto.UserRegisterDto;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.entity.User;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.repository.UserRepository;
import com.crypto.portfolio.tracker.crypto_portfolio_tracker.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User registerUser(UserRegisterDto request) throws Exception{
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if(existingUser.isPresent()){
            throw new Exception("User already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public String loginUserAndGetToken(UserLoginDto request){
        User user = loginUser(request.getEmail(), request.getPassword());
        return jwtService.generateToken(user.getEmail());
    }

    public User loginUser(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CompromisedPasswordException("Invalid password");
        }
        return user;
    }
}
