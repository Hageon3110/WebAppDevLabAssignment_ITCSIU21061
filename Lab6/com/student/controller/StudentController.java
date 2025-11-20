package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            case "search":
                searchStudents(request, response);
                break;
            case "sort":           // ← Sẽ gọi listStudents với sort params
                
            case "filter":         // ← Sẽ gọi listStudents với filter params  
            default:               // ← "list" cũng gọi listStudents
                listStudents(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
        }
    }

// Modified listStudents with pagination support
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get all possible parameters
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String major = request.getParameter("major");
        String searchKeyword = request.getParameter("keyword");

        // Pagination parameters
        String pageParam = request.getParameter("page");
        int recordsPerPage = 5; // Smaller number for testing, change to 10 for production

        // Get current page with validation
        int currentPage = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Calculate offset
        int offset = (currentPage - 1) * recordsPerPage;

        List<Student> students;
        int totalRecords;
        String message = "";

        // Decision logic based on parameters
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            // SEARCH + PAGINATION
            students = studentDAO.getStudentsPaginatedWithSearch(offset, recordsPerPage, searchKeyword);
            totalRecords = studentDAO.getTotalStudentsWithSearch(searchKeyword);
            message = "Search results for: '" + searchKeyword + "'";
            request.setAttribute("searchKeyword", searchKeyword);

        } else if (sortBy != null && !sortBy.trim().isEmpty()) {
            // SORT + PAGINATION (with optional major filter)
            if (major != null && !major.trim().isEmpty() && !"all".equalsIgnoreCase(major)) {
                // SORT + FILTER + PAGINATION
                students = studentDAO.getStudentsPaginatedByMajor(offset, recordsPerPage, major);
                totalRecords = studentDAO.getTotalStudentsByMajor(major);
                message = major + " students sorted by " + sortBy + " in "
                        + ("desc".equalsIgnoreCase(order) ? "descending" : "ascending") + " order";
            } else {
                // SORT ONLY + PAGINATION
                students = studentDAO.getStudentsPaginatedWithSort(offset, recordsPerPage, sortBy, order);
                totalRecords = studentDAO.getTotalStudents();
                message = "Students sorted by " + sortBy + " in "
                        + ("desc".equalsIgnoreCase(order) ? "descending" : "ascending") + " order";
            }

        } else if (major != null && !major.trim().isEmpty() && !"all".equalsIgnoreCase(major)) {
            // FILTER ONLY + PAGINATION
            students = studentDAO.getStudentsPaginatedByMajor(offset, recordsPerPage, major);
            totalRecords = studentDAO.getTotalStudentsByMajor(major);
            message = "Showing students in major: " + major;

        } else {
            // DEFAULT: PAGINATION ONLY
            students = studentDAO.getStudentsPaginated(offset, recordsPerPage);
            totalRecords = studentDAO.getTotalStudents();
            message = "Showing all students";
        }

        // Calculate total pages with edge case handling
        int totalPages = 0;
        if (totalRecords > 0) {
            totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        }

        // Handle case where currentPage > totalPages
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
            // Recalculate with corrected page
            offset = (currentPage - 1) * recordsPerPage;

            // Re-fetch data for the corrected page
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                students = studentDAO.getStudentsPaginatedWithSearch(offset, recordsPerPage, searchKeyword);
            } else if (major != null && !major.trim().isEmpty() && !"all".equalsIgnoreCase(major)) {
                students = studentDAO.getStudentsPaginatedByMajor(offset, recordsPerPage, major);
            } else {
                students = studentDAO.getStudentsPaginated(offset, recordsPerPage);
            }
        }

        // Set request attributes
        request.setAttribute("students", students);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        // Set filter/sort attributes
        request.setAttribute("currentSortBy", sortBy);
        request.setAttribute("currentOrder", order);
        request.setAttribute("currentMajor", major);

        // Only set message if we're doing something special
        if (searchKeyword != null || sortBy != null || (major != null && !"all".equalsIgnoreCase(major))) {
            request.setAttribute("listMessage", message);
        }

        // Forward to list view
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Show form for new student
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // Show form for editing student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);

        request.setAttribute("student", existingStudent);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }

