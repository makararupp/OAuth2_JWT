package co.kh.app.service;

import java.util.List;

import co.kh.app.entities.Book;

public interface BookService {

     List<Book> findAll();

     void deleteBookByStatus(Boolean status);
    
}
