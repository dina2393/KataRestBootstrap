package ru.kata.spring.boot_security.demo.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password_name")
    private String password;

    @Column(name="last_name")
    private String lastName;
    @Column(name="age")
    private int age;
    @Column(name="email")
    private String email;

    @ManyToMany(cascade=CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role"
            ,joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(){
    }
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public User(String userName, String password, String lastName, int age, String email) {
        this.userName = userName;
        this.password = password;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {return age;}
    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {this.email = email;}

    public Set<Role> getRoles() {return roles;}
    public void setRoles(Set<Role> roles) {this.roles = roles;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
    @Override
    public String getUsername() {
        return getUserName();
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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

//    public void addRoleToUser(Role role) {
//        this.roles.add(role);
//
//    }
    public String showUserList(){
        StringJoiner sj = new StringJoiner(" ");
        for(Role role : roles) {
            sj.add(role.getRole());
        }
        String userList = sj.toString();
        return userList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && getAge() == user.getAge() && getUserName().equals(user.getUserName()) && getPassword().equals(user.getPassword()) && getLastName().equals(user.getLastName()) && getEmail().equals(user.getEmail()) && getRoles().equals(user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserName(), getPassword(), getLastName(), getAge(), getEmail(), getRoles());
    }
}
