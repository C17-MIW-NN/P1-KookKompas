package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author MMA Dev Force
 * Any user of the KookKompas software
 */
@Entity
public class KookKompasUser implements UserDetails {

    @Id @GeneratedValue
    private Long userId;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToOne(mappedBy = "kookKompasUser", cascade = CascadeType.ALL)
    private ShoppingList shoppingList;

    public void createShoppingListIfMissing() {
        if (shoppingList == null) {
            shoppingList = new ShoppingList();
            shoppingList.setKookKompasUser(this);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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