// Insert new student (updated with validation)
    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student newStudent = new Student(studentCode, fullName, email, major);

        // Validate before inserting
        if (!validateStudent(newStudent, request)) {
            // Validation failed - return to form with error messages
            request.setAttribute("student", newStudent);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Validation passed - proceed with insertion
        if (studentDAO.addStudent(newStudent)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

// Update student (updated with validation)
    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student student = new Student(studentCode, fullName, email, major);
        student.setId(id);

        // Validate before updating
        if (!validateStudent(student, request)) {
            // Validation failed - return to form with error messages
            request.setAttribute("student", student);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Validation passed - proceed with update
        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    // Delete student
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }

// Search students with pagination
    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get keyword and pagination parameters
        String keyword = request.getParameter("keyword");
        String pageParam = request.getParameter("page");
        int recordsPerPage = 5;

        // Get current page
        int currentPage = 1;
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int offset = (currentPage - 1) * recordsPerPage;

        List<Student> students;
        int totalRecords;

        if (keyword == null || keyword.trim().isEmpty()) {
            // If no keyword, get all students with pagination
            students = studentDAO.getStudentsPaginated(offset, recordsPerPage);
            totalRecords = studentDAO.getTotalStudents();
            request.setAttribute("searchMessage", "Showing all students");
        } else {
            // Search with pagination
            students = studentDAO.getStudentsPaginatedWithSearch(offset, recordsPerPage, keyword);
            totalRecords = studentDAO.getTotalStudentsWithSearch(keyword);
            request.setAttribute("searchMessage", "Found " + totalRecords + " student(s) for keyword: '" + keyword + "'");
            request.setAttribute("searchKeyword", keyword);
        }

        // Calculate total pages
        int totalPages = 0;
        if (totalRecords > 0) {
            totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        }

        // Handle page overflow
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
            offset = (currentPage - 1) * recordsPerPage;

            if (keyword == null || keyword.trim().isEmpty()) {
                students = studentDAO.getStudentsPaginated(offset, recordsPerPage);
            } else {
                students = studentDAO.getStudentsPaginatedWithSearch(offset, recordsPerPage, keyword);
            }
        }

        // Set pagination attributes
        request.setAttribute("students", students);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalRecords", totalRecords);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Validation method for Student
    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;

        // Validate student code
        String studentCode = student.getStudentCode();
        if (studentCode == null || studentCode.trim().isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else {
            String codePattern = "[A-Z]{2}[0-9]{3,}";
            if (!studentCode.matches(codePattern)) {
                request.setAttribute("errorCode", "Invalid format. Use 2 uppercase letters + 3+ digits (e.g., SV001, IT123)");
                isValid = false;
            }
        }

        // Validate full name
        String fullName = student.getFullName();
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        } else if (fullName.trim().length() < 2) {
            request.setAttribute("errorName", "Full name must be at least 2 characters long");
            isValid = false;
        }

        // Validate email (only if provided)
        String email = student.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailPattern)) {
                request.setAttribute("errorEmail", "Invalid email format");
                isValid = false;
            }
        }

        // Validate major
        String major = student.getMajor();
        if (major == null || major.trim().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return isValid;
    }
    // Sort Students

    private void sortStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get sort parameters
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        // 2. Call DAO method
        List<Student> students = studentDAO.getStudentsSorted(sortBy, order);

        // 3. Set results and parameters as attributes
        request.setAttribute("students", students);
        request.setAttribute("currentSortBy", sortBy);
        request.setAttribute("currentOrder", order);
        request.setAttribute("sortMessage", "Students sorted by "
                + (sortBy != null ? sortBy : "id") + " in "
                + ("desc".equalsIgnoreCase(order) ? "descending" : "ascending") + " order");

        // 4. Forward to list view
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

// Filter Students by Major
    private void filterStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get major parameter
        String major = request.getParameter("major");

        // 2. Call DAO method
        List<Student> students;
        String filterMessage;

        if (major == null || major.trim().isEmpty() || "all".equalsIgnoreCase(major)) {
            // If no major or "all", get all students
            students = studentDAO.getAllStudents();
            filterMessage = "Showing all students";
        } else {
            // Filter by specific major
            students = studentDAO.getStudentsByMajor(major);
            filterMessage = "Showing students in major: " + major + " (" + students.size() + " found)";
        }

        // 3. Set results and parameters as attributes
        request.setAttribute("students", students);
        request.setAttribute("currentMajor", major);
        request.setAttribute("filterMessage", filterMessage);

        // 4. Forward to list view
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }
    // Helper method to build URL with pagination parameters

    private String buildUrlWithPagination(HttpServletRequest request, int page) {
        StringBuilder url = new StringBuilder("student?");

        // Add action if present
        String action = request.getParameter("action");
        if (action != null && !action.equals("list")) {
            url.append("action=").append(action).append("&");
        }

        // Add search keyword if present
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            url.append("keyword=").append(keyword).append("&");
        }

        // Add major filter if present
        String major = request.getParameter("major");
        if (major != null && !major.trim().isEmpty() && !"all".equals(major)) {
            url.append("major=").append(major).append("&");
        }

        // Add sort parameters if present
        String sortBy = request.getParameter("sortBy");
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            url.append("sortBy=").append(sortBy).append("&");

            String order = request.getParameter("order");
            if (order != null && !order.trim().isEmpty()) {
                url.append("order=").append(order).append("&");
            }
        }

        // Add page parameter
        url.append("page=").append(page);

        return url.toString();
    }

}
