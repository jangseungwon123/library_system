package dao;

import dto.Student;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 새 학생을 데이터베이스에 추가하는 기능
    public void addStudent (Student student) throws SQLException{
        // 1. 쿼리문 만들기 및 테스트 (DB에서)



        String sql = "INSERT INTO students(name, student_id) VALUES (?,?) ";

        try(
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,student.getName());
            pstmt.setString(2,student.getStudentId());
            pstmt.executeUpdate(); // 실행하기위해 필요
        }
    }

    // 모든 학생 목록을 조회하는 기능
    public List<Student> getAllStudents() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT * FROM students ";

        try (
                Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery(sql);
        ) {

            while(rs.next()){
               Student studentDto = new Student();
               // int sId =rs.getInt("id")
               studentDto.setId(rs.getInt("id"));
               studentDto.setName(rs.getString("name"));
               studentDto.setStudentId(rs.getString("student_id"));
               studentList.add(studentDto);
            }
        }

        return studentList;
    }

    // 학생 student_id로 학생 인증(로그인 용) 기능 만들기
    public Student authenticateStudent(String studentId) throws SQLException {
        // 학생이 정확한 학번 입력하면 Student 객체가 만들어져서 리턴 됨
        // 학생이 잘못된 학번을 입력하면 null  값을 반환
        // if -- return new Student();
        String sql = "select * from students where student_id = ? ";

        try(Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1,studentId);
                ResultSet rs = pstmt.executeQuery();


                Student studentDTO = new Student();
                studentDTO.setStudentId("id");
                studentDTO.setName("name");
                studentDTO.setStudentId("student_id");
                if(rs.next());

//                while (rs.next()){  // 헷갈리면 이거쓰셈
//                    Student studentDTO = new Student();
//                    studentDTO.setStudentId("id");
//                    studentDTO.setName("name");
//                    studentDTO.setStudentId("student_id");
//                    return studentDTO;
//                }

        }

        return null;
    }

}
