package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    @NotBlank(message = "Имя не может быть пустым")
    @Column(nullable = false, length = 30)
    private String name;

    /*
    Здесь не нужно описывать связь с User, так как там описана связь @ManyToMany с промежуточной таблицей. Если создать, то будет двунаправленная связь, но для Spring Security достаточно однонаправленной
     */

    public Role() {
    }

    public Role(Long id, String name) {
        this.role_id = id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getRoleId() {
        return role_id;
    }

    public void setRoleId(Long id) {
        this.role_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", role_id=" + role_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /*
    hasRole("ADMIN") ищет ROLE_ADMIN. Метод добавляет префикс, избегая необходимости хранить роли в БД с префиксом
     */
    @Override
    public String getAuthority() {
        return name.startsWith("ROLE_") ? name : "ROLE_" + name;
    }
}
