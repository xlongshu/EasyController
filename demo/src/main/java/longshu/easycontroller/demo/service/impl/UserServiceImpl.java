package longshu.easycontroller.demo.service.impl;

import longshu.easycontroller.demo.dao.UserDao;
import longshu.easycontroller.demo.entity.User;
import longshu.easycontroller.demo.service.ServiceException;
import longshu.easycontroller.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.List;

/**
 * UserServiceImpl
 *
 * @author LongShu 2017/06/20
 */
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Override
    public User login(String name, String password) throws ServiceException {
        if (StringUtils.isAnyBlank(name, password)) {
            throw new ServiceException("用户名或密码不能为空!");
        }
        User user = userDao.findByName(name);
        if (StringUtils.equals(user.getPassword(), password)) {
            return user;
        }
        throw new ServiceException("用户名或密码错误,登录失败");
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

}
