/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.MovieDAO;
import dal.RatingDAO;
import dal.SliderDAO;
import model.Movie;
import model.Slider;
import java.io.IOException;
import java.util.ArrayList;
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
    private SliderDAO sliderDAO;

    @Override
    public void init() throws ServletException {
        movieDAO = new MovieDAO();
        ratingDAO = new RatingDAO();
        sliderDAO = new SliderDAO();
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
            String q = request.getParameter("q");
            String genreIdStr = request.getParameter("genreId");
            String directorIdStr = request.getParameter("directorId");

            dal.GenreDAO genreDAO = new dal.GenreDAO();
            dal.DirectorDAO directorDAO = new dal.DirectorDAO();

            if (q != null && !q.trim().isEmpty()) {
                // SEARCH MODE
                List<Movie> movies = movieDAO.searchMovies(q.trim());

                Map<Integer, Double> avgRatingsMap = new HashMap<>();
                Map<Integer, String> dateLabelMap = new HashMap<>();
                for (Movie m : movies) {
                    Double avg = ratingDAO.getAverageRatingByMovie(m.getMaPhim());
                    if (avg != null) avgRatingsMap.put(m.getMaPhim(), avg);
                    if (m.getNgayKhoiChieu() != null) {
                        String formatted = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                .format(m.getNgayKhoiChieu());
                        dateLabelMap.put(m.getMaPhim(), formatted);
                    }
                }

                // Lấy danh sách slider đang hiển thị
                System.out.println("[HomePageController] [SEARCH MODE] Getting active sliders...");
                List<Slider> activeSliders = sliderDAO.getActiveSliders();
                System.out.println("[HomePageController] [SEARCH MODE] Received " + activeSliders.size() + " active sliders from DAO");

                request.setAttribute("searchQuery", q.trim());
                request.setAttribute("movies", movies);
                request.setAttribute("nowShowingMovies", movies); // dùng tab hiện tại hiển thị kết quả
                request.setAttribute("upcomingMovies", new ArrayList<Movie>()); // rỗng khi search
                request.setAttribute("allMovies", movies);
                request.setAttribute("avgRatingsMap", avgRatingsMap);
                request.setAttribute("dateLabelMap", dateLabelMap);
                request.setAttribute("sliders", activeSliders);
                System.out.println("[HomePageController] [SEARCH MODE] Set 'sliders' attribute with " + activeSliders.size() + " items");

                request.getRequestDispatcher("Views/common/home.jsp").forward(request, response);
                return;
            }

            // DEFAULT/FILTERED HOME
            List<Movie> nowShowingMovies;
            List<Movie> upcomingMovies;
            if (genreIdStr != null && !genreIdStr.isBlank()) {
                int gid = Integer.parseInt(genreIdStr);
                List<Movie> filtered = movieDAO.getMoviesByGenreId(gid);
                request.setAttribute("activeGenre", genreDAO.getById(gid));
                nowShowingMovies = new java.util.ArrayList<>();
                upcomingMovies = new java.util.ArrayList<>();
                for (Movie m : filtered) {
                    if ("Đang chiếu".equals(m.getTrangThai())) nowShowingMovies.add(m);
                    else if ("Sắp chiếu".equals(m.getTrangThai())) upcomingMovies.add(m);
                }
            } else if (directorIdStr != null && !directorIdStr.isBlank()) {
                int did = Integer.parseInt(directorIdStr);
                List<Movie> filtered = movieDAO.getMoviesByDirectorId(did);
                request.setAttribute("activeDirector", directorDAO.getById(did));
                nowShowingMovies = new java.util.ArrayList<>();
                upcomingMovies = new java.util.ArrayList<>();
                for (Movie m : filtered) {
                    if ("Đang chiếu".equals(m.getTrangThai())) nowShowingMovies.add(m);
                    else if ("Sắp chiếu".equals(m.getTrangThai())) upcomingMovies.add(m);
                }
            } else {
                nowShowingMovies = movieDAO.getNowShowingMovies();
                upcomingMovies = movieDAO.getComingSoonMovies();
            }
            
            // Giới hạn số lượng hiển thị
            if (nowShowingMovies.size() > 12) {
                nowShowingMovies = nowShowingMovies.subList(0, 12);
            }
            if (upcomingMovies.size() > 12) {
                upcomingMovies = upcomingMovies.subList(0, 12);
            }
            
            // Tạo allMovies để hiển thị tất cả (cho tab "Tất cả")
            List<Movie> allMovies = new ArrayList<>();
            allMovies.addAll(nowShowingMovies);
            allMovies.addAll(upcomingMovies);

            Map<Integer, Double> avgRatingsMap = new HashMap<>();
            Map<Integer, String> dateLabelMap = new HashMap<>();
            
            // Tính rating và format date cho tất cả phim (nowShowing + upcoming)
            List<Movie> allMoviesForRating = new ArrayList<>();
            allMoviesForRating.addAll(nowShowingMovies);
            allMoviesForRating.addAll(upcomingMovies);
            
            for (Movie m : allMoviesForRating) {
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

            // Lấy danh sách slider đang hiển thị
            System.out.println("[HomePageController] Getting active sliders...");
            List<Slider> activeSliders = sliderDAO.getActiveSliders();
            System.out.println("[HomePageController] Received " + activeSliders.size() + " active sliders from DAO");
            
            if (activeSliders.isEmpty()) {
                System.out.println("[HomePageController] WARNING: No active sliders found!");
            } else {
                System.out.println("[HomePageController] Sliders to display:");
                for (int i = 0; i < activeSliders.size(); i++) {
                    Slider s = activeSliders.get(i);
                    System.out.println("[HomePageController]   [" + (i+1) + "] " + s.getTieuDe() + " (MaSlider=" + s.getMaSlider() + ", AnhSlide=" + s.getAnhSlide() + ")");
                }
            }

            request.setAttribute("nowShowingMovies", nowShowingMovies);
            request.setAttribute("upcomingMovies", upcomingMovies);
            request.setAttribute("allMovies", allMovies);
            request.setAttribute("avgRatingsMap", avgRatingsMap);
            request.setAttribute("dateLabelMap", dateLabelMap);
            request.setAttribute("sliders", activeSliders);
            System.out.println("[HomePageController] Set 'sliders' attribute with " + activeSliders.size() + " items");

            // Map Movie -> Genres/Directors (để hiển thị button)
            java.util.Map<Integer, java.util.List<model.Genre>> movieIdToGenres = new java.util.HashMap<>();
            java.util.Map<Integer, java.util.List<model.Director>> movieIdToDirectors = new java.util.HashMap<>();
            for (Movie m : allMovies) {
                java.util.List<Integer> gids = genreDAO.getGenreIdsByMovie(m.getMaPhim());
                java.util.List<model.Genre> gs = new java.util.ArrayList<>();
                for (Integer id : gids) {
                    model.Genre g = genreDAO.getById(id);
                    if (g != null) gs.add(g);
                }
                movieIdToGenres.put(m.getMaPhim(), gs);

                java.util.List<Integer> dids = directorDAO.getDirectorIdsByMovie(m.getMaPhim());
                java.util.List<model.Director> ds = new java.util.ArrayList<>();
                for (Integer id : dids) {
                    model.Director d = directorDAO.getById(id);
                    if (d != null) ds.add(d);
                }
                movieIdToDirectors.put(m.getMaPhim(), ds);
            }
            request.setAttribute("movieIdToGenres", movieIdToGenres);
            request.setAttribute("movieIdToDirectors", movieIdToDirectors);

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