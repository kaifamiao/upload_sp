package com.pomelo.signalfile;

import com.pomelo.signalfile.config.FileConfig;
import com.pomelo.signalfile.job.DayDateUpload;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SignalFileApplication {

    public static void main(String[] args) throws SchedulerException {
        SpringApplication.run(SignalFileApplication.class, args);
        // 启动定时器 定时器执行上传文件的功能
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.clear();
        JobDetail jobDetail = JobBuilder.newJob(DayDateUpload.class)
                .withIdentity("dataUpdate", "dataUpdateGroup").build();
        // 每天下午7点执行
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(FileConfig.getCorn());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("dataUpdateTrigger", "dataUpdateTriggerGroup")
                .startNow()
                .withSchedule(cronSchedule).build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }

}
