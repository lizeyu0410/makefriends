package com.lzy.makefriends.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzy.makefriends.mapper.UserTeamMapper;
import com.lzy.makefriends.model.domain.UserTeam;
import com.lzy.makefriends.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author lizey
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-06-02 08:11:31
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




