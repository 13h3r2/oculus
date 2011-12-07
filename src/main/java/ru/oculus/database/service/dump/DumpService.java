package ru.oculus.database.service.dump;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import ru.oculus.database.service.SSHCommand;
import ru.oculus.database.service.sid.Sid;

@XmlRootElement
public class DumpService {
    private static final Logger logger = Logger.getLogger(DumpService.class);

    public List<Dump> getAll(Sid sid) throws Exception {
        ArrayList<Dump> result = new ArrayList<Dump>();
        for (String walker : new SSHCommand(sid, "cd " + sid.getDumpDir() + " && ls -1 *.dmp").run().split("\n")) {
            result.add(new Dump(walker));
        }
        return result;
    }

//    public void installDumpAsync(final Sid sid, final String dumpName, final String schema, final String remapFrom) throws Exception {
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    String command = String.format("/u01/app/oracle/admin/scripts/create_test_schema/create_asr_schema.sh %s %s %s %s",
//                            sid.getSid(),
//                            schema,
//                            remapFrom,
//                            dumpName
//                            );
//                    logger.info(command);
//                    new SSHCommand(sid, command).run();
//                } catch (Exception e) {
//                    logger.error("", e);
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//    }
    //./imp_test_data.sh QA_1 QA_1 testpay1 U_SNS/*** BILLING1 one-house-dump-1.17.0.dmp
    public void installDumpAsync(final Sid sid, final String dumpName, final String schema, final String remapFrom) throws Exception {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String command = String.format("cd %s && ./imp_test_data.sh %s %s %s %s/%s %s %s",
                            sid.getDumpDir(),
                            schema,
                            schema,
                            sid.getSid(),
                            sid.getSysLogin(),
                            sid.getSysPassword(),
                            remapFrom,
                            dumpName
                            );
                    logger.info(command);
                    new SSHCommand(sid, command).run();
                } catch (Exception e) {
                    logger.error("", e);
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
