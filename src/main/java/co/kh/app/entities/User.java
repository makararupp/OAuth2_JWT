package co.kh.app.entities;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_status")
    private Boolean status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName ="id")
    )

    private List<Role> roles;
    
}
