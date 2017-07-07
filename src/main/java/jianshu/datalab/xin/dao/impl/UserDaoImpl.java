package jianshu.datalab.xin.dao.impl;

import jianshu.datalab.xin.dao.UserDao;
import jianshu.datalab.xin.model.User;
import jianshu.datalab.xin.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by caoyuncong on
 * 2017/7/7 14:46
 * jianshu.
 */
public class UserDaoImpl implements UserDao {

    @Override
    public void signUp(User user) {
        try (SqlSession sqlSession = MybatisUtil.getSqlSession(true)) {
            sqlSession.insert("user.signUp", user);
        }
    }

    @Override
    public User queryUserByNick(String nick) {
        try (SqlSession sqlSession = MybatisUtil.getSqlSession(false)) {
            return sqlSession.selectOne("user.queryUserByNick", nick);
        }
    }

    @Override
    public User queryUserByMobile(String mobile) {
        try (SqlSession sqlSession = MybatisUtil.getSqlSession(false)) {
            return sqlSession.selectOne("user.queryUserByMobile", mobile);
        }
    }

    @Override
    public void signInUpdate(User user) {
        try (SqlSession sqlSession = MybatisUtil.getSqlSession(true)) {
            sqlSession.update("user.signInUpdate", user);
        }
    }
}
