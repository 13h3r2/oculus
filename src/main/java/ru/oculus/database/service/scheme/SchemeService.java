package ru.oculus.database.service.scheme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidService;

public class SchemeService {

    @Autowired
    private SidService sidService;

    public List<SchemeInfo> getAll(Sid sid) {
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        List<SchemeInfo> result = template.query(
                " select owner, ''||trunc(sum(bytes)/1024/1024/1024, 2), nvl(c_count, 0)"+
                " from dba_segments dbs"+
                " left outer join ( select count(*) as c_count, s.username as username from v$session s group by s.username) s on s.username = dbs.owner"+
                " group by owner, c_count"+
                " order by sum(bytes) desc",
            new RowMapper<SchemeInfo>() {
                public SchemeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SchemeInfo info = new SchemeInfo();
                    info.setName(rs.getString(1));
                    info.setSize(rs.getBigDecimal(2));
                    info.setConnectionCount(rs.getInt(3));
                    return info;
                }
            }
        );
        return result;
    }
}
