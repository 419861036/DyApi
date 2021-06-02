package com.exe.web.task;

import com.exe.base.CmdHandle;
import com.exe.base.vo.ExeResourceVo;
import com.exe.bo.RuleBo;
import com.exe.param.RuleParam;
import com.exe.web.BaseHandle;
import java.io.PrintStream;
import java.util.List;
import kkd.common.entity.Msg;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DefaultJob
        implements Job
{
    public void execute(JobExecutionContext context)
            throws JobExecutionException
    {
        Object params = context.getMergedJobDataMap().get("params");
        if (params == null)
        {
            System.out.println("id is null");
            return;
        }
        CmdHandle hanle = new CmdHandle();
        hanle.init();
        String paramsStr = params.toString();
        ExeResourceVo rule = getRule(paramsStr);
        if (rule == null)
        {
            System.out.println("rule is null");
            return;
        }
        hanle.setCaiRule(rule);
        hanle.start();
    }

    private ExeResourceVo getRule(String id)
    {
        List<ExeResourceVo> taskList = BaseHandle.getTaskList();
        if (taskList == null) {
            return null;
        }
        for (ExeResourceVo ruleVo : taskList)
        {
            Integer cid = ruleVo.getId();
            if (id.equalsIgnoreCase(cid.toString())) {
                return ruleVo;
            }
        }
        return null;
    }

    public List<ExeResourceVo> initData(String type)
    {
        Msg<List<ExeResourceVo>> msg = new Msg();
        RuleParam param = new RuleParam();
        param.setType(type);
        RuleBo.list(param, msg);
        return (List)msg.getV();
    }
}
