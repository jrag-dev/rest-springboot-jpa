package com.example.obrestdatajpa.controllers;


import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repositories.IBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);

    // Atributos
    private IBookRepository bookRepository;

    // Constructores
    public BookController(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    // ********* CRUD sobre la entidad Book ********* \\

    // Buscar todos los libros

    /**
     * http://localhost:4000/api/books
     * @return
     */
    @GetMapping("/api/books")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    //Buscar un sólo libro en base de datos según su id

    /**
     * http://localhost:4000/api/books/1
     * http://localhost:4000/api/books/2
     * Request
     * Response
     * @param id
     * @return
     */
    @GetMapping("/api/books/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id){

        Optional<Book> bookOpt = bookRepository.findById(id);

        // opción 1:
        if (bookOpt.isPresent()) {
            return ResponseEntity.ok(bookOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }

        // opción 2:
        //return bookOpt.orElse(null);
    }

    // Crear un nuevo libro en base de datos

    /**
     * http://localhost:4000/api/books
     * @param book
     * @param headers
     * @return Book
     */
    @PostMapping("/api/books")
    public ResponseEntity<Book> create(@RequestBody Book book, @RequestHeader HttpHeaders headers) {

        System.out.println(headers);

        // Verificamos que no exista un libro con el mismo titulo
        List<Book> bookDB = bookRepository.findAll();

        Boolean bookTitleExist = false;
        for (int i = 0; i < bookDB.size(); i++) {
            System.out.println(bookDB.get(i).getTitle());
            if (bookDB.get(i).getTitle().equals(book.getTitle())) {
                bookTitleExist = true;
            }
        }

        if (bookTitleExist) {
            log.warn("Trying to create a new book with a title already created.");
            System.out.println("Trying to create a new book with a title already created.");
            return ResponseEntity.badRequest().build();
        }

        // guadar el libro recibido por parámetros en la base de datos
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result);
    }


    /**
     * Actualizar un libro existente en base de datos::
     * http://localhost:4000/api/books/1
     * http://localhost:4000/api/books/2
     *
     * Request
     * Response
     * @param id
     * @return  Book
     */

    @PutMapping("/api/books/{id}")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book book) {

        if (book.getId() == null) {
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }

        if (!bookRepository.existsById(id)) {
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();
        }

        Book bookDB = bookRepository.findById(id).get();

        bookDB.setTitle(book.getTitle());
        bookDB.setAuthor(book.getAuthor());
        bookDB.setPages(book.getPages());
        bookDB.setPrice(book.getPrice());
        bookDB.setReleaseDate(book.getReleaseDate());
        bookDB.setOnline(book.getOnline());

        Book result = bookRepository.save(bookDB);

        return ResponseEntity.ok(result);
    }

    /**
     * Borrar un libro en base de datos::
     *
     * http://localhost:4000/api/books/1
     * http://localhost:4000/api/books/2
     * @param id
     * @return String
     */
    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Book> deleteById(@PathVariable Long id) {
        try {
            if (!bookRepository.existsById(id)) {
                log.warn("Trying to update a non existent book");
                return ResponseEntity.notFound().build();
            }

            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().build();
        }

    }

    @DeleteMapping("/api/books")
    public ResponseEntity<Book> deleteAll() {
        try {
            log.info("REST Request Deleting all books");
            bookRepository.deleteAll();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
