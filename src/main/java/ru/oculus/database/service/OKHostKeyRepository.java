package ru.oculus.database.service;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.UserInfo;


class OKHostKeyRepository implements HostKeyRepository {
    public void remove(String host, String type, byte[] key) {
        // TODO Auto-generated method stub

    }

    public void remove(String host, String type) {
        // TODO Auto-generated method stub

    }

    public String getKnownHostsRepositoryID() {
        // TODO Auto-generated method stub
        return null;
    }

    public HostKey[] getHostKey(String host, String type) {
        // TODO Auto-generated method stub
        return null;
    }

    public HostKey[] getHostKey() {
        // TODO Auto-generated method stub
        return null;
    }

    public int check(String host, byte[] key) {
        return HostKeyRepository.OK;
    }

    public void add(HostKey hostkey, UserInfo ui) {
        // TODO Auto-generated method stub

    }
}