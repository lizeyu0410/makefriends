package com.lzy.makefriends.model.dto;
import com.lzy.makefriends.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 队伍查询封装类
 * @author lizey
 */

@EqualsAndHashCode(callSuper = true) // 父类属性进行比较
@Data
public class TeamQuery extends PageRequest {

    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * id 列表
     */
    private List<Long> idList;

    /**
     * 描述
     */
    private String description;

    /**
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    private String searchText;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;
}
