package co.kh.app.entities;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role implements GrantedAuthority{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;
    @Column(name = "role_name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_authorities",
    joinColumns = @JoinColumn(name ="role_id", referencedColumnName ="id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName ="id")
    )
    private  List<Authority> authorities;

    @Override
    public String getAuthority() {
        return this.getAuthority();
    }
}
