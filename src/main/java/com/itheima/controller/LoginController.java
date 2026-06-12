package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import com.itheima.validation.ValidationGroups;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private EmpService empService;

    @PostMapping
    public Result login(@RequestBody @Validated(ValidationGroups.Login.class) Emp emp) {
        LoginInfo info = empService.login(emp);
        if (info != null) {
            log.info("Employee login succeeded: username={}", emp.getUsername());
            return Result.success(info);
        }

        log.warn("Employee login failed: username={}", emp.getUsername());
        return Result.error("用户名或密码错误");
    }
}
