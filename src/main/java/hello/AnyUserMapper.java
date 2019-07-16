package hello;

import entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnyUserMapper {

    /** 通过用户名获取用户信息 */
    SystemUser getUserByUsername(@Param("username") String username);
}
