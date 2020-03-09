package com.pucmm.isc415.Services;

import com.pucmm.isc415.Models.Url;
import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Utils.Constants;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class URLServices extends GenericServices<Url> {

    private static URLServices instance;

    private URLServices() {
        super(Url.class);
    }

    public static URLServices getInstance () {

        if (instance == null)
            instance = new URLServices();

        return instance;
    }

    public long getCount () {

        return (Long) getEntityManager().createQuery("SELECT COUNT(shortenedUrl) FROM Url").getSingleResult();
    }

    public String getUrlByShort (String shortUrl) {

        Query query = getEntityManager().createQuery("SELECT originalUrl FROM Url WHERE shortenedUrl =:shortUrl");
        query.setParameter("shortUrl", shortUrl);
        try{
            return (String) query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Url> lazyFind(int pageNumber, User user) {

        EntityManager em = getEntityManager();
        TypedQuery<Url> query;

        if (user.isAdmin()) {
            query = em.createQuery("SELECT url FROM Url url ORDER BY date DESC", Url.class);

        } else {
            query = em.createQuery("SELECT url FROM Url url WHERE url.owner = :user ORDER BY date DESC", Url.class);
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
