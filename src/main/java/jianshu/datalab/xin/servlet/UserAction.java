package jianshu.datalab.xin.servlet;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Constants;
import jianshu.datalab.xin.dao.UserDao;
import jianshu.datalab.xin.model.User;
import jianshu.datalab.xin.util.Error;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyuncong on
 * 2017/6/28 10:27
 * jianshu.
 */
@WebServlet(urlPatterns = "/user")
public class UserAction extends HttpServlet {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("signUp".equals(action)) {
            signUp(req, resp);
            return;
        }

        if ("isNickOrMobileExisted".equals(action)) {
            isNickOrMobileExisted(req, resp);
            return;
        }

        if ("signIn".equals(action)) {
            signIn(req, resp);
            return;
        }

        if ("signInApi".equals(action)) {
            signInApi(req, resp);
            return;
        }

        if ("signOut".equals(action)) {
            signOut(req, resp);
            return;
        }

        if ("checkValidCode".equals(action)) {
            checkValidCode(req, resp);
            return;
        }

        Error.showError(req, resp);
    }


    private void signUp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nick = req.getParameter("nick").trim();
        String mobile = req.getParameter("mobile").trim();
        String plainPassword = req.getParameter("password");

        if (nick.length() == 0) {
            req.setAttribute("message", "请输入昵称");
            req.getRequestDispatcher("sign_up.jsp").forward(req, resp);
            return;
        }

        if (mobile.length() == 0) {
            req.setAttribute("message", "请输入手机号");
            req.getRequestDispatcher("sign_up.jsp").forward(req, resp);
            return;
        }

        if (plainPassword.length() < 6) {
            req.setAttribute("message", "密码不能少于6个字符");
            req.getRequestDispatcher("sign_up.jsp").forward(req, resp);
            return;
        }

        if (isNickExisted(req, resp)) {
            req.setAttribute("message", "昵称 已经被使用");
            req.getRequestDispatcher("sign_up.jsp").forward(req, resp);
            return;
        }

        if (isMobileExisted(req, resp)) {
            req.setAttribute("message", "手机号 已经被使用");
            req.getRequestDispatcher("sign_up.jsp").forward(req, resp);
            return;
        }

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        String password = encryptor.encryptPassword(plainPassword);
        String lastIp = req.getRemoteAddr();

        userDao.signUp(new User(nick, mobile, password, lastIp));

        resp.sendRedirect("sign_in.jsp");
    }

    private User checkSignIn(HttpServletRequest req, HttpServletResponse resp) {
        String mobile = req.getParameter("mobile").trim();
        String plainPassword = req.getParameter("password");

        User user = userDao.queryUserByMobile(mobile);
        if (user != null) {
            String encryptedPassword = user.getPassword();
            StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
            if (encryptor.checkPassword(plainPassword, encryptedPassword)) {
                String lastIp = req.getRemoteAddr();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String lastTime = format.format(new Date());
                user.setLastIp(lastIp);
                user.setLastTime(lastTime);
                userDao.signInUpdate(user);
                return user;
            }
        }
        return null;
    }

    /**
     * 处理 Android 客户端请求
     */
    private void signInApi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = checkSignIn(req, resp);

        resp.setContentType("application/json");
        Writer writer = resp.getWriter();
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            map.put("canSignIn", true);
            map.put("user", user);
        } else {
            map.put("canSignIn", false);
            map.put("user", null);
        }

        String json = JSON.toJSONString(map);
        writer.write(json);
    }

    private void signIn(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!checkValidCode(req, resp)) {
            req.setAttribute("message", "验证码错误");
            req.getRequestDispatcher("sign_in.jsp").forward(req, resp);
            return;
        }

        User user = checkSignIn(req, resp);
        if (user != null) {
            req.getSession().setAttribute("user", user);
            resp.sendRedirect("default.jsp");
            return;
        }

        req.setAttribute("message", "登录失败， 手机号/邮箱或密码错误");
        req.getRequestDispatcher("sign_in.jsp").forward(req, resp);
    }

    private void signOut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect("default.jsp");
    }


    /**
     * for signUp
     */
    private boolean isNickExisted(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return isExisted(req, resp, "nick", req.getParameter("nick").trim());
    }

    /**
     * for signUp
     */
    private boolean isMobileExisted(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return isExisted(req, resp, "mobile", req.getParameter("mobile").trim());
    }

    /**
     * for AJAX
     */
    private void isNickOrMobileExisted(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String field = req.getParameter("field");
        String value = req.getParameter("value").trim();

        boolean isExisted = isExisted(req, resp, field, value);

        resp.setContentType("application/json");
        Writer writer = resp.getWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("isExisted", isExisted);
        writer.write(JSON.toJSONString(map));
    }

    private boolean isExisted(HttpServletRequest req, HttpServletResponse resp, String field, String value) throws ServletException, IOException {
        boolean isNickExisted = false;
        boolean isMobileExisted = false;


        if (field.equals("nick")) {
            User user = userDao.queryUserByNick(value);
            isNickExisted = (user != null);
        } else {
            User user = userDao.queryUserByMobile(value);
            isMobileExisted = (user != null);
        }
        return isNickExisted || isMobileExisted;
    }

    private boolean checkValidCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String kaptchaReceived = req.getParameter("kaptchaReceived");
        String kaptchaExpected = (String) req.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println(kaptchaExpected);
        resp.setContentType("application/json");
        Writer writer = resp.getWriter();
        Map<String, Boolean> map = new HashMap<>();

        if (kaptchaExpected.equalsIgnoreCase(kaptchaReceived)) {
            map.put("isValid", true);
        } else {
            map.put("isValid", false);
        }
        writer.write(JSON.toJSONString(map));
        return map.get("isValid");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
