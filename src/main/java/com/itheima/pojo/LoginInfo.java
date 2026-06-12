package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 封装登入信息
 */
public class LoginInfo {
    private Integer id;
    private String username;
    private String name;
    private String token;

}
