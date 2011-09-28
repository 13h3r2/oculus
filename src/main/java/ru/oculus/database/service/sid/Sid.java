package ru.oculus.database.service.sid;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sid {
    private String host;
    private String sid;
    private String sysLogin;
    private String sysPassword;
    private String sshLogin;
    private String sshPassword;
    private String dumpDir;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSysLogin() {
        return sysLogin;
    }

    public void setSysLogin(String sysLogin) {
        this.sysLogin = sysLogin;
    }

    public String getSysPassword() {
        return sysPassword;
    }

    public void setSysPassword(String sysPassword) {
        this.sysPassword = sysPassword;
    }

    public String getSshLogin() {
        return sshLogin;
    }

    public void setSshLogin(String sshLogin) {
        this.sshLogin = sshLogin;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getDumpDir() {
        return dumpDir;
    }

    public void setDumpDir(String dumpDir) {
        this.dumpDir = dumpDir;
    }
}
