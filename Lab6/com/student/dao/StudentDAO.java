package com.student.dao;

import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    // Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get student by ID
    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                return student;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Add new student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_code = ?, full_name = ?, email = ?, major = ? WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search students
    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();

        // Use actual column names from your students table
        String sql = "SELECT * FROM students WHERE student_code LIKE ? OR full_name LIKE ? OR email LIKE ? OR major LIKE ? ORDER BY id DESC";

        // Add wildcards to keyword for partial matching
        String searchPattern = "%" + keyword + "%";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the same search pattern for all four placeholders
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setStudentCode(rs.getString("student_code"));
                student.setFullName(rs.getString("full_name"));
                student.setEmail(rs.getString("email"));
                student.setMajor(rs.getString("major"));
                student.setCreatedAt(rs.getTimestamp("created_at"));
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    // Sort Students

    public List<Student> getStudentsSorted(String sortBy, String order) {
        List<Student> students = new ArrayList<>();

        // Validate parameters
        String validatedSortBy = validateSortBy(sortBy);
        String validatedOrder = validateOrder(order);

        String sql = "SELECT * FROM students ORDER BY " + validatedSortBy + " " + validatedOrder;

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

// Filter Students by Major
    public List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students WHERE major = ? ORDER BY id DESC";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

// BONUS: Combined method for both filtering and sorting
    public List<Student> getStudentsFiltered(String major, String sortBy, String order) {
        List<Student> students = new ArrayList<>();

        // Validate parameters
        String validatedSortBy = validateSortBy(sortBy);
        String validatedOrder = validateOrder(order);

        String sql;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {

            if (major == null || major.trim().isEmpty() || "all".equalsIgnoreCase(major)) {
                // No major filter - just sort
                sql = "SELECT * FROM students ORDER BY " + validatedSortBy + " " + validatedOrder;
                pstmt = conn.prepareStatement(sql);
            } else {
                // Apply both major filter and sort
                sql = "SELECT * FROM students WHERE major = ? ORDER BY " + validatedSortBy + " " + validatedOrder;
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, major);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return students;
    }

// Validate sortBy parameter
    private String validateSortBy(String sortBy) {
        // List of allowed columns
        String[] validColumns = {"id", "student_code", "full_name", "email", "major"};

        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "id"; // default
        }

        // Check if sortBy is in validColumns
        for (String validColumn : validColumns) {
            if (validColumn.equalsIgnoreCase(sortBy)) {
                return validColumn; // return the validated column name
            }
        }

        return "id"; // default if invalid
    }

// Validate order parameter
    private String validateOrder(String order) {
        if (order == null || order.trim().isEmpty()) {
            return "ASC"; // default
        }

        if ("desc".equalsIgnoreCase(order)) {
            return "DESC";
        }

        return "ASC"; // default
    }

// Helper method to map ResultSet to Student object (to avoid code duplication)
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setStudentCode(rs.getString("student_code"));
        student.setFullName(rs.getString("full_name"));
        student.setEmail(rs.getString("email"));
        student.setMajor(rs.getString("major"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        return student;
    }

    // Get total count of students
    public int getTotalStudents() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM students";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

// Get paginated results
    public List<Student> getStudentsPaginated(int offset, int limit) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
// Get total count with search

    public int getTotalStudentsWithSearch(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM students WHERE student_code LIKE ? OR full_name LIKE ? OR email LIKE ? OR major LIKE ?";
        String searchPattern = "%" + keyword + "%";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

// Get total count with major filter
    public int getTotalStudentsByMajor(String major) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM students WHERE major = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

// Get paginated results with search
    public List<Student> getStudentsPaginatedWithSearch(int offset, int limit, String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_code LIKE ? OR full_name LIKE ? OR email LIKE ? OR major LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        String searchPattern = "%" + keyword + "%";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setInt(5, limit);
            pstmt.setInt(6, offset);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

// Get paginated results with major filter
    public List<Student> getStudentsPaginatedByMajor(int offset, int limit, String major) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE major = ? ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

// Get paginated results with sort
    public List<Student> getStudentsPaginatedWithSort(int offset, int limit, String sortBy, String order) {
        List<Student> students = new ArrayList<>();

        String validatedSortBy = validateSortBy(sortBy);
        String validatedOrder = validateOrder(order);

        String sql = "SELECT * FROM students ORDER BY " + validatedSortBy + " " + validatedOrder + " LIMIT ? OFFSET ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = mapResultSetToStudent(rs);
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Add this main method to test (remove after testing)
    public static void main(String[] args) {

        StudentDAO dao = new StudentDAO();
        List<Student> results = dao.searchStudents("john");
        System.out.println("Found " + results.size() + " students");
        for (Student s : results) {
            System.out.println(s);
        }
    }
}
