package com.example.jpa.dao;


import com.example.jpa.bean.ApiSendRecord;
import com.example.jpa.enums.BusinessType;
import com.example.jpa.enums.DataTrans;
import com.example.jpa.enums.FileSendStatus;
import com.example.jpa.enums.HandStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by chenzhengwei on 2017/4/23.
 */
public interface ApiSendRecordDAO extends BaseDAO<ApiSendRecord,Long>{

    List<ApiSendRecord> findByBankNo(@Param(value = "bankNo") String bankNo);

    List<ApiSendRecord> findByDataTransAndHandStatus(@Param(value = "dataTrans") DataTrans dataTrans, @Param(value = "handStatus") HandStatus handStatus);

    List<ApiSendRecord> findByBankNoAndDataTrans(@Param(value = "bankNo") String bankNo, @Param(value = "dataTrans") DataTrans dataTrans);

    List<ApiSendRecord> findByFileSendStatusAndHandStatus(@Param(value = "fileSendStatus") FileSendStatus fileSendStatus, @Param(value = "handStatus") HandStatus handStatus);

    @Query(value = "SELECT *  FROM api_send_record WHERE data_trans=0 AND business_type=0 AND bank_no=?1",nativeQuery = true)
    List<ApiSendRecord> findRequestByBankNo(@Param(value = "bankNo") String bankNo);

    List<ApiSendRecord> findByBankNoAndBusinessTypeOrderByIdDesc(@Param(value = "bankNo") String bankNo, @Param(value = "businessType") BusinessType businessType);

    /**找出身份证号相同的  并且相关联的配偶和担保人 并且没有发过拒绝的**/
    @Query(value = "select  *  from  api_send_record where bank_no in (select bank_no from chedai_bank_data_mapping where project_id in(select DISTINCT project_id from chedai_bank_data_mapping where serial_no=?1)) and business_type='0' and data_trans='0' and hand_status!=4",nativeQuery = true)
    List<ApiSendRecord> findNeedCloseByCerNo(@Param(value = "cerNo") String cerNo);

    /**超30天还没有发补件的**/
    @Query(value = "SELECT * FROM api_send_record a WHERE NOT EXISTS ( SELECT bank_no FROM api_send_record b WHERE business_type = 1 AND data_trans = 0 AND a.bank_no = b.bank_no ) AND hand_status = 0 AND date_sub(curdate(), INTERVAL 30 DAY) >= date(create_time)",nativeQuery = true)
    List<ApiSendRecord> findOverTimeRecord();

    /**每日征信查询与反馈总量以及相应日期**/
    @Query(value = "select a.date, ifnull(applyCount,0) applyCount, ifnull(notifyCount,0) notifyCount  from (select DATE_FORMAT(create_time, '%Y-%m-%d') as date ,count(1) as applyCount from api_send_record  where business_type=0 and data_trans=0 and create_time BETWEEN ?1 and ?2 GROUP BY DATE_FORMAT( create_time, '%Y-%m-%d')) a  left join (select DATE_FORMAT( create_time, '%Y-%m-%d') as date ,count(1) as notifyCount from api_send_record  where business_type=0 and data_trans=1 and create_time BETWEEN ?1 and ?2 GROUP BY DATE_FORMAT( create_time, '%Y-%m-%d'))b on a.date=b.date",nativeQuery = true)
    List<Object[]> findApplyNotifyAmount(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);

    /**反馈的当日征信的量 & 日期**/
    @Query(value = "select DISTINCT apply_time as date, count(1) as amount from (select bank_no, DATE_FORMAT(create_time, '%Y-%m-%d') apply_time, project_id from  api_send_record  where business_type=0 and data_trans=0 and bank_no!='' and create_time BETWEEN ?1 and ?2) a inner join (select bank_no, DATE_FORMAT (create_time, '%Y-%m-%d') notify_time, project_id from api_send_record where business_type=0 and data_trans=1 and bank_no!='' and create_time BETWEEN ?1 and ?2) b on a.bank_no=b.bank_no and a.project_id=b.project_id and a.apply_time=b.notify_time GROUP BY date ",nativeQuery = true)
    List<Object[]> findNotifyDaily(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);

