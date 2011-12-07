package ru.oculus.database.service.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import ru.oculus.database.service.sid.Sid;
import ru.oculus.database.service.sid.SidConnectionService;
import ru.sqlfactory.Statement;
import ru.sqlfactory.StatementLoader;
import ru.sqlfactory.converters.predefined.RawStringConverter;

public class SchemaMetadataService {

    private static String TABLE_NAME = "DUMP_METADATA";
    private static String INSTALLATION_DATE = "installationDate";
    private static String DUMP_DATE = "dumpDate";
    private static String DESCRIPTION = "description";

    @Autowired
    private SidConnectionService sidService;

    public SchemaMetadata getMetadata(String schemaName, Sid sid) {
        if (isMetadataTablePresent(schemaName, sid)) {
            JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
            final SchemaMetadata result = new SchemaMetadata();
            template.query("select * from " + schemaName + "." + TABLE_NAME, new RowCallbackHandler() {
                public void processRow(ResultSet rs) throws SQLException {
                    String name = rs.getString(1);
                    String value = rs.getString(2);
                    if (INSTALLATION_DATE.equals(name)) {
                        result.setInstallationDate(value);
                    }
                    if (DUMP_DATE.equals(name)) {
                        result.setDumpDate(value);
                    }
                    if (DESCRIPTION.equals(name)) {
                        result.setDescription(value);
                    }
                }
            });
            return result;
        }
        return SchemaMetadata.NO_METADATA;

    }
    
    public void saveMetadata(SchemaMetadata metadata, String schema, Sid sid) {
        if(!isMetadataTablePresent(schema, sid)) {
            createMetadataTable(schema, sid);
        }
        
        String fullTableName = schema + "." + TABLE_NAME;
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        Statement st = StatementLoader
                .loadStatement("delete from /*{:tableName*/DEV_ROMANCHUK.DUMP_METADATA/*}*/ where name = /*{:propName*/'description'/*}*/");
        st.setParameter("tableName", fullTableName, RawStringConverter.INSTANCE);
        st.setParameter("propName", DESCRIPTION);
        template.execute(st.toString());
        saveProperty(DESCRIPTION, metadata.getDescription(), schema, sid);
    }

    private void saveProperty(String name, String value, String schema, Sid sid) {
        String fullTableName = schema + "." + TABLE_NAME;
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        Statement st = StatementLoader
                .loadStatement("insert into /*{:tableName*/DEV_ROMANCHUK.DUMP_METADATA/*}*/ values ( /*{:propName*/'description'/*}*/, /*{:propValue*/'blah'/*}*/)");
        st.setParameter("tableNane", fullTableName);
        st.setParameter("propName", name);
        st.setParameter("propValue", value);
        template.execute(st.toString());
    }

    private void createMetadataTable(String schema, Sid sid) {
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        String fullTableName = schema + "." + TABLE_NAME;
        template.execute("create table " + fullTableName + " ( name varchar2(255), value varchar2(255) )");
    }

    private boolean isMetadataTablePresent(String schemaName, Sid sid) {
        Statement st = StatementLoader
                .loadStatement("select count(*) from all_tables where table_name = /*{:tableName*/'DB_PATCHES'/*}*/ and owner = /*{:schema*/'DEV_ROMANCHUK'/*}*/");
        st.setParameter("tableName", TABLE_NAME);
        st.setParameter("schema", schemaName);
        JdbcTemplate template = new JdbcTemplate(sidService.getDatasource(sid));
        return template.queryForInt(st.toString()) > 0;
    }
}
