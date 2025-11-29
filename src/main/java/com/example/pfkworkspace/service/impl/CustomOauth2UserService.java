package com.example.pfkworkspace.service.impl;

import com.example.pfkworkspace.entity.User;
import com.example.pfkworkspace.enums.OauthSource;
import com.example.pfkworkspace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email address not found");
        }

        User user = userRepository.findUserByEmail(email).orElse(null);

        if (user == null) {

        }
    }

    private

    private OauthSource detectOauthSource(OAuth2User user) {
        Map<String,Object> attributes = user.getAttributes();

        if (attributes.containsKey("sub")) return OauthSource.GOOGLE;
        if (attributes.containsKey("login")) return OauthSource.GITHUB;

        return OauthSource.UNKNOWN;
    }
}
