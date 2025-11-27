package com.student.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    
    /**
     * Create and add cookie to response
     * @param response HTTP response
     * @param name Cookie name
     * @param value Cookie value
     * @param maxAge Cookie lifetime in seconds
     */
    public static void createCookie(HttpServletResponse response, 
                                   String name, 
                                   String value, 
                                   int maxAge) {
        // TODO: Implement
        // 1. Create new Cookie with name and value
        if (response == null || name == null || name.trim().isEmpty()) {
            return;
        }
        
        try {
            Cookie cookie = new Cookie(name, value);
            
            // 2. Set maxAge
            cookie.setMaxAge(maxAge);
            
            // 3. Set path to "/"
            cookie.setPath("/");
            
            // 4. Set httpOnly to true (bảo mật)
            cookie.setHttpOnly(true);
            
            // 5. Add cookie to response
            response.addCookie(cookie);
            
        } catch (Exception e) {
            System.err.println("Error creating cookie: " + e.getMessage());
        }
    }
    
    /**
     * Get cookie value by name
     * @param request HTTP request
     * @param name Cookie name
     * @return Cookie value or null if not found
     */
    public static String getCookieValue(HttpServletRequest request, String name) {
        // TODO: Implement
        // 1. Get all cookies from request
        if (request == null || name == null || name.trim().isEmpty()) {
            return null;
        }
        
        Cookie[] cookies = request.getCookies();
        
        // 2. Loop through cookies
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 3. Find cookie with matching name
                if (name.equals(cookie.getName())) {
                    // 4. Return value or null
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    /**
     * Check if cookie exists
     * @param request HTTP request
     * @param name Cookie name
     * @return true if cookie exists
     */
    public static boolean hasCookie(HttpServletRequest request, String name) {
        // TODO: Implement
        return getCookieValue(request, name) != null;
    }
    
    /**
     * Delete cookie by setting max age to 0
     * @param response HTTP response
     * @param name Cookie name to delete
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        // TODO: Implement
        if (response == null || name == null || name.trim().isEmpty()) {
            return;
        }
        
        try {
            // 1. Create cookie with same name and empty value
            Cookie cookie = new Cookie(name, "");
            
            // 2. Set maxAge to 0
            cookie.setMaxAge(0);
            
            // 3. Set path to "/"
            cookie.setPath("/");
            
            // 4. Add to response
            response.addCookie(cookie);
            
        } catch (Exception e) {
            System.err.println("Error deleting cookie: " + e.getMessage());
        }
    }
    
    /**
     * Update existing cookie
     * @param response HTTP response
     * @param name Cookie name
     * @param newValue New cookie value
     * @param maxAge New max age
     */
    public static void updateCookie(HttpServletResponse response, 
                                   String name, 
                                   String newValue, 
                                   int maxAge) {
        // TODO: Implement
        // Simply create a new cookie with same name
        createCookie(response, name, newValue, maxAge);
    }
    
    /**
     * Create a session cookie (expires when browser closes)
     * @param response HTTP response
     * @param name Cookie name
     * @param value Cookie value
     */
    public static void createSessionCookie(HttpServletResponse response, 
                                         String name, 
                                         String value) {
        createCookie(response, name, value, -1);
    }
    
    /**
     * Create a persistent cookie with days
     * @param response HTTP response
     * @param name Cookie name
     * @param value Cookie value
     * @param days Number of days until expiration
     */
    public static void createPersistentCookie(HttpServletResponse response, 
                                            String name, 
                                            String value, 
                                            int days) {
        int maxAge = days * 24 * 60 * 60; // Convert days to seconds
        createCookie(response, name, value, maxAge);
    }
}