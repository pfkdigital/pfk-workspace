package com.example.pfkworkspace.entity;

import com.example.pfkworkspace.enums.AuthProvider;
import com.example.pfkworkspace.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name", nullable = true)
  private String firstName;

  @Column(name = "last_name", nullable = true)
  private String lastName;

  @Column(unique = true, nullable = false, name = "email_address")
  private String emailAddress;

  @Column(nullable = false, unique = true, name = "username")
  private String username;

  @Column(name = "password", nullable = true)
  private String password;

  @Column(nullable = false, name = "role")
  @Enumerated(EnumType.STRING)
  private Roles role;

  @Column(name = "auth_provider")
  @Enumerated(value = EnumType.STRING)
  private AuthProvider authProvider;

  @Column(name = "provider_id")
  private String providerId;

  @Column(nullable = false, name = "is_enabled")
  private boolean isEnabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
