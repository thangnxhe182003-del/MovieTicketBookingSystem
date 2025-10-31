package controller;

import dal.MovieDAO;
import dal.RatingDAO;
import model.Movie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search", "/search-ajax"})
public class SearchServlet extends HttpServlet {
    private final MovieDAO movieDAO = new MovieDAO();
    private final RatingDAO ratingDAO = new RatingDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/search-ajax".equals(path)) {
            handleAjax(req, resp);
        } else {
            String q = req.getParameter("q");
            if (q == null || q.trim().isEmpty()) {
                resp.sendRedirect("home");
                return;
            }
            req.setAttribute("movies", movieDAO.searchMovies(q.trim()));
            req.setAttribute("searchQuery", q);
            req.getRequestDispatcher("Views/common/search-results.jsp").forward(req, resp);
        }
    }

    private void handleAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String q = req.getParameter("q");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (q == null || q.trim().isEmpty()) {
            out.print("");
            return;
        }

        List<Movie> movies = movieDAO.searchMovies(q.trim());
        if (movies.isEmpty()) {
            out.print("<div style='padding:16px; color:#999; text-align:center;'>Đang gõ...</div>");
            return;
        }

        for (Movie m : movies) {
            Double rating = ratingDAO.getAverageRatingByMovie(m.getMaPhim());
            String rate = rating != null ? "⭐ " + Math.round(rating * 10.0) / 10.0 : "";
            String date = m.getNgayKhoiChieu() != null ? "📅 " + m.getNgayKhoiChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";

            out.print(
                "<a href='movie?action=detail&maPhim=" + m.getMaPhim() + "' " +
                "style='display:flex; gap:12px; padding:12px 16px; border-bottom:1px solid #eee; text-decoration:none; color:inherit;'>" +
                    "<img src='/assets/image/" + m.getPoster() + "' " +
                    "data-base='/assets/image/" + m.getPoster().substring(0, m.getPoster().lastIndexOf('.')) + "' " +
                    "style='width:50px; height:75px; object-fit:cover; border-radius:6px; background:#f0f0f0; opacity:0; transition:opacity .3s;' " +
                    "onload='this.style.opacity=1' onerror='tryImage(this)'>" +
                    "<div>" +
                        "<div style='font-weight:600; font-size:15px;'>" + m.getTenPhim() + "</div>" +
                        "<div style='font-size:13px; color:#666; display:flex; gap:6px; flex-wrap:wrap; margin-top:4px;'>" +
                            "<span style='background:#e3f2fd; color:#1565c0; padding:2px 6px; border-radius:12px; font-size:11px;'>" + m.getTheLoai() + "</span>" +
                            "<span style='color:#ff9800; font-weight:600; font-size:12px;'>" + rate + "</span>" +
                            "<span style='color:#e67e22; font-size:12px;'>" + date + "</span>" +
                        "</div>" +
                    "</div>" +
                "</a>"
            );
        }
    }
}