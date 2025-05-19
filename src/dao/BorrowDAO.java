package dao;

import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowDAO {

    // 도서 대출을 처리 기능
    public void borrowBook(int bookId, int studentPk) throws SQLException {
        // 대출 가능 여부 ---SELCET(books)
        // 대출 한다면 ---> INSERT(borrows)
        // 대출이 실행 되었다면 --> UPDATE(books -> available)

        String checkSql = "select available from books where id = ? ";
        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
            checkPstmt.setInt(1, bookId);
            ResultSet rs1 = checkPstmt.executeQuery();

            if (rs1.next() && rs1.getBoolean("available")) {
                //insert, update
                String insertSql = "insert into borrows (student_id, book_id, borrow_date) \n" +
                        "values(?, ?, CURRENT_DATE) ";
                String updateSql = "update books set available = FALSE where id = ? ";

                try (PreparedStatement borrowStmt = conn.prepareStatement(insertSql);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    borrowStmt.setInt(1, studentPk);
                    borrowStmt.setInt(2, bookId);
                    System.out.println("--------------------------------------------");
                    updateStmt.setInt(1,bookId);

                    borrowStmt.executeUpdate();
                    updateStmt.executeUpdate();

            }

            }else{
                throw new SQLException("도서가 대출 불가능 합니다.");
            }
        }
    }

    // 메인 함수
    public static void main(String[] args) {
        // 대출 실행 테스트
        BorrowDAO borrowDAO = new BorrowDAO();

        try {
            borrowDAO.borrowBook(1,3);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
