package ru.oculus.database.service.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidConnectionService;
import ru.sqlfactory.Statement;
import ru.sqlfactory.StatementLoader;

public class TableService {

    private static final Logger logger = Logger.getLogger(TableService.class);

    @Autowired
    private SidConnectionService sidService;

    public List<TableInfo> getAllTables(Sid sid, String schemaName) {
    	JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
    	Statement st = StatementLoader.loadStatement(TableService.class.getResource("get-all.sql"));
    	st.setParameter("owner", schemaName);
    	logger.info(st.toString());
    	return template.query(st.toString(), new RowMapper<TableInfo>() {
			public TableInfo mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				TableInfo result = new TableInfo();
				result.setName(rs.getString(1));
				result.setSize(rs.getBigDecimal(2));
				return result ;
			}
    		
    	});
    	
    }
}
