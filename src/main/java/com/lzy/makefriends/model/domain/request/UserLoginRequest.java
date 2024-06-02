package com.lzy.makefriends.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 101330308975246518L;
    private String userAccount;
    private String userPassword;

}
