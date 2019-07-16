package entity;

public class SysUserRole {
    private Integer UserRoleId;
    private Integer UserId;
    private Integer RoleId;

    public Integer getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        UserRoleId = userRoleId;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getRoleId() {
        return RoleId;
    }

    public void setRoleId(Integer roleId) {
        RoleId = roleId;
    }
}
