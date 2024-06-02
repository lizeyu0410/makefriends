package com.lzy.makefriends.service;

import com.lzy.makefriends.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;



@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();

        user.setUsername("lzy");
        user.setUserAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("410228");
        user.setPhone("123123123");
        user.setEmail("123123123");
        user.setPlanetCode("123123");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        // 断言
        Assertions.assertTrue(result);

    }

    @Test
    void userRegister() {
        String userAccount = "lzy";
        String userPassword = "123456";
        String checkPassword = "123456";
        String planetCode = "12345";


        Long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);


    }

    @Test
    public void searchUsersByTags(){
        List<String> tagNameList = Arrays.asList("java","python");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assert.assertNotNull(userList);
    }

    @Test
    public void testLambda(){
        BiPredicate<String, String> eq = (s1, s2) -> s1.equals(s2);
        System.out.println(eq.test("123", "123"));

        BiPredicate<String, String> eq1 = String :: equals;
        System.out.println(eq1.test("qwe", "w"));


        Function<User, String> fun = (user) -> user.getUserAccount();
        User user = new User();
        fun.apply(user);
        Function<User, String> fun1 = User :: getUserAccount;

    }
}