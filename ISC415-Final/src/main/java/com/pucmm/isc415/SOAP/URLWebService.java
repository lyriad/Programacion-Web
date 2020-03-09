package com.pucmm.isc415.SOAP;

import com.pucmm.isc415.Models.Url;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface URLWebService {

    @WebMethod
    List<Url> getUrlsFromUser (String username);

    @WebMethod
    Url shortenUrl (String url, String username);
}
