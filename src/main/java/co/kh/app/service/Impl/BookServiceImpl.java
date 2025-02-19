package co.kh.app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.kh.app.entities.Book;
import co.kh.app.repositories.BookRepository;
import co.kh.app.service.BookService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteBookByStatus(Boolean status) {
       bookRepository.deleteBookByStatus(status);
        
    }
}
