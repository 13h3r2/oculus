import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ru.oculus.database.model.Sid;
import ru.oculus.database.service.sid.SidStorage;

public class Main2 {

    public static void main(String[] args) throws IOException, JAXBException {
        Sid sid = new Sid();
        sid.setDumpDir("/DB/oradata/exp");
        sid.setHost("mox04.sibirenergo-billing.ru");
        sid.setSid("qaasr");
        sid.setSshLogin("1");
        sid.setSshPassword("1");
        sid.setSysLogin("SYSMAN");
        sid.setSysPassword("*******");

        SidStorage storage = new SidStorage();
        storage.getSids().add(sid);

        sid = new Sid();
        sid.setDumpDir("/DB/oradata/exp");
        sid.setHost("mox05.sibirenergo-billing.ru");
        sid.setSid("prodasr");
        sid.setSshLogin("1");
        sid.setSshPassword("1");
        sid.setSysLogin("SYSMAN");
        sid.setSysPassword("********");
        storage.getSids().add(sid);

        JAXBContext jc = JAXBContext.newInstance(Sid.class, SidStorage.class);

        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(storage, System.out);
    }
}