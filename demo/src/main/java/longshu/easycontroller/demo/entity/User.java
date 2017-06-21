package longshu.easycontroller.demo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * User
 *
 * @author LongShu 2017/06/20
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    private String email;

}
