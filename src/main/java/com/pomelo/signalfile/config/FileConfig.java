package com.pomelo.signalfile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "file")
public class FileConfig {

    private static String directory;
    private static String corn;
    private static String suffix;
    private static String queue;
    private static String ip;
    private static String user;
    private static String password;
    private static String port;
    private static String filePath;

    public static String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        FileConfig.queue = queue;
    }

    public static String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        FileConfig.suffix = suffix;
    }

    public static String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        FileConfig.directory = directory;
    }

    public static String getCorn() {
        return corn;
    }

    public void setCorn(String corn) {
        FileConfig.corn = corn;
    }

    public static String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        FileConfig.ip = ip;
    }

    public static String getUser() {
        return user;
    }

    public void setUser(String user) {
        FileConfig.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        FileConfig.password = password;
    }

    public static String getPort() {
        return port;
    }

    public void setPort(String port) {
        FileConfig.port = port;
    }

    public static String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        FileConfig.filePath = filePath;
    }


}
