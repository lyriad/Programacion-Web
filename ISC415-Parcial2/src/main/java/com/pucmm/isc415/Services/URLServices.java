package com.pucmm.isc415.Services;

import com.pucmm.isc415.Models.URL;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Utils.Constants;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class URLServices extends GenericServices<URL> {

    private static URLServices instance;

    private URLServices() {
        super(URL.class);
    }

    public static URLServices getInstance () {

        if (instance == null)
            instance = new URLServices();

        return instance;
    }

    public long getCount () {

        return (Long) getEntityManager().createQuery("SELECT COUNT(shortenedUrl) FROM URL").getSingleResult();
    }

    public String getUrlByShort (String shortUrl) {

        Query query = getEntityManager().createQuery("SELECT originalUrl FROM URL WHERE shortenedUrl =:shortUrl");
        query.setParameter("shortUrl", shortUrl);
        try{
            return (String) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<URL> lazyFind(int pageNumber, User user) {

        EntityManager em = getEntityManager();
        TypedQuery<URL> query;

        if (user.isAdmin()) {
            query = em.createQuery("SELECT url FROM URL url ORDER BY date DESC", URL.class);

        } else {
            query = em.createQuery("SELECT url FROM URL url WHERE url.owner = :user ORDER BY date DESC", URL.class);
            query.setParameter("user", user);
        }

        try {

            query.setFirstResult((pageNumber - 1) * Constants.PAGE_SIZE);
            query.setMaxResults(Constants.PAGE_SIZE);
            return query.getResultList();

        } catch (NoResultException e) {
            return null;
        }
    }

}
