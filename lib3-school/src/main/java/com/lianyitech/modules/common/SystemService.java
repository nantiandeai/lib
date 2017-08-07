/**
 *
 */
package com.lianyitech.modules.common;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 *
 * @version 2016-07-05
 */
@Service
public class SystemService{

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String password) {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        return encoder.encodePassword(password, null);
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        return encoder.isPasswordValid(plainPassword, password, null);
    }

}
