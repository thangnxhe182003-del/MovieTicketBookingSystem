package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Staff;

/**
 * Utility class để kiểm tra quyền truy cập theo role
 */
public class RoleChecker {
    
    /**
     * Kiểm tra xem user đã đăng nhập chưa
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        return session.getAttribute("loggedInStaff") != null;
    }
    
    /**
     * Kiểm tra xem user có phải Admin không
     */
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) return false;
        
        return "Admin".equalsIgnoreCase(staff.getChucVu());
    }
    
    /**
     * Kiểm tra xem user có phải Manager không
     */
    public static boolean isManager(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        
        Staff staff = (Staff) session.getAttribute("loggedInStaff");
        if (staff == null) return false;
        
        return "Manager".equalsIgnoreCase(staff.getChucVu());
    }
    
    /**
     * Kiểm tra và redirect nếu chưa đăng nhập
     * @return true nếu đã đăng nhập, false nếu chưa (và đã redirect)
     */
    public static boolean requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isLoggedIn(request)) {
            response.sendRedirect("home");
            return false;
        }
        return true;
    }
    
    /**
     * Kiểm tra và redirect nếu không phải Admin
     * @return true nếu là Admin, false nếu không (và đã redirect)
     */
    public static boolean requireAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) return false;
        
        if (!isAdmin(request)) {
            response.sendRedirect("home");
            return false;
        }
        return true;
    }
    
    /**
     * Kiểm tra và redirect nếu không phải Manager
     * @return true nếu là Manager, false nếu không (và đã redirect)
     */
    public static boolean requireManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) return false;
        
        if (!isManager(request)) {
            response.sendRedirect("home");
            return false;
        }
        return true;
    }
    
    /**
     * Kiểm tra và redirect nếu không phải Admin hoặc Manager
     * @return true nếu là Admin hoặc Manager, false nếu không (và đã redirect)
     */
    public static boolean requireAdminOrManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!requireLogin(request, response)) return false;
        
        if (!isAdmin(request) && !isManager(request)) {
            response.sendRedirect("home");
            return false;
        }
        return true;
    }
}
