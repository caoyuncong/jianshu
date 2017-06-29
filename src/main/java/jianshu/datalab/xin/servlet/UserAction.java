package jianshu.datalab.xin.servlet;

import com.alibaba.fastjson.JSON;
import jianshu.datalab.xin.util.DB;
import jianshu.datalab.xin.util.Error;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyuncong on
 * 2017/6/28 10:27
 * jianshu.
 */
@WebServlet(urlPatterns = "/user")
public class UserAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("signUp".equals(action)) {
            signUp(req,resp);
            return;
        }

        if ("isNickExisted".equals(action)) {
            isNickExisted(req,resp);
            return;
        }

        Error.showError(req,resp);
    }


    private void signUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nick = req.getParameter("nick").trim();
        String mobile = req.getParameter("mobile").trim();

        // TODO: 2017/6/28 isNickExisted
        // TODO: 2017/6/28 isMobileExisted

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        String password = encryptor.encryptPassword(req.getParameter("password"));

        Connection connection = DB.getConnection();
        PreparedStatement preparedStatement = null;

        String sql = "INSERT INTO db_jianshu.user(nick, mobile, password,lastIp) VALUE (?,?,?,?)";

        try {
            if (connection != null) {
                preparedStatement = connection.prepareStatement(sql);
            }else{
                Error.showError(req, resp);
                return;
            }
            preparedStatement.setString(1,nick);
            preparedStatement.setString(2,mobile);
            preparedStatement.setString(3,password);
            preparedStatement.setString(4, req.getRemoteAddr());

            preparedStatement.executeUpdate();

            resp.sendRedirect("/");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DB.close(null,preparedStatement,connection);
        }
    }

    private boolean isNickExisted(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nick = req.getParameter("nick").trim();
        boolean isNickExisted = false;

        Connection connection = DB.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM db_jianshu.user WHERE nick = ?";

        try {
            if (connection != null) {
                preparedStatement = connection.prepareStatement(sql);
            }else {
                Error.showError(req, resp);
                return false;
            }
            preparedStatement.setString(1, nick);
            resultSet = preparedStatement.executeQuery();
            isNickExisted = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DB.close(resultSet,preparedStatement,connection);
        }
        resp.setContentType("application/json");
        Writer writer = resp.getWriter();

        Map<String, Object> map = new HashMap<>();
        map.put("isNickExisted", (Math.random() > .5) ? true : false);

        String json = JSON.toJSONString(map, true);

        System.out.println(json);

        writer.write(json);
        return true;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
