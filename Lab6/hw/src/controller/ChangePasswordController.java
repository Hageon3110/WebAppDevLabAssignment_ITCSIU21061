package com.student.controller;

import com.student.dao.UserDAO;
import com.student.model.User;
import com.student.util.PasswordHashGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {
    
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show change password form
        request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // TODO: Get form parameters
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (currentPassword == null || currentPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }
        
        // TODO: Validate current password
        if (!userDAO.verifyPassword(currentUser.getId(), currentPassword)) {
            request.setAttribute("error", "Current password is incorrect.");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }
        
        // TODO: Validate new password length
        if (newPassword.length() < 8) {
            request.setAttribute("error", "New password must be at least 8 characters long.");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }
        
        // TODO: Validate password match
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New password and confirmation do not match.");
            request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
            return;
        }
        
        // TODO: Hash new password and update
        try {
            String hashedNewPassword = PasswordHashGenerator.hashPassword(newPassword);
            boolean success = userDAO.updatePassword(currentUser.getId(), hashedNewPassword);
            
            if (success) {
                request.setAttribute("message", "Password changed successfully!");
            } else {
                request.setAttribute("error", "Failed to change password. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while changing password.");
        }
        
        request.getRequestDispatcher("/views/change-password.jsp").forward(request, response);
    }
}