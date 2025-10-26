package controller;

import dal.CinemaDAO;
import model.Cinema;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AdminCinemaServlet extends HttpServlet {

    private CinemaDAO cinemaDAO;

    @Override
    public void init() throws ServletException {
        cinemaDAO = new CinemaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listCinemas(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCinema(request, response);
                    break;
                case "search":
                    searchCinemas(request, response);
                    break;
                default:
                    listCinemas(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            listCinemas(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("admin-cinema");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addCinema(request, response);
                    break;
                case "update":
                    updateCinema(request, response);
                    break;
                default:
                    response.sendRedirect("admin-cinema");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            if ("add".equals(action)) {
                showAddForm(request, response);
            } else if ("update".equals(action)) {
                showEditForm(request, response);
            } else {
                listCinemas(request, response);
            }
        }
    }

    /**
     * Hiển thị danh sách rạp phim
     */
    private void listCinemas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Cinema> cinemas = cinemaDAO.getAllCinemas();
        List<String> areas = cinemaDAO.getDistinctAreas();
        
        request.setAttribute("cinemas", cinemas);
        request.setAttribute("areas", areas);
        request.getRequestDispatcher("Views/admin/cinema-list.jsp").forward(request, response);
    }

    /**
     * Hiển thị form thêm rạp phim
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("Views/admin/cinema-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị form sửa rạp phim
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String maRapStr = request.getParameter("maRap");
        if (maRapStr == null) {
            request.setAttribute("error", "Không tìm thấy mã rạp phim");
            listCinemas(request, response);
            return;
        }

        try {
            int maRap = Integer.parseInt(maRapStr);
            Cinema cinema = cinemaDAO.getCinemaById(maRap);
            
            if (cinema == null) {
                request.setAttribute("error", "Không tìm thấy rạp phim");
                listCinemas(request, response);
                return;
            }

            request.setAttribute("cinema", cinema);
            request.getRequestDispatcher("Views/admin/cinema-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã rạp phim không hợp lệ");
            listCinemas(request, response);
        }
    }

    /**
     * Thêm rạp phim mới
     */
    private void addCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String tenRap = request.getParameter("tenRap");
        String diaChi = request.getParameter("diaChi");
        String khuVuc = request.getParameter("khuVuc");

        // Validation
        if (tenRap == null || tenRap.trim().isEmpty()) {
            request.setAttribute("error", "Tên rạp phim không được để trống");
            showAddForm(request, response);
            return;
        }

        if (diaChi == null || diaChi.trim().isEmpty()) {
            request.setAttribute("error", "Địa chỉ không được để trống");
            showAddForm(request, response);
            return;
        }

        // Kiểm tra tên rạp phim trùng
        if (cinemaDAO.cinemaNameExists(tenRap.trim(), 0)) {
            request.setAttribute("error", "Tên rạp phim đã tồn tại");
            showAddForm(request, response);
            return;
        }

        Cinema cinema = new Cinema();
        cinema.setTenRap(tenRap.trim());
        cinema.setDiaChi(diaChi.trim());
        cinema.setKhuVuc(khuVuc != null ? khuVuc.trim() : "");

        if (cinemaDAO.addCinema(cinema)) {
            request.setAttribute("success", "Thêm rạp phim thành công");
        } else {
            request.setAttribute("error", "Thêm rạp phim thất bại");
        }

        listCinemas(request, response);
    }

    /**
     * Cập nhật rạp phim
     */
    private void updateCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String maRapStr = request.getParameter("maRap");
        String tenRap = request.getParameter("tenRap");
        String diaChi = request.getParameter("diaChi");
        String khuVuc = request.getParameter("khuVuc");

        if (maRapStr == null) {
            request.setAttribute("error", "Không tìm thấy mã rạp phim");
            listCinemas(request, response);
            return;
        }

        try {
            int maRap = Integer.parseInt(maRapStr);

            // Validation
            if (tenRap == null || tenRap.trim().isEmpty()) {
                request.setAttribute("error", "Tên rạp phim không được để trống");
                showEditForm(request, response);
                return;
            }

            if (diaChi == null || diaChi.trim().isEmpty()) {
                request.setAttribute("error", "Địa chỉ không được để trống");
                showEditForm(request, response);
                return;
            }

            // Kiểm tra tên rạp phim trùng (loại trừ rạp phim hiện tại)
            if (cinemaDAO.cinemaNameExists(tenRap.trim(), maRap)) {
                request.setAttribute("error", "Tên rạp phim đã tồn tại");
                showEditForm(request, response);
                return;
            }

            Cinema cinema = new Cinema();
            cinema.setMaRap(maRap);
            cinema.setTenRap(tenRap.trim());
            cinema.setDiaChi(diaChi.trim());
            cinema.setKhuVuc(khuVuc != null ? khuVuc.trim() : "");

            if (cinemaDAO.updateCinema(cinema)) {
                request.setAttribute("success", "Cập nhật rạp phim thành công");
            } else {
                request.setAttribute("error", "Cập nhật rạp phim thất bại");
            }

            listCinemas(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã rạp phim không hợp lệ");
            listCinemas(request, response);
        }
    }

    /**
     * Xóa rạp phim
     */
    private void deleteCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String maRapStr = request.getParameter("maRap");
        if (maRapStr == null) {
            request.setAttribute("error", "Không tìm thấy mã rạp phim");
            listCinemas(request, response);
            return;
        }

        try {
            int maRap = Integer.parseInt(maRapStr);
            
            if (cinemaDAO.deleteCinema(maRap)) {
                request.setAttribute("success", "Xóa rạp phim thành công");
            } else {
                request.setAttribute("error", "Xóa rạp phim thất bại");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Mã rạp phim không hợp lệ");
        }

        listCinemas(request, response);
    }

    /**
     * Tìm kiếm rạp phim
     */
    private void searchCinemas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        List<Cinema> cinemas;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            cinemas = cinemaDAO.searchCinemas(keyword.trim());
            request.setAttribute("searchKeyword", keyword.trim());
        } else {
            cinemas = cinemaDAO.getAllCinemas();
        }
        
        List<String> areas = cinemaDAO.getDistinctAreas();
        request.setAttribute("cinemas", cinemas);
        request.setAttribute("areas", areas);
        request.getRequestDispatcher("Views/admin/cinema-list.jsp").forward(request, response);
    }
}
