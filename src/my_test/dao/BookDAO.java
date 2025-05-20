package my_test.dao;

import my_test.dto.Book;
import my_test.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 도서 관련 데이터베이스 작업을 처리하는 DAO 클래스
 */
public class BookDAO {

    // 새 도서를 데이터 베이스에 추가
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, publisher, publication_year,isbn)" +
                "VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setString(5, book.getIsbn());
            pstmt.executeUpdate();
        }
    }

    // 모든 도서 목록을 조회 기능 추가
    public List<Book> getAllBooks() throws SQLException {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT FROM  books ";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("author");
                int publicationYear = rs.getInt("publication_year");
                String isbn = rs.getString("isbn");
                boolean available = rs.getBoolean("available");

                Book book = new Book(id, title, author, publisher, publicationYear, isbn, available);
                bookList.add(book);
            }
        }
            return bookList;
    }
    // 책 제목으로 도서를 검색 기능
    public List<Book> searchBooksTitle(String searchTitle) throws SQLException {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books where title like ? ";

        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,"%" + searchTitle + "%");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String title =rs.getString("title");
                String author = rs.getString("author");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return bookList;
    }

    public static void main(String[] args) {
       BookDAO bookDAO = new BookDAO();
       try {
               ArrayList<Book> selectedBookList =
                       (ArrayList) bookDAO.searchBooksTitle("입문");
               for(int i = 0 ; i < selectedBookList.size(); i++) {
                   System.out.println(selectedBookList.get(i));
               }
               } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }


} // end of class
