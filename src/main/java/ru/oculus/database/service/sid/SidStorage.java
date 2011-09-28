package ru.oculus.database.service.sid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ru.oculus.database.model.Sid;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class SidStorage {
    @XmlElement(name = "sid")
    private List<Sid> sids = new ArrayList<Sid>();

    public List<Sid> getSids() {
        return sids;
    }

    public void setSids(List<Sid> sids) {
        this.sids = sids;
    }

    public String toXml() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(Sid.class, SidStorage.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(this, baos);
        baos.flush();
        return new String(baos.toByteArray());
    }

    public static SidStorage fromXml(InputStream input) throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance(Sid.class, SidStorage.class);

        Unmarshaller m = jc.createUnmarshaller();
        SidStorage result = (SidStorage) m.unmarshal(input);
        input.close();
        return result;
    }

}
