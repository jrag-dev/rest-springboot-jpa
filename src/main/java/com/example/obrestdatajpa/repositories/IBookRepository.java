package com.example.obrestdatajpa.repositories;

import com.example.obrestdatajpa.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {
}
