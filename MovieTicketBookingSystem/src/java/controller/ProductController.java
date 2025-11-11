package controller;

import dal.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductController extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy danh sách tất cả sản phẩm
            List<Product> allProducts = productDAO.getAllProducts();
            
            // Lọc chỉ lấy sản phẩm đang bán (TrangThai = 'Đang bán')
            List<Product> activeProducts = new ArrayList<>();
            for (Product product : allProducts) {
                if ("Active".equals(product.getTrangThai())) {
                    activeProducts.add(product);
                }
            }
            
            request.setAttribute("products", activeProducts);
            request.getRequestDispatcher("Views/common/products.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

