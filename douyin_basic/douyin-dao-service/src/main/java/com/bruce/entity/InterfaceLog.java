package com.bruce.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * 接口访问日志表（记录UserController所有接口调用信息）(InterfaceLog)表实体类
 *
 * @author makejava
 * @since 2025-11-12 09:24:28
 */
@SuppressWarnings("serial")
public class InterfaceLog extends Model<InterfaceLog> {
    //主键ID
    private Long id;
    //请求唯一标识（UUID前8位或完整UUID）
    private String requestId;
    //请求完整URL
    private String requestUrl;
    //HTTP请求方式（GET/POST/PUT/DELETE等）
    private String httpMethod;
    //控制器方法名（如UserController的doLogin）
    private String controllerMethod;
    //请求参数（JSON格式存储数组或对象）
    private String requestParams;
    //响应结果（JSON格式，异常时可为空）
    private String responseResult;
    //接口开始执行时间
    private Date startTime;
    //执行耗时（毫秒）
    private Long costTime;
    //执行状态（0-异常，1-成功）
    private Integer status;
    //异常信息（状态为0时存储）
    private String errorMsg;
    //客户端IP地址
    private String ipAddress;
    //日志创建时间
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getControllerMethod() {
        return controllerMethod;
    }

    public void setControllerMethod(String controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    }

