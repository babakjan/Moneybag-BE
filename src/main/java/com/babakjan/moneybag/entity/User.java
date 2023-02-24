package com.babakjan.moneybag.entity;

import com.babakjan.moneybag.dto.user.RegisterRequest;
import com.babakjan.moneybag.dto.user.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts; //one user belongs to many accounts

    private String currency; //all accounts of user have to be in same currency in order to make statistics

    public User(RegisterRequest request, PasswordEncoder passwordEncoder) {
        firstName = request.getFirstName();
        lastName = request.getLastName();
        email = request.getEmail();
        password = passwordEncoder.encode(request.getPassword());
        role = Role.USER;
        currency = request.getCurrency();
    }

    /**
     * Add account.
     * @param account account to add
     */
    public void  addAccount(Account account) {
        for (Account a: accounts) {
            if (Objects.equals(a.getId(), account.getId())) {
                return; //account is already in list of accounts
            }
        }
        accounts.add(account);
    }

    /**
     * Create data transfer object.
     * @return user dto
     */
    public UserDto dto() {
        return UserDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .currency(currency)
                .accountIds(accounts != null ? accounts.stream().map(Account::getId).toList() : new ArrayList<>())
                .build();
    }

    /**
     * Get list of granted authorities (roles).
     * @return list of authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
}
