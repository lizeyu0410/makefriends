package com.lzy.makefriends.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzy.makefriends.common.BaseResponse;
import com.lzy.makefriends.common.ErrorCode;
import com.lzy.makefriends.common.ResultUtils;
import com.lzy.makefriends.exception.BusinessException;
import com.lzy.makefriends.model.domain.Team;
import com.lzy.makefriends.model.domain.User;
import com.lzy.makefriends.model.domain.request.TeamAddRequest;
import com.lzy.makefriends.model.domain.request.TeamJoinRequest;
import com.lzy.makefriends.model.domain.request.TeamUpdateRequest;
import com.lzy.makefriends.model.dto.TeamQuery;
import com.lzy.makefriends.model.vo.TeamUserVO;
import com.lzy.makefriends.service.TeamService;
import com.lzy.makefriends.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin
public class TeamController {
    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    /**
     * 创建队伍
     * @param teamAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request){
        if (teamAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    /**
     * 删除队伍
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(Long id){
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 修改队伍
     * @param teamUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request){
        if (teamUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据id查询队伍
     * @param id
     * @return
     */
    @PostMapping("/get")
    public BaseResponse<Team> getTeamById(Long id){
        if (id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        return ResultUtils.success(team);
    }

    /**
     * 根据条件查询队伍
     * @param teamQuery
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery, HttpServletRequest request){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, isAdmin);
        return ResultUtils.success(teamList);
    }

    /**
     * 根据条件分页查询队伍
     * @param teamQuery
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamByPage(TeamQuery teamQuery){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        // 将teamQuery中的属性复制到team中
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        Page<Team> resulPage = teamService.page(teamPage, queryWrapper);
        return ResultUtils.success(resulPage);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request){
        if (teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }
}
