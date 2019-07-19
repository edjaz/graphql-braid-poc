package fr.edjaz.graphql.book.repository;

import fr.edjaz.graphql.book.models.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BookRepository {

    private static List<Book> bookList;

    static {
        bookList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            bookList.add(new Book("book" + i,  "bookname" + i));
        }
    }


    public Book bookById(String id) {
        return bookList.stream().filter(user -> Objects.equals(user.getId(), id)).findFirst().orElse(null);
    }


}
