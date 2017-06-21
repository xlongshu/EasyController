package longshu.easycontroller.demo.service;

import java.util.List;

import longshu.easycontroller.demo.entity.User;

/**
 * UserService
 * 
 * @author LongShu 2017/06/20
 */
public interface UserService {

    User login(String name, String password) throws ServiceException;

    List<User> findAll();

}
