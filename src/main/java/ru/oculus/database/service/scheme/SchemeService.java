package ru.oculus.database.service.scheme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidService;

public class SchemeService {

    @Autowired
    private SidService sidService;

    public List<SchemeInfo> getAll(Sid sid, String withTable) {
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        String sql = " select dbs.owner, trunc(sum(bytes)/1024/1024/1024, 2), nvl(c_count, 0)"+
		" from dba_segments dbs"+
		" inner join all_catalog t on t.owner = dbs.owner"+
		" left outer join ( select count(*) as c_count, s.username as username from v$session s group by s.username) s on s.username = dbs.owner"
		;
        if( !StringUtils.isEmpty(withTable) ) {
        	sql += " where 1=1 " +
        			"and t.table_name = '"+withTable+"'";
        }
        sql += " group by dbs.owner, c_count"+
		" order by sum(bytes) desc";
		List<SchemeInfo> result = template.query(
                sql,
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
