package jianshu.datalab.xin.dao;

import jianshu.datalab.xin.model.User;

/**
 * Created by caoyuncong on
 * 2017/7/7 14:38
 * jianshu.
 */
public interface UserDao {
    void signUp(User user);

    User queryUserByNick(String nick);

    User queryUserByMobile(String mobile);

    void signInUpdate(User user);

}
