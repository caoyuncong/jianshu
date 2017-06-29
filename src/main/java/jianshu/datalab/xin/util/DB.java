package jianshu.datalab.xin.util;

import com.mysql.jdbc.Driver;

import java.sql.*;

/**
 * Created by caoyuncong on
 * 2017/6/27 16:03
 * jianshu.
 */
public class DB {
    private static final String URL = "jdbc:mysql:///?user=root&password=system";

    public static Connection getConnection() {
//        new Driver();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        if (resultSet!=null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement!=null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection!=null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
