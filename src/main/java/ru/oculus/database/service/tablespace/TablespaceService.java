package ru.oculus.database.service.tablespace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.oculus.database.model.Sid;
import ru.oculus.database.service.sid.SidService;

public class TablespaceService {

    @Autowired
    private SidService sidService;

    public List<TablespaceInfo> getAll(Sid sid) {
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        List<TablespaceInfo> result = template.query("select f.tablespace_name as name, trunc(sum(u.bytes)/1024/1024/1024, 0) total, trunc(sum(f.bytes)/1024/1024/1024,0) as free \n" +
        		"from  ( select tablespace_name , sum(bytes) as bytes from sys.dba_free_space group by tablespace_name)  f\n" +
        		"inner join ( select tablespace_name , sum(bytes) as bytes from sys.dba_data_files group by tablespace_name) u on u.tablespace_name = f.tablespace_name\n" +
        		"where f.tablespace_name = 'ASR_DATA'\n" +
        		"group by f.tablespace_name", new RowMapper<TablespaceInfo>() {

            public TablespaceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                TablespaceInfo ts = new TablespaceInfo();
                ts.setName(rs.getString("name"));
                ts.setTotalSpace(rs.getBigDecimal("total"));
                ts.setFreeSpace(rs.getBigDecimal("free"));
                return ts;
            }

        });
        return result;
    }
}
