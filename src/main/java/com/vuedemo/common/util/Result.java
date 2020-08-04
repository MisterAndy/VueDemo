package com.vuedemo.common.util;

import java.util.LinkedHashMap;

import com.vuedemo.common.constant.HttpStatus;

/**
 * 操作消息提醒
 *
 * @author qipd
 */
public class Result extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "message";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     */
    public Result() {}

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code
     *            状态码
     * @param msg
     *            返回内容
     */
    public Result(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code
     *            状态码
     * @param msg
     *            返回内容
     * @param data
     *            数据对象
     */
    public Result(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回消息
     *
     * @param code
     *            状态码
     * @param msg
     *            返回内容
     * @param data
     *            数据对象
     * @return 成功消息
     */
    public static Result response(int code, String msg, Object data) {

        return new Result(code, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static Result success() {

        return Result.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static Result success(Object data) {

        return Result.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg
     *            返回内容
     * @return 成功消息
     */
    public static Result success(String msg) {

        return Result.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg
     *            返回内容
     * @param data
     *            数据对象
     * @return 成功消息
     */
    public static Result success(String msg, Object data) {

        return response(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param code
     *            状态码
     * @param msg
     *            返回内容
     * @return 成功消息
     */
    public static Result success(int code, String msg) {

        return response(code, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static Result fail() {

        return Result.fail("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg
     *            返回内容
     * @return 警告消息
     */
    public static Result fail(String msg) {

        return Result.fail(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg
     *            返回内容
     * @param data
     *            数据对象
     * @return 警告消息
     */
    public static Result fail(String msg, Object data) {

        return response(HttpStatus.BAD_REQUEST, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code
     *            状态码
     * @param msg
     *            返回内容
     * @return 警告消息
     */
    public static Result fail(int code, String msg) {

        return response(code, msg, null);
    }

}
