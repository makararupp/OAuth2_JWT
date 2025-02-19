package co.kh.app.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import co.kh.app.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>{
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByTitleStartingWithIgnoreCaseOrderByStatusDesc(String title);

    @Query("SELECT b FROM e_books b where b.author =  ?1")
    List<Book> selectBookByAuthorName(String author);

    @Query("SELECT b from e_books b where b.uuid = ?1")
    Optional<Book> selectBookByPrimarykey(UUID uuid);

    @Modifying
    @Query("DELETE FROM e_books b where b.status = ?1")
    void deleteBookByStatus(Boolean status);

    
}
