package com.pomelo.signalfile.job;

import com.pomelo.signalfile.common.FtpUtils;
import com.pomelo.signalfile.config.FileConfig;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DayDateUpload implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long start = System.currentTimeMillis();
        Logger logger = LoggerFactory.getLogger(getClass());
        Date date = new Date();
        logger.info("上传文件：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
//        String now = new SimpleDateFormat("yyyyMMdd").format(date);
//        now = FileConfig.getDirectory() + now + FileConfig.getSuffix();
        try {
            FtpUtils.batch(FileConfig.getDirectory());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        logger.info("上传完成，耗时：" + (end - start) + "ms");
    }
}
