package ru.oculus.database.service.sid;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SidConnectionService {

    private static final Logger logger = Logger.getLogger(SidConnectionService.class);

    @Autowired
    private BeanFactory factory;

    private Map<Sid, DataSource> dss = new HashMap<Sid, DataSource>();

    public DataSource getDatasource(Sid sid) {
        DataSource result = dss.get(sid);
        if( result == null ) {
            synchronized (this) {
                result = dss.get(sid);
                if( result == null ) {
                    result = createDS(sid);
                    dss.put(sid, result);
                }
            }
        }
        return result;
    }

    private DataSource createDS(Sid sid) {
        logger.info("Created DS for " + sid.toString());
        org.apache.commons.dbcp.BasicDataSource dataSource = (BasicDataSource) factory.getBean("datasourcePrototype");
        dataSource.setUrl("jdbc:oracle:thin:@" + sid.getHost() + ":1521:" + sid.getSid());
        //dataSource.addConnectionProperty("internal_logon", "sysdba");
        dataSource.setUsername(sid.getSysLogin());
        dataSource.setPassword(sid.getSysPassword());
        return dataSource;
    }

}
