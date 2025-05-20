package my_test.dao;

import dto.Borrow;
import my_test.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    // 도서 대출 처리 기능
    public void borrowBook(int bookId, int studentPK) throws SQLException {

        String checkSql = "select available from books where id = ? ";
        try(Connection conn = DatabaseUtil.getConnection();
            PreparedStatement checkPstmt = conn.prepareStatement(checkSql)){
            checkPstmt.setInt(1,bookId);
            ResultSet rs1 = checkPstmt.executeQuery();

            if (rs1.next() && rs1.getBoolean("available")) {
                //insert, update
                String insertSql = "insert into borrows (student_id, book_id, borrow_date) \n" +
                        "values(?, ?, CURRENT_DATE) ";
                String updateSql = "update books set available = FALSE where id = ? ";

                try (PreparedStatement borrowStmt = conn.prepareStatement(insertSql);
                     PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    borrowStmt.setInt(1, studentPK);
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
    // 현재 대출중인 도서 목록을 조회
    public List<Borrow> getBorrowedBooks() throws SQLException{
        List<Borrow> borrowList = new ArrayList<>();
        String sql = "select * from borrows where return_date is null ";
        try(
                Connection conn = util.DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);

        ) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Borrow borrowDTO = new Borrow();
                borrowDTO.setId(rs.getInt("id"));
                borrowDTO.setBookId(rs.getInt("book_id"));
                borrowDTO.setStudentId(rs.getInt("student_id"));
                // JAVA DTO 에서 데이터 타입은 localDate이다.
                //하지만 JDBC API에서 아직은 localDate 타입을 지원하지 않는다.
                //JDBC API 제공하는 날짜 데이터 타입은 Date이다.
                // rs.getLocalDate <-- 아직 지원안함
                // rs.getDate("borrow_date");
                borrowDTO.setBorrowDate(rs.getDate("borrow_date").toLocalDate());
                borrowList.add(borrowDTO);
            }
        }
        return borrowList;
    }

    // 도서 반납을 처리하는기능 추가
    // 1. borrows 테이블에 책 정보 조회 (check) -- SECELT ( 복합 조건)
    // 2. borrows 테이블에 return_date 수정 -- UPDATE
    // 3. books 테이블에 available 수정 --- UPDATE
    public void returnBook(int bookId, String studentPK) throws SQLException{
        // studentPK --> borrows 테이블에  student_id 컬럼이다.
        // 즉, students 테이블에  pk를 의미한다.
        // String sql = "select * from borrows where return_date = ?";
        String sql = "select * from borrows where book_id = ? AND student_id = ? AND return_date is null ";
//        try(
//            Connection conn = DatabaseUtil.getConnection();
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1,bookId);
//            pstmt.setInt(2,studentPK);
//            ResultSet rs = pstmt.executeQuery()
//
//                )
    }

    // 메인 함수
    public static void main(String[] args) {
        // 대출 실행 테스트
        dao.BorrowDAO borrowDAO = new dao.BorrowDAO();

        try {
            // borrowDAO.borrowBook(1,3);
            // 현재 대출 중인 조회

            System.out.println(borrowDAO.getBorrowedBooks());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    } // end of main



} // end of class
