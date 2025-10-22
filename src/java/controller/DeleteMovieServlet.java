/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.MovieDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 *
 * @author thang
 */
public class DeleteMovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maStr = request.getParameter("ma");
        if (maStr == null || maStr.trim().isEmpty()) {
            response.sendRedirect("movie"); // Redirect if no ID
            return;
        }
        try {
            int maPhim = Integer.parseInt(maStr);
            MovieDAO dao = new MovieDAO();
            dao.deleteMovie(maPhim);
            response.sendRedirect("movie"); // Success: redirect to list
        } catch (NumberFormatException e) {
            response.sendRedirect("movie"); // Invalid ID: redirect to list
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Not needed for delete, but redirect to GET for safety
        doGet(request, response);
    }
}
