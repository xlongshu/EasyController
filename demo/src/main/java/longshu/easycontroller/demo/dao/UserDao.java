package longshu.easycontroller.demo.dao;

import longshu.easycontroller.demo.entity.User;

import java.util.List;

/**
 * UserDao
 *
 * @author LongShu 2017/06/20
 */
public interface UserDao {

    int countAll();

    User findById(int id);

    User findByName(String name);

    List<User> findAll();

}
