package ar.vicria.security.microservice.configurations;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
public class SecurityUser implements UserDetails {

    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    /**
     * ID пользователя.
     */
    private Integer id;

    /**
     * Логин.
     */
    private String username;

    /**
     * Пароль.
     */
    private String password;

    /**
     * Роли Spring Security.
     */
    private List<SimpleGrantedAuthority> authorities;

    /**
     * Конструктор с обязательными полями.
     *
     * @param id          ID пользователя
     * @param username    login
     * @param password    пароль
     * @param authorities список прав
     */
    public SecurityUser(
            Integer id, String username, String password,
            List<SimpleGrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static SecurityUser create(int id, String login, String password) {
        List<SimpleGrantedAuthority> roles = new LinkedList<>();
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        roles.add(admin);
            /*systemUser.getOsuSubject().getRights()
                .stream()
                .map(it -> new SimpleGrantedAuthority("ROLE_" + it.getId()))
                .collect(Collectors.toList());*/

        return new SecurityUser(
                id,
                login,
                passwordEncoder.encode(password),
                roles
        );
    }

    /**
     * Указывает, истек ли срок действия учетной записи пользователя.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, заблокирован ли пользователь или разблокирован.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, истек ли срок действия учетных данных пользователя (пароля).
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, включен ли пользователь или отключен.
     *
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Objects.equals(id, ((SecurityUser) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
