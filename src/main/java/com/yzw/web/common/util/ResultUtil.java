package com.yzw.web.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果工具类
 *
 * @author Yao
 * @Date: 2020/05/11/13:37
 */
@Slf4j
public class ResultUtil extends HashMap<String, Object>{

    /**
     * 私有化构造器
     */
    private ResultUtil() {
    }


    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态类型
     * @param msg 返回内容
     */
    public ResultUtil(Integer code, String msg)
    {
        super.put("code", code);
        super.put("msg", msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态类型
     * @param msg 返回内容
     * @param data 数据对象
     */
    public ResultUtil(Integer code, String msg, Object data)
    {
        super.put("code", code);
        super.put("msg", msg);
        if (data!=null) {
            super.put("data", data);
        }
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResultUtil success(String msg, Object data)
    {
        return new ResultUtil(200, msg, data);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ResultUtil success(Object data)
    {
        return ResultUtil.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResultUtil success(String msg)
    {
        return ResultUtil.success(msg, null);
    }


    /**
     * 返回错误消息
     *
     * @return
     */
    public static ResultUtil error()
    {
        return ResultUtil.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResultUtil error(String msg)
    {
        return ResultUtil.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResultUtil error(String msg, Object data)
    {
        return new ResultUtil(500, msg, data);
    }



    /**
     * 获取ajax提交后的返回数据
     *
     * @param msg 返回信息
     * @return
     */
    public static String getAjaxReturnJson(String msg, Object obj) {
        Map<String, Object> rtnMSG = new HashMap<String, Object>();
        rtnMSG.put("msg", msg);
        rtnMSG.put("code", 200);
        if (obj != null) {
            rtnMSG.put("data", obj);
        }
        return JSON.toJSONStringWithDateFormat(rtnMSG, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 获取ajax提交异常后的返回数据
     *
     * @param msg 返回信息
     * @return
     */
    public static String getFailAjaxReturnJson(String msg) {
        Map<String, Object> rtnMSG = new HashMap<String, Object>();
        rtnMSG.put("msg", msg);
        rtnMSG.put("code", 500);
        return JSON.toJSONString(rtnMSG);
    }

}