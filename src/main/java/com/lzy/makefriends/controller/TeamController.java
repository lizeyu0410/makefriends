package com.lzy.makefriends.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzy.makefriends.common.BaseResponse;
import com.lzy.makefriends.common.ErrorCode;
import com.lzy.makefriends.common.ResultUtils;
import com.lzy.makefriends.exception.BusinessException;
import com.lzy.makefriends.model.domain.Team;
import com.lzy.makefriends.model.dto.TeamQuery;
import com.lzy.makefriends.service.TeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("team")
@CrossOrigin
public class TeamController {
    @Resource
    private TeamService teamService;

    /**
     * 创建队伍
     * @param team
     * @return
     */
    @PostMapping("add")
    public BaseResponse<Long> addTeam(@RequestBody Team team){
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.save(team);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入失败");
        }
        return ResultUtils.success(team.getId());
    }

    /**
     * 删除队伍
     * @param id
     * @return
     */
    @PostMapping("delete")
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
     * @param team
     * @return
     */
    @PostMapping("update")
    public BaseResponse<Team> updateTeam(@RequestBody Team team){
        if (team == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.updateById(team);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return ResultUtils.success(team);
    }

    /**
     * 根据id查询队伍
     * @param id
     * @return
     */
    @PostMapping("get")
    public BaseResponse<Team> updateTeam(Long id){
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
    @PostMapping("list")
    public BaseResponse<List<Team>> listTeam(TeamQuery teamQuery){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        // 将teamQuery中的属性复制到team中
        BeanUtils.copyProperties(team, teamQuery);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
        return ResultUtils.success(teamList);
    }

    /**
     * 根据条件分页查询队伍
     * @param teamQuery
     * @return
     */
    @PostMapping("list/page")
    public BaseResponse<Page<Team>> listTeamByPage(TeamQuery teamQuery){
        if (teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        // 将teamQuery中的属性复制到team中
        BeanUtils.copyProperties(team, teamQuery);
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> teamPage = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        Page<Team> resulPage = teamService.page(teamPage, queryWrapper);
        return ResultUtils.success(resulPage);
    }

}
