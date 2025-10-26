/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.MovieDAO;
import dal.RatingDAO;
import model.Movie;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author acer
 */
public class HomePageController extends HttpServlet {

    private MovieDAO movieDAO;
    private RatingDAO ratingDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
        ratingDAO = new RatingDAO();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            List<Movie> allMovies = movieDAO.getAllMovies();
            List<Movie> upcomingMovies = movieDAO.getUpcomingMovies();

            List<Movie> nowShowingMovies = allMovies.stream()
                .filter(m -> m.getNgayKhoiChieu() != null && !m.getNgayKhoiChieu().isAfter(java.time.LocalDateTime.now()))
                .limit(12)
                .toList();

            Map<Integer, Double> avgRatingsMap = new HashMap<>();
            Map<Integer, String> dateLabelMap = new HashMap<>();
            for (Movie m : allMovies) {
                Double avg = ratingDAO.getAverageRatingByMovie(m.getMaPhim());
                if (avg != null) {
                    avgRatingsMap.put(m.getMaPhim(), avg);
                }
                if (m.getNgayKhoiChieu() != null) {
                    String formatted = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            .format(m.getNgayKhoiChieu());
                    dateLabelMap.put(m.getMaPhim(), formatted);
                }
            }

            request.setAttribute("nowShowingMovies", nowShowingMovies);
            request.setAttribute("upcomingMovies", upcomingMovies.stream().limit(12).toList());
            request.setAttribute("allMovies", allMovies);
            request.setAttribute("avgRatingsMap", avgRatingsMap);
            request.setAttribute("dateLabelMap", dateLabelMap);

            request.getRequestDispatcher("Views/common/home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}