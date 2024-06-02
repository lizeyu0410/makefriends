package com.lzy.makefriends.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzy.makefriends.mapper.TeamMapper;
import com.lzy.makefriends.model.domain.Team;
import com.lzy.makefriends.service.TeamService;
import org.springframework.stereotype.Service;

/**
* @author lizey
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-06-02 08:10:37
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

}




