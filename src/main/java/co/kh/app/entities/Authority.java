package co.kh.app.entities;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//Authority and Permission is the same case

@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "authority_name")
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private List<Role> roles;

}
