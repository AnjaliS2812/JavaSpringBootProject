package com.junit.crudOperation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.junit.crudOperation.Entity.Book;

public interface BookRepository  extends JpaRepository<Book, Long>{

}
