package com.lzy.makefriends.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzy.makefriends.model.domain.Team;
import com.lzy.makefriends.model.domain.User;
import com.lzy.makefriends.model.domain.request.TeamJoinRequest;
import com.lzy.makefriends.model.domain.request.TeamQuitRequest;
import com.lzy.makefriends.model.domain.request.TeamUpdateRequest;
import com.lzy.makefriends.model.dto.TeamQuery;
import com.lzy.makefriends.model.vo.TeamUserVO;

import java.util.List;

/**
* @author lizey
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-06-02 08:10:37
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param loginuser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginuser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除（解散）队伍
     *
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
