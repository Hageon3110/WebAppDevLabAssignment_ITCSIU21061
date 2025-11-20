<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${student.id == null ? 'Add Student' : 'Edit Student'}"/> - MVC</title>
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
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        
        h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
            text-align: center;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
        }
        
        .required::after {
            content: " *";
            color: #dc3545;
        }
        
        input, select {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #e9ecef;
            border-radius: 6px;
            font-size: 14px;
            transition: all 0.3s;
        }
        
        input:focus, select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .error {
            color: #dc3545;
            font-size: 14px;
            display: block;
            margin-top: 5px;
            font-weight: 500;
        }
        
        .error-field {
            border-color: #dc3545 !important;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
            margin-right: 10px;
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
        
        .btn-secondary:hover {
            background-color: #5a6268;
        }
        
        .form-actions {
            text-align: center;
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #e9ecef;
        }
        
        .help-text {
            color: #6c757d;
            font-size: 12px;
            margin-top: 5px;
            display: block;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>
            <c:choose>
                <c:when test="${student.id == null}">➕ Add New Student</c:when>
                <c:otherwise>✏️ Edit Student</c:otherwise>
            </c:choose>
        </h1>
        
        <form action="student" method="post">
            <input type="hidden" name="action" value="${student.id == null ? 'insert' : 'update'}">
            <c:if test="${student.id != null}">
                <input type="hidden" name="id" value="${student.id}">
            </c:if>

            <!-- Student Code Field -->
            <div class="form-group">
                <label for="studentCode" class="required">Student Code</label>
                <input type="text" id="studentCode" name="studentCode" 
                       value="${student.studentCode}"
                       class="${not empty errorCode ? 'error-field' : ''}"
                       placeholder="e.g., SV001, IT123">
                <c:if test="${not empty errorCode}">
                    <span class="error">❌ ${errorCode}</span>
                </c:if>
                <span class="help-text">Format: 2 uppercase letters + 3 or more digits (e.g., SV001, IT123)</span>
            </div>

            <!-- Full Name Field -->
            <div class="form-group">
                <label for="fullName" class="required">Full Name</label>
                <input type="text" id="fullName" name="fullName" 
                       value="${student.fullName}"
                       class="${not empty errorName ? 'error-field' : ''}"
                       placeholder="Enter full name">
                <c:if test="${not empty errorName}">
                    <span class="error">❌ ${errorName}</span>
                </c:if>
                <span class="help-text">Minimum 2 characters required</span>
            </div>

            <!-- Email Field -->
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" 
                       value="${student.email}"
                       class="${not empty errorEmail ? 'error-field' : ''}"
                       placeholder="Enter email address">
                <c:if test="${not empty errorEmail}">
                    <span class="error">❌ ${errorEmail}</span>
                </c:if>
                <span class="help-text">Optional field</span>
            </div>

            <!-- Major Field -->
            <div class="form-group">
                <label for="major" class="required">Major</label>
                <select id="major" name="major" class="${not empty errorMajor ? 'error-field' : ''}">
                    <option value="">-- Select Major --</option>
                    <option value="Computer Science" ${student.major == 'Computer Science' ? 'selected' : ''}>Computer Science</option>
                    <option value="Information Technology" ${student.major == 'Information Technology' ? 'selected' : ''}>Information Technology</option>
                    <option value="Software Engineering" ${student.major == 'Software Engineering' ? 'selected' : ''}>Software Engineering</option>
                    <option value="Data Science" ${student.major == 'Data Science' ? 'selected' : ''}>Data Science</option>
                    <option value="Cybersecurity" ${student.major == 'Cybersecurity' ? 'selected' : ''}>Cybersecurity</option>
                    <option value="Artificial Intelligence" ${student.major == 'Artificial Intelligence' ? 'selected' : ''}>Artificial Intelligence</option>
                </select>
                <c:if test="${not empty errorMajor}">
                    <span class="error">❌ ${errorMajor}</span>
                </c:if>
            </div>

            <!-- Form Actions -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <c:choose>
                        <c:when test="${student.id == null}">Add Student</c:when>
                        <c:otherwise>Update Student</c:otherwise>
                    </c:choose>
                </button>
                <a href="student" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>