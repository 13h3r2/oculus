package ru.oculus.database.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import ru.oculus.database.service.sid.Sid;

public class SSHCommand {
    private Sid sid;

    private String command;

    public SSHCommand(Sid sid, String command) {
        super();
        this.sid = sid;
        this.command = command;
    }

    public String run() throws Exception {
        JSch jSch = new JSch();
        jSch.setHostKeyRepository(new OKHostKeyRepository());
        Session s = jSch.getSession(sid.getSshLogin(), sid.getHost());
        s.setPassword(sid.getSshPassword());
        s.connect();
        Channel channel = s.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setInputStream(null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        channel.setOutputStream(out);
        channel.connect();
        InputStream in = channel.getInputStream();

        StringBuilder result = new StringBuilder();
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                result.append(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
        channel.disconnect();
        s.disconnect();
        return result.toString();
    }
}
