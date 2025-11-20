/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.util;

/**
 *
 * @author Lenovo
 */

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        String plainPassword = "password123";
        String adminPassword = "admin";
        String JohnPassword = "12345678";
        String JanePassword = "abcdef";
        
        // Generate hash
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        String adminhashedPassword = BCrypt.hashpw(adminPassword, BCrypt.gensalt());
        String JohnhashedPassword = BCrypt.hashpw(JohnPassword, BCrypt.gensalt());
        String JanehashedPassword = BCrypt.hashpw(JanePassword, BCrypt.gensalt());
        
        
        System.out.println("Plain Password: " + plainPassword);
        System.out.println("Hashed Password: " + hashedPassword);
        System.out.println("admin Password: " + adminPassword);
        System.out.println("admin Hashed Password: " + adminhashedPassword);
        System.out.println("John Password: " + JohnPassword);
        System.out.println("John Hashed Password: " + JohnhashedPassword);
        System.out.println("Jane Password: " + JanePassword);
        System.out.println("Jane Hashed Password: " + JanehashedPassword);
        System.out.println("\nCopy the hashed password to your INSERT statement");
        
        // Test verification
        boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
        System.out.println("\nVerification test: " + matches);
    }
}
