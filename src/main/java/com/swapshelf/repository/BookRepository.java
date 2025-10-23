package com.swapshelf.repository;

import com.swapshelf.entity.Book;
import com.swapshelf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByOwner(User user);

    @Query("SELECT b FROM Book b WHERE b.owner.id <> :userId " +
            "AND b.status = com.swapshelf.entity.enums.BookStatus.AVAILABLE " +
            "AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:genre IS NULL OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) " +
            "AND (:tag IS NULL OR LOWER(b.tags) LIKE LOWER(CONCAT('%', :tag, '%')))")
    List<Book> findBooksByFilters(@Param("userId") Long userId,
                                  @Param("title") String title,
                                  @Param("author") String author,
                                  @Param("genre") String genre,
                                  @Param("tag") String tag);


    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(String title, String author, String genre);

    @Query("SELECT DISTINCT b.genre FROM Book b")


    List<String> findAllGenres();

    List<Book> findByGenre(String genre);



    @Query("SELECT DISTINCT b.tags FROM Book b")
    List<String> findAllTags();

    List<Book> findByTags(String tags);

}





