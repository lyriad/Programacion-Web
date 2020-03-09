package com.pucmm.isc415.SOAP;

import com.pucmm.isc415.Helpers.UrlHelper;
import com.pucmm.isc415.Models.Url;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Services.URLServices;
import com.pucmm.isc415.Services.UserServices;
import com.pucmm.isc415.Utils.Constants;
import com.pucmm.isc415.Utils.MyParser;
import kong.unirest.json.JSONObject;
import javax.jws.WebService;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebService (endpointInterface = "com.pucmm.isc415.SOAP.URLWebService")
public class URLWebServiceImpl implements URLWebService {

    @Override
    public List<Url> getUrlsFromUser(String username) {
        return UserServices.getInstance().get(username).getMyUrls();
    }

    @Override
    public Url shortenUrl (String originalUrl, String username) {

        if (originalUrl == null || originalUrl.isEmpty()) {
            return null;
        }

        User mUser = null;

        if (username != null && !username.isEmpty()) {
            mUser = UserServices.getInstance().get(username);

            if (mUser == null) {
                return null;
            }
        }

        String shortUrl = Constants.DOMAIN + "/r/"
                + MyParser.base62Encode(URLServices.getInstance().getCount() + 1);

        //If mUser is null, register URL as visitor
        Map<String, String> preview_result = UrlHelper.getPrevia(originalUrl);
        Url url = new Url(originalUrl, shortUrl, mUser, new Timestamp(new Date().getTime()),
                preview_result.get("image"), preview_result.get("description"));
        URLServices.getInstance().create(url);

        //If mUser is not null, add url to their shortened urls
        if (mUser != null) {
            mUser.getMyUrls().add(url);
            UserServices.getInstance().update(mUser);
        }

        return url;
    }
}
