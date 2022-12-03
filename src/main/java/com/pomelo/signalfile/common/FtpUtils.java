package com.pomelo.signalfile.common;

import com.jcraft.jsch.*;
import com.pomelo.signalfile.config.FileConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FtpUtils {

    private static final Logger logger;
    private static final Session session;
    private static ChannelSftp sftp;

    /*
     * 通过SFTP协议建立连接
     */
    static {
        logger = LoggerFactory.getLogger(FtpUtils.class);
        JSch jsch = new JSch();
        try {
            if (Integer.parseInt(FileConfig.getPort()) <= 0) {
                //连接服务器，采用默认端口
                session = jsch.getSession(FileConfig.getUser(), FileConfig.getIp());
            } else {
                //采用指定的端口连接服务器
                session = jsch.getSession(FileConfig.getUser(), FileConfig.getIp(), Integer.parseInt(FileConfig.getPort()));
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        //如果服务器连接不上，则抛出异常
        if (session == null) {
            logger.error("session is null");
        } else {
            //设置登陆主机的密码
            session.setPassword(FileConfig.getPassword());//设置密码
            //设置第一次登陆的时候提示，可选值：(ask | yes | no)
            session.setConfig("StrictHostKeyChecking", "no");
            //设置登陆超时时间
            try {
                session.connect(30000);
                logger.info("登录成功！");
                //创建sftp通信通道
                Channel channel = (Channel) session.openChannel("sftp");
                channel.connect(1000);
                sftp = (ChannelSftp) channel;
                //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
                sftp.cd(FileConfig.getFilePath());
            } catch (JSchException | SftpException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 上传单个文件
     *
     * @param fileName 文件地址
     */
    public static void sshSftp(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        String name = !file.getName().contains("全部Ａ股") ? file.getName() : StringUtils.substringAfter(file.getName(), "全部Ａ股");
        try {
            sftp.put(fileInputStream, name);
        } catch (SftpException e) {
            logger.error(e.getMessage());
        }
        logger.info("上传成功:" + FileConfig.getFilePath() + name);
    }

    /**
     * 上传文件夹下所有文件
     *
     * @param dir 目录
     */
    public static void batch(String dir) throws FileNotFoundException {
        File file = new File(dir);
        if (!file.isDirectory()) { // 如果不是目录 执行上传单个的上传操作
            try {
                sshSftp(dir);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        File[] files = file.listFiles((f) -> f.getName().endsWith(FileConfig.getSuffix()));
        assert files != null;
        for (File f : files) {
            FileInputStream fileInputStream = new FileInputStream(f);
            String name = !f.getName().contains("全部Ａ股") ? f.getName() : StringUtils.substringAfter(f.getName(), "全部Ａ股");
            try {
                sftp.put(fileInputStream, name);
            } catch (SftpException e) {
                logger.error(e.getMessage());
            }
            logger.info("上传成功:" + FileConfig.getFilePath() + name);
        }
    }
}
