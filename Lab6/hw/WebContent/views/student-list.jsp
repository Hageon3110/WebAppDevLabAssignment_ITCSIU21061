<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/default.css">
        <title>Student List - MVC</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
            }

            .container {
                max-width: 1200px;
                margin: 0 auto;
                background: white;
                border-radius: 10px;
                padding: 30px;
                box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            }

            h1 {
                color: #333;
                margin-bottom: 10px;
                font-size: 32px;
            }

            .subtitle {
                color: #666;
                margin-bottom: 30px;
                font-style: italic;
            }

            .message {
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 5px;
                font-weight: 500;
            }

            .success {
                background-color: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }

            .error {
                background-color: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }

            .info {
                background-color: #d1ecf1;
                color: #0c5460;
                border: 1px solid #bee5eb;
            }

            .btn {
                padding: 8px 20px;
                background: #F5C857;
                color: black;
                text-decoration: none;
                border-radius: 5px;
                font-size: 14px;
            
                display: inline-block;
                font-weight: 500;
                transition: all 0.3s;
                border: none;
                cursor: pointer;
            }

            .btn-primary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }

            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }

            .btn-secondary {
                background-color: #6c757d;
                color: white;
            }

            .btn-success {
                background-color: #28a745;
                color: white;
            }

            .btn-outline {
                background: transparent;
                border: 2px solid #667eea;
                color: #667eea;
            }

            .btn-outline:hover {
                background: #667eea;
                color: white;
            }

            .btn-danger {
                background-color: #dc3545;
                color: white;
                padding: 8px 16px;
                font-size: 13px;
            }

            .btn-danger:hover {
                background-color: #c82333;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }

            thead {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }

            th, td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                font-weight: 600;
                text-transform: uppercase;
                font-size: 13px;
                letter-spacing: 0.5px;
            }

            tbody tr {
                transition: background-color 0.2s;
            }

            tbody tr:hover {
                background-color: #f8f9fa;
            }

            .actions {
                display: flex;
                gap: 10px;
            }

            .empty-state {
                text-align: center;
                padding: 60px 20px;
                color: #999;
            }

            .empty-state-icon {
                font-size: 64px;
                margin-bottom: 20px;
            }

            /* Search Form Styles */
            .search-section {
                background: #f8f9fa;
                padding: 25px;
                border-radius: 8px;
                margin-bottom: 25px;
                border: 1px solid #e9ecef;
            }

            .search-title {
                font-size: 18px;
                font-weight: 600;
                color: #495057;
                margin-bottom: 15px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .search-form {
                display: flex;
                gap: 12px;
                align-items: center;
                flex-wrap: wrap;
            }

            .search-input {
                flex: 1;
                min-width: 300px;
                padding: 12px 16px;
                border: 2px solid #e9ecef;
                border-radius: 6px;
                font-size: 14px;
                transition: all 0.3s;
            }

            .search-input:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .search-input::placeholder {
                color: #6c757d;
            }

            .action-buttons {
                display: flex;
                gap: 15px;
                margin-bottom: 20px;
                flex-wrap: wrap;
            }

            .stats {
                background: #e7f3ff;
                padding: 12px 20px;
                border-radius: 6px;
                margin-top: 15px;
                border-left: 4px solid #667eea;
                font-size: 14px;
                color: #495057;
            }

            @media (max-width: 768px) {
                .search-form {
                    flex-direction: column;
                    align-items: stretch;
                }

                .search-input {
                    min-width: auto;
                }

                .action-buttons {
                    flex-direction: column;
                }

                .btn {
                    text-align: center;
                }
            }

            /* New Styles for Sort & Filter */
            .sort-filter-section {
                background: #f8f9fa;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                border: 1px solid #e9ecef;
            }

            .filter-form {
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
            }

            .filter-group {
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .filter-label {
                font-weight: 600;
                color: #495057;
                white-space: nowrap;
            }

            .filter-select {
                padding: 10px 12px;
                border: 2px solid #e9ecef;
                border-radius: 6px;
                background: white;
                min-width: 200px;
            }

            .sortable-header {
                cursor: pointer;
                transition: background-color 0.2s;
                padding-right: 20px !important;
                position: relative;
            }

            .sortable-header:hover {
                background: rgba(255, 255, 255, 0.2);
            }

            .sort-indicator {
                position: absolute;
                right: 8px;
                top: 50%;
                transform: translateY(-50%);
                font-size: 12px;
            }

            .current-sort {
                background: rgba(255, 255, 255, 0.3);
            }

            .filter-active {
                background: #e7f3ff;
                padding: 15px;
                border-radius: 6px;
                margin-top: 10px;
                border-left: 4px solid #28a745;
            }

        </style>
    </head>
    <body>
        <!-- Navigation Bar -->
        <div class="navbar">
            <h2>📚 Student Management System</h2>
            <div class="navbar-right">
                <div class="user-info">
                    <span>Welcome, ${sessionScope.fullName}</span>
                    <span class="role-badge role-${sessionScope.role}">
                        ${sessionScope.role}
                    </span>
                </div>

                <li><a href="dashboard" class="btn">Dashboard</a>
                <li><a href="logout" class="btn-logout">Logout</a>
                <li><a href="change-password" class="btn">Change Password</a>

            </div>
        </div>

        <div class="container">
            <h1>📚 Student Management System</h1>
            <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>

            <!-- Success Message -->
            <c:if test="${not empty param.message}">
                <div class="message success">
                    ✅ ${param.message}
                </div>
            </c:if>

            <!-- Error Message -->
            <c:if test="${not empty param.error}">
                <div class="message error">
                    ❌ ${param.error}
                </div>
            </c:if>

            <!-- Search Message -->
            <c:if test="${not empty searchMessage}">
                <div class="message info">
                    🔍 ${searchMessage}
                </div>
            </c:if>

            <!-- List/Sort/Filter Message -->
            <c:if test="${not empty listMessage}">
                <div class="message info">
                    📊 ${listMessage}
                </div>
            </c:if>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <c:if test="${sessionScope.role eq 'admin'}">
                    <a href="student?action=new" class="btn btn-primary">
                        ➕ Add New Student
                    </a>
                </c:if>
                <a href="student" class="btn btn-outline">
                    📋 Show All Students
                </a>
            </div>

            <!-- Search Section -->
            <div class="search-section">
                <div class="search-title">
                    🔍 Search Students
                </div>
                <form action="student" method="get" class="search-form">
                    <input type="hidden" name="action" value="search">
                    <input type="text" 
                           name="keyword" 
                           class="search-input" 
                           placeholder="Search by student code, name, email, or major..." 
                           value="${searchKeyword}">
                    <button type="submit" class="btn btn-success">Search</button>
                    <a href="student" class="btn btn-secondary">Clear</a>
                </form>

                <!-- Search Stats -->
                <c:if test="${not empty students and not empty searchKeyword}">
                    <div class="stats">
                        📊 Found <strong>${students.size()}</strong> student(s) matching "<strong>${searchKeyword}</strong>"
                    </div>
                </c:if>
            </div>

            <!-- Sort & Filter Section -->
            <div class="sort-filter-section">
                <div class="filter-form">
                    <!-- Major Filter -->
                    <div class="filter-group">
                        <span class="filter-label">Filter by Major:</span>
                        <form action="student" method="get" style="display: inline;">
                            <input type="hidden" name="action" value="list">
                            <select name="major" class="filter-select" onchange="this.form.submit()">
                                <option value="">All Majors</option>
                                <option value="Computer Science" ${currentMajor == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                                <option value="Information Technology" ${currentMajor == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                                <option value="Software Engineering" ${currentMajor == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                                <option value="Data Science" ${currentMajor == 'Data Science' ? 'selected' : ''}>Data Science</option>
                                <option value="Cybersecurity" ${currentMajor == 'Cybersecurity' ? 'selected' : ''}>Cybersecurity</option>
                                <option value="Artificial Intelligence" ${currentMajor == 'Artificial Intelligence' ? 'selected' : ''}>Artificial Intelligence</option>
                            </select>
                        </form>
                    </div>

                    <!-- Clear Filters -->
                    <c:if test="${not empty currentMajor or not empty currentSortBy}">
                        <div class="filter-group">
                            <a href="student" class="btn btn-secondary">Clear All Filters</a>
                        </div>
                    </c:if>
                </div>

                <!-- Active Filter Display -->
                <c:if test="${not empty currentMajor}">
                    <div class="filter-active">
                        <strong>Active Filter:</strong> Major = "${currentMajor}"
                        <a href="student" style="margin-left: 10px; color: #dc3545;">✕ Remove</a>
                    </div>
                </c:if>
            </div>

            <!-- Student Table -->
            <c:choose>
                <c:when test="${not empty students}">
                    <table>
                        <thead>
                            <tr>
                                <!-- ID Column -->
                                <th class="sortable-header ${currentSortBy == 'id' ? 'current-sort' : ''}">
                                    <a href="student?action=list&sortBy=id&order=${currentSortBy == 'id' and currentOrder == 'asc' ? 'desc' : 'asc'}${not empty currentMajor ? '&major=' += currentMajor : ''}" 
                                       style="color: inherit; text-decoration: none;">
                                        ID
                                        <c:if test="${currentSortBy == 'id'}">
                                            <span class="sort-indicator">${currentOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>

                                <!-- Student Code Column -->
                                <th class="sortable-header ${currentSortBy == 'student_code' ? 'current-sort' : ''}">
                                    <a href="student?action=list&sortBy=student_code&order=${currentSortBy == 'student_code' and currentOrder == 'asc' ? 'desc' : 'asc'}${not empty currentMajor ? '&major=' += currentMajor : ''}" 
                                       style="color: inherit; text-decoration: none;">
                                        Student Code
                                        <c:if test="${currentSortBy == 'student_code'}">
                                            <span class="sort-indicator">${currentOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>

                                <!-- Full Name Column -->
                                <th class="sortable-header ${currentSortBy == 'full_name' ? 'current-sort' : ''}">
                                    <a href="student?action=list&sortBy=full_name&order=${currentSortBy == 'full_name' and currentOrder == 'asc' ? 'desc' : 'asc'}${not empty currentMajor ? '&major=' += currentMajor : ''}" 
                                       style="color: inherit; text-decoration: none;">
                                        Full Name
                                        <c:if test="${currentSortBy == 'full_name'}">
                                            <span class="sort-indicator">${currentOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>

                                <!-- Email Column -->
                                <th class="sortable-header ${currentSortBy == 'email' ? 'current-sort' : ''}">
                                    <a href="student?action=list&sortBy=email&order=${currentSortBy == 'email' and currentOrder == 'asc' ? 'desc' : 'asc'}${not empty currentMajor ? '&major=' += currentMajor : ''}" 
                                       style="color: inherit; text-decoration: none;">
                                        Email
                                        <c:if test="${currentSortBy == 'email'}">
                                            <span class="sort-indicator">${currentOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>

                                <!-- Major Column -->
                                <th class="sortable-header ${currentSortBy == 'major' ? 'current-sort' : ''}">
                                    <a href="student?action=list&sortBy=major&order=${currentSortBy == 'major' and currentOrder == 'asc' ? 'desc' : 'asc'}${not empty currentMajor ? '&major=' += currentMajor : ''}" 
                                       style="color: inherit; text-decoration: none;">
                                        Major
                                        <c:if test="${currentSortBy == 'major'}">
                                            <span class="sort-indicator">${currentOrder == 'asc' ? '▲' : '▼'}</span>
                                        </c:if>
                                    </a>
                                </th>

                                <c:if test="${sessionScope.role eq 'admin'}">
                                    <th>Actions</th>
                                    </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="student" items="${students}">
                                <tr>
                                    <td>${student.id}</td>
                                    <td><strong>${student.studentCode}</strong></td>
                                    <td>${student.fullName}</td>
                                    <td>${student.email}</td>
                                    <td>
                                        <span style="padding: 4px 8px; background: #e9ecef; border-radius: 4px; font-size: 12px;">
                                            ${student.major}
                                        </span>
                                    </td>
                                    <c:if test="${sessionScope.role eq 'admin'}">
                                        <td>
                                            <div class="actions">
                                                <a href="student?action=edit&id=${student.id}" class="btn btn-secondary">
                                                    ✏️ Edit
                                                </a>
                                                <a href="student?action=delete&id=${student.id}" 
                                                   class="btn btn-danger"
                                                   onclick="return confirm('Are you sure you want to delete this student?')">
                                                    🗑️ Delete
                                                </a>
                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <!-- Results Count -->
                    <div class="stats" style="margin-top: 20px;">
                        📊 Showing <strong>${students.size()}</strong> student(s)
                        <c:if test="${not empty currentMajor}">
                            in <strong>${currentMajor}</strong>
                        </c:if>
                        <c:if test="${not empty currentSortBy}">
                            sorted by <strong>${currentSortBy}</strong> in <strong>${currentOrder == 'asc' ? 'ascending' : 'descending'}</strong> order
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-state-icon">
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">🔍</c:when>
                                <c:when test="${not empty currentMajor}">📊</c:when>
                                <c:otherwise>📭</c:otherwise>
                            </c:choose>
                        </div>
                        <h3>
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">
                                    No students found for "${searchKeyword}"
                                </c:when>
                                <c:when test="${not empty currentMajor}">
                                    No students found in major "${currentMajor}"
                                </c:when>
                                <c:otherwise>
                                    No students found
                                </c:otherwise>
                            </c:choose>
                        </h3>
                        <p>
                            <c:choose>
                                <c:when test="${not empty searchKeyword}">
                                    Try adjusting your search terms or <a href="student">view all students</a>
                                </c:when>
                                <c:when test="${not empty currentMajor}">
                                    Try selecting a different major or <a href="student">view all students</a>
                                </c:when>
                                <c:otherwise>
                                    Start by adding a new student
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>