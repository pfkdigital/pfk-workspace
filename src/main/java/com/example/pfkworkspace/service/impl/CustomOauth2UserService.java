package com.example.pfkworkspace.service.impl;

import com.example.pfkworkspace.entity.User;
import com.example.pfkworkspace.enums.AuthProvider;
import com.example.pfkworkspace.enums.OauthSource;
import com.example.pfkworkspace.enums.Roles;
import com.example.pfkworkspace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {

    OAuth2User oAuth2User = super.loadUser(userRequest);
    String email = oAuth2User.getAttribute("email");
    String provider = userRequest.getClientRegistration().getClientName();
    System.out.println(provider);

    if (email == null) {
      throw new OAuth2AuthenticationException("Email address not found");
    }

    User user = userRepository.findUserByEmailAddress(email).orElse(null);

    if (user == null) {
      user =
          provider.equals("Google")
              ? createGoogleSourceUser(oAuth2User)
              : createGithubSourceUser(oAuth2User);
    }

    userRepository.save(user);

    String userNameAttributeName =
        userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

    return new DefaultOAuth2User(
        List.of(new SimpleGrantedAuthority(Roles.ROLE_USER.name())),
        oAuth2User.getAttributes(),
        userNameAttributeName);
}

  private User createGoogleSourceUser(OAuth2User oAuth2User) {
    String email = oAuth2User.getAttribute("email");
    String baseName;

    if (email != null) {
      baseName = email.substring(0, email.indexOf("@"));
    } else {
      baseName = oAuth2User.getAttribute("given_name");
    }
    return User.builder()
        .firstName(oAuth2User.getAttribute("given_name"))
        .lastName(oAuth2User.getAttribute("family_name"))
        .username(baseName)
        .password(null)
        .emailAddress(email)
        .role(Roles.ROLE_USER)
        .authProvider(AuthProvider.GOOGLE)
        .providerId(oAuth2User.getAttribute("sub"))
        .isEnabled(true)
        .build();
  }

  private User createGithubSourceUser(OAuth2User oAuth2User) {
    String fullName = oAuth2User.getAttribute("name");
    String firstName;
    String lastName;

    if (fullName != null) {
      firstName = fullName.substring(0, fullName.indexOf(" "));
      lastName = fullName.substring(fullName.indexOf(" ") + 1);
    } else {
      firstName = "";
      lastName = "";
    }

    return User.builder()
        .firstName(firstName)
        .lastName(lastName)
        .username(oAuth2User.getAttribute("login"))
        .password(null)
        .emailAddress(oAuth2User.getAttribute("email"))
        .role(Roles.ROLE_USER)
        .authProvider(AuthProvider.GITHUB)
        .providerId(oAuth2User.getAttribute("id"))
        .isEnabled(true)
        .build();
  }
}
