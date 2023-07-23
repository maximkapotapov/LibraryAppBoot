package com.github.library_app_boot.repositories;


import com.github.library_app_boot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<Book> findByTitleOfBook (String titleOfBook);

    Book findBookByTitleOfBookStartingWith (String keyword);


}
