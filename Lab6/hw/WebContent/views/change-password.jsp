<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Change Password - Student Management System</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/views/styles/default.css">
        <style>
            .change-password-container {
                max-width: 400px;
                margin: 50px auto;
                padding: 20px;
            }
            .password-form {
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            .form-group {
                margin-bottom: 20px;
            }
            .form-group input {
                width: 100%;
                padding: 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 16px;
                box-sizing: border-box;
            }
            .form-group input:focus {
                border-color: #007bff;
                outline: none;
            }
            .btn-change-password {
                width: 100%;
                padding: 12px;
                background: #007bff;
                color: white;
                border: none;
                border-radius: 4px;
                font-size: 16px;
                cursor: pointer;
            }
            .btn-change-password:hover {
                background: #0056b3;
            }
            .back-link {
                display: block;
                text-align: center;
                margin-top: 15px;
                color: #6c757d;
                text-decoration: none;
            }
            .back-link:hover {
                color: #007bff;
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
                <li><a href="student" class="btn">Students</a>
                <li><a href="logout" class="btn-logout">Logout</a>
            </div>
        </div>

        <div class="container">
            <div class="change-password-container">
                <div class="password-form">
                    <h2 style="text-align: center; margin-bottom: 30px; color: #333;">Change Password</h2>

                    <!-- Success Message -->
                    <c:if test="${not empty message}">
                        <div class="message success" style="margin-bottom: 20px;">
                            ✅ ${message}
                        </div>
                    </c:if>

                    <!-- Error Message -->
                    <c:if test="${not empty error}">
                        <div class="message error" style="margin-bottom: 20px;">
                            ❌ ${error}
                        </div>
                    </c:if>

                    <form action="change-password" method="post">
                        <div class="form-group">
                            <input type="password" name="currentPassword" 
                                   placeholder="Current Password" required>
                        </div>

                        <div class="form-group">
                            <input type="password" name="newPassword" 
                                   placeholder="New Password (min 8 characters)" 
                                   minlength="8" required>
                        </div>

                        <div class="form-group">
                            <input type="password" name="confirmPassword" 
                                   placeholder="Confirm Password" 
                                   minlength="8" required>
                        </div>

                        <button type="submit" class="btn-change-password">
                            Change Password
                        </button>

                        <a href="dashboard" class="back-link">← Back to Dashboard</a>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>