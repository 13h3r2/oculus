package ru.oculus.database.service.sid;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;
import javax.xml.bind.JAXBException;

import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class SidService {
    private SidStorage storage;

    public List<Sid> getAllSids() throws JAXBException, IOException {
        if (storage == null) {
            storage = SidStorage.fromXml(SidService.class.getClassLoader().getResourceAsStream("sids.xml"));
        }
        return storage.getSids();
    }

    public Sid getSid(String host, String sid) throws JAXBException, IOException {
        for (Sid walker : getAllSids()) {
            if (walker.getHost().equals(host) && walker.getSid().equals(sid)) {
                return walker;
            }
        }
        return null;
    }

    public DataSource getDatasource(Sid sid) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@" + sid.getHost() + ":1521:" + sid.getSid());
        Properties cp = new Properties();
        cp.put("internal_logon", "sysdba");
        dataSource.setConnectionProperties(cp );
        dataSource.setUsername(sid.getSysLogin());
        dataSource.setPassword(sid.getSysPassword());
        return dataSource;
    }

}
