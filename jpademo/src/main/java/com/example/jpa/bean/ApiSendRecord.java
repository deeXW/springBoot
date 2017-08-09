package com.example.jpa.bean;

import com.example.jpa.enums.BusinessType;
import com.example.jpa.enums.DataTrans;
import com.example.jpa.enums.FileSendStatus;
import com.example.jpa.enums.HandStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenzhengwei on 2017/4/23.
 * 发送请求日志表
 */
@Table(name="api_send_record")
@Data
@Entity
public class ApiSendRecord implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name= "chedai_app_id", nullable = false, length = 20, columnDefinition = "varchar(20) COMMENT '多车贷类型cheguo/crw'")
    private String chedaiAppId;

    /**
     * 车贷projectId
     */
    @Column(name = "project_id", nullable = false, length=20, columnDefinition ="bigint(20) COMMENT '对应车贷传过来的征信projectId'")
    private Long projectId;

    /**
     * 对应apiServiceRecord主键
     */
    @Column(name= "api_service_id", nullable = false, length = 20, columnDefinition = "bigint(20) COMMENT '对应apiServiceRecord主键'")
    private Long apiServiceId;

    /**
     * 银行主键
     */
    @Column(name = "bank_no", nullable = false, length=30, columnDefinition = "varchar(30) COMMENT '银行主键'")
    private String bankNo;

    /**
     * 银行类型 wzbc
     */
    @Column(name = "bank_code", nullable = false, length=30, columnDefinition = "varchar(30) COMMENT '银行类型'")
    private String bankCode;

    /**
     * 请求地址
     */
    @Column(name = "send_url", nullable = false, length=100, columnDefinition = "varchar(100) COMMENT '请求地址'")
    private String sendUrl;

    /**
     * 请求参数
     */
    @Column(name = "request", length=5000, columnDefinition = "varchar(5000) COMMENT '请求参数'")
    private String request;

    /**
     * 返回参数
     */
    @Column(name = "response", length=200,columnDefinition = "varchar(200) COMMENT '信息返回结果'")
    private String response;

    /**
     * 响应时间
     */
    @Column(name = "response_time", length = 20, columnDefinition = "bigint(20) COMMENT '响应时间'")
    private Long responseTime;

    /**
     * 重试次数
     */
    @Column(name = "retries", length=13, columnDefinition = "varchar(13) COMMENT '重试次数'")
    private Integer retries;

    /**
     * 请求状态
     */
    @Column(name = "hand_status",  length=20,columnDefinition = "int(11) COMMENT '请求发送状态 0：成功， 1：失败，2：未连接成功'，3：人工处理")
    private HandStatus handStatus;

    /**
     * 文件发送状态
     */
    @Column(name = "file_send_status",  length=20,  columnDefinition = "int(11) COMMENT '文件发送状态 0:成功，1：失败，2:已发送待处理，3:失败超出限制'")
    private FileSendStatus fileSendStatus;

    /**
     * 文件发送失败次数
     */
    @Column(name = "send_fail_count", length=11, columnDefinition="int(11) COMMENT '文件发送失败次数'")
    private Integer sendFailCount;

    /**
     * 创建时间
     */
    @Column(name = "create_time", columnDefinition="COMMENT '创建时间'")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", columnDefinition="COMMENT '更新时间'")
    private Date updateTime;

    /**
     * 业务类型
     */
    @Column(name = "business_type", nullable = false, length=20, columnDefinition="COMMENT '业务类型 0:征信，1：审贷'")
    private BusinessType businessType;


    /**
     * 传输方向
     */
    @Column(name = "data_trans", nullable = false, length=20, columnDefinition="COMMENT '传输方向 0：车贷到银行，1：银行到车贷'")
    private DataTrans dataTrans;

    /**
     * 附件地址
     */
    @Column(name = "file_path", length=200, columnDefinition = "varchar(200) COMMENT '附件地址'")
    private String filePath;

    /**
     * 额外字段
     */
    @Column(name = "extra", length=500, columnDefinition = "varchar(500) COMMENT '文件上传接口返回报文'")
    private String extra;



}