    /**当日征信反馈用时 & 日期**/
    @Query(value = "select DISTINCT date,count(1)  from (select a.bank_no ,apply_time as date,TIMESTAMPDIFF(MINUTE,a.apply_create_time,b.notify_create_time)  as responseTime  from ( select bank_no, DATE_FORMAT(create_time, '%Y-%m-%d') apply_time ,create_time as apply_create_time,project_id from  api_send_record  where business_type=0 and data_trans=0 and bank_no!='' and create_time BETWEEN ?3 and ?4) a inner join (select bank_no, DATE_FORMAT(create_time, '%Y-%m-%d') notify_time,create_time as notify_create_time, project_id from api_send_record where business_type=0 and data_trans=1 and bank_no!='' and create_time BETWEEN ?3 and ?4) b on a.bank_no=b.bank_no and a.project_id=b.project_id and a.apply_time=b.notify_time ) c where c.responseTime BETWEEN ?1 and ?2  GROUP BY c.date ",nativeQuery = true)
    List<Object[]> findResponstTime(@Param(value = "startMin") int startMin, @Param(value = "endMin") int endMin, @Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);

    /**重推备注为空的征信反馈信息**/
    @Query(value = "select *  from api_send_record where request like ?1 and create_time> ?2 and bank_no in (select bank_no  from chedai_bank_data_mapping WHERE serial_no in('360502199501105621','360481198101140028','360481198606270119','450881199501105326','150125198610202411','452325198109030314','230622199006096168','450326198603281842','45242519751103041X','510811197404164971','341621198508275110','430521198901299229') )",nativeQuery = true)
    List<ApiSendRecord> findNullinvestble(@Param(value = "str") String str, @Param(value = "Time") String Time);

    /**重推指定的loanNotify**/
    @Query(value = "select  *  from  api_send_record where bank_no=?1 and business_type='1' and data_trans='1'",nativeQuery = true)
    List<ApiSendRecord> findResendFailedLoanNotify(@Param(value = "bankNo") String bankNo);

    /**重推所属机构错误的补件**/
    @Query(value = "select * from api_send_record  where project_id in('2000135618','2000135883','2000135865','2000144046','2000147733','2000147733','2000145651','2000151138','2000149520','2000147711','2000145077','2000151676','2000151855','2000150180','2000152903','2000151107','2000151128','2000154669','2000157687','2000157684','2000161157','2000159068','2000158022','2000155923','2000162655','2000162006','2000162977','2000162703','2000164321','2000162475','2000163873','2000160544','2000164533','2000166297','2000163660','2000165524','2000163203','2000163199','2000165943','2000165524','2000164533','2000165524','2000164533','2000164482','2000166297','2000166003','2000167075','2000166732','2000165524','2000164533','2000165293','2000166817','2000166909','2000166674') and bank_no !='' and response  not like '%该申请件不存在或已处理%' and data_trans=0",nativeQuery = true)
    List<ApiSendRecord> findregenerateLoanSend();

    /**阿里监控 15分钟内重试超过3次的**/
    @Query(value = "select *  from api_send_record where create_time BETWEEN ?1 and ?2 and retries>3",nativeQuery = true)
    List<ApiSendRecord> findRetry3Times(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);

    /**阿里监控 白天半小时内没有接收数据 检查中间件接收是否正常**/
    @Query(value = "select *  from api_send_record where create_time BETWEEN ?1 and ?2",nativeQuery = true)
    List<ApiSendRecord> findReceiveNormal(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);

    /**阿里监控 发送温州银行 30分钟内发送失败次数超过10次，检查代码是否有问题**/
    @Query(value = "select *  from api_send_record where create_time BETWEEN ?1 and ?2 and hand_status=1",nativeQuery = true)
    List<ApiSendRecord> findFailTimes(@Param(value = "startDate") String startDate, @Param(value = "endDate") String endDate);


    List<ApiSendRecord> findByProjectIdAndHandStatus(@Param(value = "projectId") Long projectId, @Param(value = "handStatus") HandStatus handStatus);
}
