package taskagile.domain.model.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SimpleUser implements UserDetails, Serializable {

  private static final long serialVersionUID = -7144174657188362966L; // 자바 직렬화 대상 객체는 동일한 UID를 가지고 있어야한다.

  private long userId;
  private String username;
  private String password;

  public SimpleUser(User user) {
    this.userId = user.getId();
    this.username = user.getUsername();
    this.password = user.getPassword();
  }

  public long getUserId() {
    return userId;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
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

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SimpleUser)) return false;
    SimpleUser that = (SimpleUser) o;
    return Objects.equals(username, that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  @Override
  public String toString() {
    return "SimpleUser{" +
      "userId=" + userId +
      ", username='" + username + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
}
