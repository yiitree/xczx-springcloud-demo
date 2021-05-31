package com.xuecheng.framework.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by mrt on 2018/5/21.
 */
@Data
@ToString
@NoArgsConstructor
public class AuthToken {

    //访问token就是短令牌，用户身份令牌
    String access_token;
    //刷新token
    String refresh_token;
    //jwt令牌(长令牌，真实令牌)
    String jwt_token;

}
