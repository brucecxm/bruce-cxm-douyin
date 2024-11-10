package com.bruce.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data

public class OnlineTest {
    private String taskName;
    private String sn;
    private Date surveySdate;
    private Date surveySdate1;
    private Date surveyEdate;
    private String surveyTime;
    private String lanId;
    private String regionId;
    private String staffName;
    private String orgName;
    private String examScore;
    private String examScore1;
    private String exOrder;
    private String exOrderLan;
    private String exOrder1;
    private String exOrderLan1;
    private String channelCode;
    private String startTime;
    private Date twoSurveyDate;
    private Date threeSurveyDate;

}
