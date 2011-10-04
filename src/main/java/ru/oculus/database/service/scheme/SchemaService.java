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

public class SchemaService {

    @Autowired
    private SidService sidService;

    public SchemaInfo getSchemaInfo(Sid sid, String name) {
    	JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        String sql = " select dbs.owner, trunc(sum(bytes)/1024/1024/1024, 2), nvl(c_count, 0)"+
		" from dba_segments dbs"+
		" left outer join ( select count(*) as c_count, s.username as username from v$session s group by s.username) s on s.username = dbs.owner" +
		" where dbs.owner = '" + name + "'" +
        " group by dbs.owner, c_count"+
		" order by sum(bytes) desc";
        SchemaInfo result = template.queryForObject(sql, new RowMapper<SchemaInfo>() {

			public SchemaInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				SchemaInfo result = new SchemaInfo();
				result.setName(rs.getString(1));
                result.setSize(rs.getBigDecimal(2));
                result.setConnectionCount(rs.getInt(3));
				return result;
			}
        });
		result.setLastPatch(getLastVersion(template, result.getName()));
        return result;
    }

    public List<SchemaInfo> getAll(Sid sid, String withTable) {
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
		List<SchemaInfo> result = template.query(
                sql,
            new RowMapper<SchemaInfo>() {
                public SchemaInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    SchemaInfo info = new SchemaInfo();
                    info.setName(rs.getString(1));
                    info.setSize(rs.getBigDecimal(2));
                    info.setConnectionCount(rs.getInt(3));
                    return info;
                }
            }
        );

		for( SchemaInfo walker : result ) {
		    walker.setLastPatch(getLastVersion(template, walker.getName()));
		}


        return result;
    }

	private String getLastVersion(JdbcTemplate template, String name) {
		String query = "select * from (select * from ( select sprint as patch from "+name+".db_patches order by sprint desc, id desc ) where rownum = 1 union (select 'N/A' from dual)) where rownum=1";
		return template.queryForObject(query, new RowMapper<String>() {
		    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		        return rs.getString("patch");
		    }
		});
	}

	public void dropScheme(Sid sid, String name) {
	    JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
	    template.execute("drop user " + name + " cascade");
	}
}
