package controller;

import dal.MovieDAO;
import dal.GenreDAO;
import dal.DirectorDAO;
import dal.TrailerDAO;
import dal.ShowtimeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.Movie;
import model.Trailer;

@MultipartConfig
public class AdminMovieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }

        MovieDAO movieDAO = new MovieDAO();

        switch (action) {
            case "list":
                listMovies(request, response, movieDAO);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response, movieDAO);
                break;
            default:
                listMovies(request, response, movieDAO);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect("admin-movies");
            return;
        }

        MovieDAO movieDAO = new MovieDAO();

        switch (action) {
            case "create":
                createMovie(request, response, movieDAO);
                break;
            case "update":
                updateMovie(request, response, movieDAO);
                break;
            case "delete":
                deleteMovie(request, response, movieDAO);
                break;
            default:
                response.sendRedirect("admin-movies");
        }
    }

    private void listMovies(HttpServletRequest request, HttpServletResponse response, MovieDAO movieDAO)
            throws ServletException, IOException {
        List<Movie> movies = movieDAO.getAllMovies();
        
        // Format dates for JSP display
        java.time.LocalDateTime today = java.time.LocalDateTime.now();
        request.setAttribute("today", today);
        
        // Create a map to store formatted dates
        java.util.Map<Integer, String> dateLabelMap = new java.util.HashMap<>();
        for (Movie movie : movies) {
            if (movie.getNgayKhoiChieu() != null) {
                dateLabelMap.put(movie.getMaPhim(), movie.getNgayKhoiChieu().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        }
        request.setAttribute("dateLabelMap", dateLabelMap);
        
        // Join: lấy tên thể loại và đạo diễn từ bảng liên kết
        GenreDAO genreDAO = new GenreDAO();
        DirectorDAO directorDAO = new DirectorDAO();
        java.util.Map<Integer, String> movieIdToGenres = new java.util.HashMap<>();
        java.util.Map<Integer, String> movieIdToDirectors = new java.util.HashMap<>();
        for (Movie movie : movies) {
            java.util.List<String> gnames = genreDAO.getGenreNamesByMovie(movie.getMaPhim());
            java.util.List<String> dnames = directorDAO.getDirectorNamesByMovie(movie.getMaPhim());
            movieIdToGenres.put(movie.getMaPhim(), String.join(", ", gnames));
            movieIdToDirectors.put(movie.getMaPhim(), String.join(", ", dnames));
        }
        request.setAttribute("movies", movies);
        request.setAttribute("movieIdToGenres", movieIdToGenres);
        request.setAttribute("movieIdToDirectors", movieIdToDirectors);
        // Lấy danh sách thể loại từ bảng Genre cho filter
        java.util.List<String> sortedGenres = new java.util.ArrayList<>();
        try {
//            GenreDAO genreDAO = new GenreDAO();
            for (model.Genre g : genreDAO.getAll()) {
                sortedGenres.add(g.getTenTheLoai());
            }
        } catch (Exception ignore) {}
        request.setAttribute("genres", sortedGenres);
        request.getRequestDispatcher("Views/admin/movie-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Load danh sách Genre/Director cho multi-select
        GenreDAO genreDAO = new GenreDAO();
        DirectorDAO directorDAO = new DirectorDAO();
        request.setAttribute("allGenres", genreDAO.getAll());
        request.setAttribute("allDirectors", directorDAO.getAll());
        request.getRequestDispatcher("Views/admin/movie-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, MovieDAO movieDAO)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        
        if (maPhimStr == null) {
            response.sendRedirect("admin-movies");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            Movie movie = movieDAO.getMovieById(maPhim);
            
            if (movie == null) {
                request.setAttribute("error", "Không tìm thấy phim");
                response.sendRedirect("admin-movies");
                return;
            }

            // Lấy trailer của phim (lấy trailer đầu tiên nếu có)
            TrailerDAO trailerDAO = new TrailerDAO();
            List<Trailer> trailers = trailerDAO.getTrailersByMovie(maPhim);
            Trailer trailer = trailers.isEmpty() ? null : trailers.get(0);

            // Load danh sách Genre/Director và đã chọn
            GenreDAO genreDAO = new GenreDAO();
            DirectorDAO directorDAO = new DirectorDAO();
            request.setAttribute("allGenres", genreDAO.getAll());
            request.setAttribute("allDirectors", directorDAO.getAll());
            request.setAttribute("selectedGenreIds", genreDAO.getGenreIdsByMovie(maPhim));
            request.setAttribute("selectedDirectorIds", directorDAO.getDirectorIdsByMovie(maPhim));
            
            // Format ngày khởi chiếu cho JSP
            String ngayKhoiChieuFormatted = null;
            if (movie.getNgayKhoiChieu() != null) {
                ngayKhoiChieuFormatted = movie.getNgayKhoiChieu().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            request.setAttribute("movie", movie);
            request.setAttribute("trailer", trailer);
            request.setAttribute("ngayKhoiChieuFormatted", ngayKhoiChieuFormatted);
            request.getRequestDispatcher("Views/admin/movie-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("admin-movies");
        }
    }

    private void createMovie(HttpServletRequest request, HttpServletResponse response, MovieDAO movieDAO)
            throws ServletException, IOException {
        String tenPhim = request.getParameter("tenPhim");
        String[] genreIdsParam = request.getParameterValues("genreIds");
        String loaiPhim = request.getParameter("loaiPhim");
        String[] directorIdsParam = request.getParameterValues("directorIds");
        String dienVien = request.getParameter("dienVien");
        String doTuoiGioiHanStr = request.getParameter("doTuoiGioiHan");
        String thoiLuongStr = request.getParameter("thoiLuong");
        String noiDung = request.getParameter("noiDung");
        String ngayKhoiChieuStr = request.getParameter("ngayKhoiChieu");
        String ngonNguPhuDe = request.getParameter("ngonNguPhuDe");
        String trangThai = request.getParameter("trangThai");
        
        // Lấy file upload poster
        Part posterPart = null;
        try { posterPart = request.getPart("posterFile"); } catch (IllegalStateException ex) {}

        if (tenPhim == null || loaiPhim == null || 
            dienVien == null || doTuoiGioiHanStr == null || thoiLuongStr == null || 
            noiDung == null || ngayKhoiChieuStr == null || ngonNguPhuDe == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            request.getRequestDispatcher("Views/admin/movie-form.jsp").forward(request, response);
            return;
        }

        try {
            int doTuoiGioiHan = Integer.parseInt(doTuoiGioiHanStr);
            int thoiLuong = Integer.parseInt(thoiLuongStr);
            java.time.LocalDateTime ngayKhoiChieu = java.time.LocalDateTime.parse(
                ngayKhoiChieuStr + "T00:00:00", 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
            
            // Xử lý lưu file poster
            String posterFileName = null;
            if (posterPart != null && posterPart.getSize() > 0) {
                String submitted = getSubmittedFileName(posterPart);
                if (submitted != null && !submitted.isBlank()) {
                    posterFileName = submitted;
                    saveUploadedFile(posterPart, getUploadDir(), posterFileName);
                }
            }
            
            if (posterFileName == null || posterFileName.isBlank()) {
                request.setAttribute("error", "Vui lòng chọn poster phim");
                request.getRequestDispatcher("Views/admin/movie-form.jsp").forward(request, response);
                return;
            }
            
            Movie movie = new Movie();
            movie.setTenPhim(tenPhim);
            movie.setLoaiPhim(loaiPhim);
            movie.setDienVien(dienVien);
            movie.setDoTuoiGioiHan(doTuoiGioiHan);
            movie.setThoiLuong(thoiLuong);
            movie.setNoiDung(noiDung);
            movie.setNgayKhoiChieu(ngayKhoiChieu);
            movie.setPoster(posterFileName);
            movie.setNgonNguPhuDe(ngonNguPhuDe);
            movie.setTrangThai(trangThai != null ? trangThai : "Sắp chiếu");

            boolean success = movieDAO.addMovie(movie);
            
            if (success) {
                // Lưu quan hệ thể loại và đạo diễn
                GenreDAO genreDAO = new GenreDAO();
                DirectorDAO directorDAO = new DirectorDAO();
                java.util.List<Integer> genreIds = new java.util.ArrayList<>();
                if (genreIdsParam != null) {
                    for (String s : genreIdsParam) {
                        try { genreIds.add(Integer.parseInt(s)); } catch (NumberFormatException ignore) {}
                    }
                }
                java.util.List<Integer> directorIds = new java.util.ArrayList<>();
                if (directorIdsParam != null) {
                    for (String s : directorIdsParam) {
                        try { directorIds.add(Integer.parseInt(s)); } catch (NumberFormatException ignore) {}
                    }
                }
                try {
                    genreDAO.setGenresForMovie(movie.getMaPhim(), genreIds);
                    directorDAO.setDirectorsForMovie(movie.getMaPhim(), directorIds);
                } catch (Exception ex) {
                    // ignore but logable
                }
                // Lưu trailer nếu có
                String linkTrailer = request.getParameter("linkTrailer");
                String moTaTrailer = request.getParameter("moTaTrailer");
                
                if (linkTrailer != null && !linkTrailer.trim().isEmpty()) {
                    TrailerDAO trailerDAO = new TrailerDAO();
                    Trailer trailer = new Trailer();
                    trailer.setMaPhim(movie.getMaPhim());
                    trailer.setLinkTrailer(linkTrailer.trim());
                    trailer.setMoTa(moTaTrailer != null ? moTaTrailer.trim() : null);
                    trailerDAO.addTrailer(trailer);
                }
                
                request.setAttribute("success", "Tạo phim thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo phim");
            }
            
            response.sendRedirect("admin-movies");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu số không hợp lệ");
            request.getRequestDispatcher("Views/admin/movie-form.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("Views/admin/movie-form.jsp").forward(request, response);
        }
    }

    private void updateMovie(HttpServletRequest request, HttpServletResponse response, MovieDAO movieDAO)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        String tenPhim = request.getParameter("tenPhim");
        String[] genreIdsParam = request.getParameterValues("genreIds");
        String loaiPhim = request.getParameter("loaiPhim");
        String[] directorIdsParam = request.getParameterValues("directorIds");
        String dienVien = request.getParameter("dienVien");
        String doTuoiGioiHanStr = request.getParameter("doTuoiGioiHan");
        String thoiLuongStr = request.getParameter("thoiLuong");
        String noiDung = request.getParameter("noiDung");
        String ngayKhoiChieuStr = request.getParameter("ngayKhoiChieu");
        String ngonNguPhuDe = request.getParameter("ngonNguPhuDe");
        String trangThai = request.getParameter("trangThai");
        
        // Lấy file upload poster
        Part posterPart = null;
        try { posterPart = request.getPart("posterFile"); } catch (IllegalStateException ex) {}

        if (maPhimStr == null || tenPhim == null || loaiPhim == null || 
            dienVien == null || doTuoiGioiHanStr == null || thoiLuongStr == null || 
            noiDung == null || ngayKhoiChieuStr == null || ngonNguPhuDe == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            response.sendRedirect("admin-movies");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            int doTuoiGioiHan = Integer.parseInt(doTuoiGioiHanStr);
            int thoiLuong = Integer.parseInt(thoiLuongStr);
            java.time.LocalDateTime ngayKhoiChieu = java.time.LocalDateTime.parse(
                ngayKhoiChieuStr + "T00:00:00", 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            );
            
            // Lấy phim hiện tại để giữ poster cũ nếu không upload mới
            Movie current = movieDAO.getMovieById(maPhim);
            String posterFileName = (current != null) ? current.getPoster() : null;
            if (posterPart != null && posterPart.getSize() > 0) {
                String submitted = getSubmittedFileName(posterPart);
                if (submitted != null && !submitted.isBlank()) {
                    posterFileName = submitted;
                    saveUploadedFile(posterPart, getUploadDir(), posterFileName);
                }
            }
            
            Movie movie = new Movie();
            movie.setMaPhim(maPhim);
            movie.setTenPhim(tenPhim);
            movie.setLoaiPhim(loaiPhim);
            movie.setDienVien(dienVien);
            movie.setDoTuoiGioiHan(doTuoiGioiHan);
            movie.setThoiLuong(thoiLuong);
            movie.setNoiDung(noiDung);
            movie.setNgayKhoiChieu(ngayKhoiChieu);
            movie.setPoster(posterFileName);
            movie.setNgonNguPhuDe(ngonNguPhuDe);
            movie.setTrangThai(trangThai);

            boolean success = movieDAO.updateMovie(movie);
            
            if (success) {
                // Cập nhật quan hệ thể loại và đạo diễn
                GenreDAO genreDAO = new GenreDAO();
                DirectorDAO directorDAO = new DirectorDAO();
                java.util.List<Integer> genreIds = new java.util.ArrayList<>();
                if (genreIdsParam != null) {
                    for (String s : genreIdsParam) {
                        try { genreIds.add(Integer.parseInt(s)); } catch (NumberFormatException ignore) {}
                    }
                }
                java.util.List<Integer> directorIds = new java.util.ArrayList<>();
                if (directorIdsParam != null) {
                    for (String s : directorIdsParam) {
                        try { directorIds.add(Integer.parseInt(s)); } catch (NumberFormatException ignore) {}
                    }
                }
                try {
                    genreDAO.setGenresForMovie(maPhim, genreIds);
                    directorDAO.setDirectorsForMovie(maPhim, directorIds);
                } catch (Exception ex) {
                    // ignore but logable
                }
                // Xử lý trailer
                String linkTrailer = request.getParameter("linkTrailer");
                String moTaTrailer = request.getParameter("moTaTrailer");
                
                TrailerDAO trailerDAO = new TrailerDAO();
                List<Trailer> existingTrailers = trailerDAO.getTrailersByMovie(maPhim);
                
                if (linkTrailer != null && !linkTrailer.trim().isEmpty()) {
                    // Có link trailer
                    if (existingTrailers.isEmpty()) {
                        // Tạo mới trailer
                        Trailer trailer = new Trailer();
                        trailer.setMaPhim(maPhim);
                        trailer.setLinkTrailer(linkTrailer.trim());
                        trailer.setMoTa(moTaTrailer != null ? moTaTrailer.trim() : null);
                        trailerDAO.addTrailer(trailer);
                    } else {
                        // Cập nhật trailer đầu tiên
                        Trailer trailer = existingTrailers.get(0);
                        trailer.setLinkTrailer(linkTrailer.trim());
                        trailer.setMoTa(moTaTrailer != null ? moTaTrailer.trim() : null);
                        trailerDAO.updateTrailer(trailer);
                    }
                } else {
                    // Không có link trailer, xóa trailer nếu có
                    for (Trailer trailer : existingTrailers) {
                        trailerDAO.deleteTrailer(trailer.getMaTrailer());
                    }
                }
                
                request.setAttribute("success", "Cập nhật phim thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi cập nhật phim");
            }
            
            response.sendRedirect("admin-movies");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            response.sendRedirect("admin-movies");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            response.sendRedirect("admin-movies");
        }
    }

    private void deleteMovie(HttpServletRequest request, HttpServletResponse response, MovieDAO movieDAO)
            throws ServletException, IOException {
        String maPhimStr = request.getParameter("maPhim");
        
        if (maPhimStr == null) {
            response.sendRedirect("admin-movies");
            return;
        }

        try {
            int maPhim = Integer.parseInt(maPhimStr);
            boolean success = movieDAO.deleteMovie(maPhim);
            
            if (success) {
                request.setAttribute("success", "Xóa phim thành công");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi xóa phim");
            }
            
            response.sendRedirect("admin-movies");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID phim không hợp lệ");
            response.sendRedirect("admin-movies");
        }
    }

    // Lấy tên file gốc từ header content-disposition
    private String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;
        for (String cd : header.split(";")) {
            String trimmed = cd.trim();
            if (trimmed.startsWith("filename")) {
                String fileName = trimmed.substring(trimmed.indexOf('=') + 1).trim().replace("\"", "");
                return new File(fileName).getName();
            }
        }
        return null;
    }

    // Thư mục lưu file upload theo yêu cầu
    private String getUploadDir() {
        return "D:\\JavaProject\\MovieTicketSystem\\MovieTicketBookingSystem\\web\\assets\\image";
    }

    // Lưu file lên ổ đĩa, ghi đè nếu tồn tại
    private void saveUploadedFile(Part part, String uploadDir, String fileName) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Path target = new File(dir, fileName).toPath();
        try {
            Files.copy(part.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            part.delete();
        }
    }
}
