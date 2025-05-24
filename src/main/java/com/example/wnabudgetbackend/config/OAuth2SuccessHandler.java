package com.example.wnabudgetbackend.config;

import com.example.wnabudgetbackend.model.AuthProvider;
import com.example.wnabudgetbackend.model.User;
import com.example.wnabudgetbackend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;

    @Value("${frontend.url}")
    private String URL;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            return userRepo.save(newUser);
        });

        String token = jwtUtil.generateToken(email);
        String userId = user.getId().toString();

        String redirectUrl = UriComponentsBuilder
                .fromUriString(URL + "/oauth-success")
                .queryParam("token", token)
                .queryParam("user_id", userId)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
