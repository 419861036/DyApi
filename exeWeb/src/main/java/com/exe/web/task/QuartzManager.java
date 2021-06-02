package com.exe.web.task;

import java.util.HashMap;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager
{
    Scheduler scheduler = null;

    public static void main(String[] args)
    {
        QuartzManager qm = get();
        TaskDO task = new TaskDO();
        task.setBeanClass("com.exe.web.task.TestJob");
        task.setCronExpression("0/10 *  *  *  * ?");
        task.setJobGroup("test");
        task.setJobName("com");
        task.setId("1");
        task.setParams("1");


        qm.addJob(task);

        try{
            task.setBeanClass(null);
            task.setId(null);
            task.setCronExpression(null);
            task.setParams(null);
            task.setJobName("1");
            String state=qm.getState(task);
            System.out.println("------"+state);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static HashMap<String, QuartzManager> qmMap = new HashMap();

    public static QuartzManager get()
    {
        String key = "default";
        QuartzManager qm = (QuartzManager)qmMap.get(key);
        if (qm == null)
        {
            qm = new QuartzManager();
            qmMap.put(key, qm);
        }
        return qm;
    }

    public void start()
    {
        try
        {
            if (this.scheduler != null)
            {
                if (!this.scheduler.isStarted()) {
                    this.scheduler.start();
                }
                return;
            }
            this.scheduler = new StdSchedulerFactory().getScheduler();
            this.scheduler.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initSchedule()
            throws SchedulerException
    {}

    public void addJob(TaskDO task)
    {
        try
        {
            start();

            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(task.getBeanClass()).newInstance().getClass();

            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(task.getJobName(), task.getJobGroup()).usingJobData("id", task.getId()).usingJobData("params", task.getParams()).build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getJobName(), task.getJobGroup()).startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND)).withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression())).startNow().build();

            this.scheduler.scheduleJob(jobDetail, trigger);
            if (!this.scheduler.isShutdown()) {
                this.scheduler.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void pauseJob(TaskDO task)
            throws SchedulerException
    {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        this.scheduler.pauseJob(jobKey);
    }

    public void resumeJob(TaskDO task)
            throws SchedulerException
    {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        this.scheduler.resumeJob(jobKey);
    }

    public void deleteJob(TaskDO task)
            throws SchedulerException
    {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        this.scheduler.deleteJob(jobKey);
    }

    public void runJobNow(TaskDO task)
            throws SchedulerException
    {
        JobKey jobKey = JobKey.jobKey(task.getJobName(), task.getJobGroup());
        this.scheduler.triggerJob(jobKey);
    }

    public void updateJobCron(TaskDO task)
            throws SchedulerException
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
        CronTrigger trigger = (CronTrigger)this.scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
        trigger = (CronTrigger)trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        this.scheduler.rescheduleJob(triggerKey, trigger);
    }

    public String getState(TaskDO task)
            throws SchedulerException
    {
        TriggerKey triggerKey = TriggerKey.triggerKey(task.getJobName(), task.getJobGroup());
        return this.scheduler.getTriggerState(triggerKey).toString();
    }
}
