package controller;

import dal.DirectorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Director;
import java.io.IOException;
import java.util.List;

public class AdminDirectorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
		if (action == null) {
			action = "list";
		}
        DirectorDAO dao = new DirectorDAO();
        switch (action) {
            case "list":
            default:
                List<Director> directors = dao.getAll();
                request.setAttribute("directors", directors);
                request.getRequestDispatcher("Views/admin/director-list.jsp").forward(request, response);
        }
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		DirectorDAO dao = new DirectorDAO();
		if ("create".equals(action)) {
			String name = request.getParameter("name");
			if (name != null && !name.trim().isEmpty()) {
				dao.create(name.trim());
			}
		} else if ("update".equals(action)) {
			String idStr = request.getParameter("id");
			String name = request.getParameter("name");
			try {
				int id = Integer.parseInt(idStr);
				if (name != null && !name.trim().isEmpty()) {
					dao.update(id, name.trim());
				}
			} catch (Exception ignored) {}
		} else if ("delete".equals(action)) {
			String idStr = request.getParameter("id");
			try {
				int id = Integer.parseInt(idStr);
				dao.delete(id);
			} catch (Exception ignored) {}
		}
		response.sendRedirect("admin-directors");
	}
}
