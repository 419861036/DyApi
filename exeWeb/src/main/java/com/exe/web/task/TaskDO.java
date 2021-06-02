package com.exe.web.task;

public class TaskDO
{
    private String id;
    private String jobName;
    private String jobGroup;
    private String beanClass;
    private String params;
    private String cronExpression;

    public String getJobName()
    {
        return this.jobName;
    }

    public String getJobGroup()
    {
        return this.jobGroup;
    }

    public String getBeanClass()
    {
        return this.beanClass;
    }

    public String getCronExpression()
    {
        return this.cronExpression;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    public void setBeanClass(String beanClass)
    {
        this.beanClass = beanClass;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParams()
    {
        return this.params;
    }

    public void setParams(String params)
    {
        this.params = params;
    }
}
