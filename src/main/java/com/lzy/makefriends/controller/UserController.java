package com.lzy.makefriends.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzy.makefriends.common.BaseResponse;
import com.lzy.makefriends.common.ErrorCode;
import com.lzy.makefriends.common.ResultUtils;
import com.lzy.makefriends.exception.BusinessException;
import com.lzy.makefriends.model.domain.User;
import com.lzy.makefriends.model.domain.request.UserLoginRequest;
import com.lzy.makefriends.model.domain.request.UserRegisterRequest;
import com.lzy.makefriends.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lzy.makefriends.constant.UserConstant.ADMIN_ROLE;
import static com.lzy.makefriends.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = { "http://localhost:5173" })
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null){
            return null;
        }

        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 搜索用户
     * @param username
     * @return
     */
    @GetMapping("search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        if (!userService.isAdmin(request)) {
            return null;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);

        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 推荐用户
     * @param request
     * @return
     */
    @GetMapping("recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        // 设置缓存的key，要区分每个用户
        String redisKey = String.format("lzy:user:recommend:%s", loginUser.getId());

        //如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) redisTemplate.opsForValue().get(redisKey);
        if (userPage != null){
            return ResultUtils.success(userPage);
        }
        // 无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>((pageNum - 1) * pageSize, pageSize) ,queryWrapper);

        // 写缓存，设置过期时间
        try {
            redisTemplate.opsForValue().set(redisKey, userPage, 10000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userPage);
    }

    /**
     * 删除用户（逻辑删除）
     * @param id 用户id
     * @return
     */
    @PostMapping("delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request){
        if (!userService.isAdmin(request)) {
            return null;
        }

        if (id <= 0){
            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新用户信息
     * @return
     */
    @PostMapping("update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request){
        // 校验参数是否为空
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验权限
        User loginUser = userService.getLoginUser(request);

        Integer result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);

    }


    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @return
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }


}
