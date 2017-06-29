package jianshu.datalab.xin.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by caoyuncong on
 * 2017/6/27 17:12
 * jianshu.
 */
public class Error {
    public static void showError(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("message", "Error");
        try {
            request.getRequestDispatcher("default.jsp").forward(request,response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
