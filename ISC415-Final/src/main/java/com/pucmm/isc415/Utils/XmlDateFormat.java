package com.pucmm.isc415.Utils;

import java.text.SimpleDateFormat;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;

public class XmlDateFormat extends XmlAdapter<String, Timestamp>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String marshal(Timestamp v) throws Exception {
        return dateFormat.format(v);
    }

    @Override
    public Timestamp unmarshal(String v) throws Exception {
        return new Timestamp(dateFormat.parse(v).getTime());
    }
}
