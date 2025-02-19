package co.kh.app.entities;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "e_books")
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="uuid")
    private UUID uuid;
    @Column(name ="title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name =  "status")
    private Boolean status = true;


}
