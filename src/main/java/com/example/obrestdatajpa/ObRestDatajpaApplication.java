package com.example.obrestdatajpa;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repositories.IBookRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class ObRestDatajpaApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(ObRestDatajpaApplication.class, args);

		IBookRepository repository = context.getBean(IBookRepository.class);

		// Crear un libro
		Book book1 = new Book(null, "Quantum Mechanics", "Sakuray", 502, 245.99, LocalDate.of(2010, 11, 21), true);
		Book book2 = new Book(null, "Statistical Physics", "Willian Jose", 365, 185.99, LocalDate.of(2020, 12, 28), true);

		System.out.println("Num libros en base de datos: " + repository.findAll().size());

		// Almacenar un libro
		//repository.save(book1);
		//repository.save(book2);

		// Recuperar un libro
		System.out.println("Num libros en base de datos: " + repository.findAll().size());

		// Borrar un libro
		repository.deleteById(1L);

		System.out.println("Num libros en base de datos: " + repository.findAll().size());
	}

}
