package com.yzw.web.core.mapper;

import com.yzw.web.core.entity.Menu;
import com.yzw.web.core.entity.RmMap;
import com.yzw.web.core.entity.RmMapExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author Yao
 */
public interface RmMapMapper {
    /**
     * 根据主键删除数据库的记录,t_rm_map
     *
     * @param rmMap
     */
    int deleteByPrimaryKey(String rmMap);

    /**
     * 新写入数据库记录,t_rm_map
     *
     * @param record
     */
    int insert(RmMap record);

    /**
     * 动态字段,写入数据库记录,t_rm_map
     *
     * @param record
     */
    int insertSelective(RmMap record);

    /**
     * 根据指定的条件查询符合条件的数据库记录,t_rm_map
     *
     * @param example
     */
    List<RmMap> selectByExample(RmMapExample example);

    /**
     * 根据指定主键获取一条数据库记录,t_rm_map
     *
     * @param rmMap
     */
    RmMap selectByPrimaryKey(String rmMap);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,t_rm_map
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RmMap record);

    /**
     * 根据主键来更新符合条件的数据库记录,t_rm_map
     *
     * @param record
     */
    int updateByPrimaryKey(RmMap record);

    /**
     * 根据用户获取菜单(包括按钮)
     *
     * @param userNo
     * @return
     */
    List<Menu> findAllMenuByUserNo(@Param(value = "userNo") String userNo);

    /**
     * 根据用户获取菜单(不包括按钮)
     *
     * @param userNo
     * @return
     */
    List<Menu> findMenuByUserNotBt(@Param(value = "userNo") String userNo);
}