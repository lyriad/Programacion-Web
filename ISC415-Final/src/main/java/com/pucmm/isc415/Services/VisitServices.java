package com.pucmm.isc415.Services;

import com.pucmm.isc415.Models.User;
import com.pucmm.isc415.Models.Visit;

import javax.persistence.Query;

public class VisitServices extends GenericServices<Visit> {

    private static VisitServices instance;

    private VisitServices(){
        super(Visit.class);
    }

    public static VisitServices getInstance() {

        if(instance == null)
            instance = new VisitServices();

        return instance;
    }

    public long getSizeVisitByBrowser(String browser){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.browser =:browser");
        query.setParameter("browser", browser);
        return (long) query.getSingleResult();
    }

    public long getSizeVisitByOs(String os){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.os =:os");
        query.setParameter("os", os);
        return (long) query.getSingleResult();
    }

    public long getSizeVisitByShortUrl(String urlShort){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.url.shortenedUrl =:urlShort");
        query.setParameter("urlShort", urlShort);
        return (long) query.getSingleResult();
    }

    public long getSizeVisitByShortUrlBrowser(String urlShort, String browser){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.url.shortenedUrl =:urlShort and" +
                " v.browser =: browser");
        query.setParameter("urlShort", urlShort);
        query.setParameter("browser", browser);
        return (long) query.getSingleResult();
    }

    public long getSizeVisitByShortUrlOs(String urlShort, String os){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.url.shortenedUrl =:urlShort and" +
                " v.os =: os");
        query.setParameter("urlShort", urlShort);
        query.setParameter("os", os);
        return (long) query.getSingleResult();
    }
    public long getSizeByDay(String dia){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.day =:dia");
        query.setParameter("dia", dia);
        return (long) query.getSingleResult();
    }

    public long getSizeByShortUrlDay(String urlShort, String dia){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.day =:dia and " +
                "v.url.shortenedUrl =:urlShort");
        query.setParameter("urlShort", urlShort);
        query.setParameter("dia", dia);
        return (long) query.getSingleResult();
    }

    public long getSizeBytime(long hora){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.time =:hora");
        query.setParameter("hora", hora);
        return (long) query.getSingleResult();
    }

    public long getSizeByShortUrltime(String urlShort, long hora){
        Query query = getEntityManager().createQuery("Select count(v.id) from Visit v where v.time =:hora and " +
                "v.url.shortenedUrl =:urlShort");
        query.setParameter("urlShort", urlShort);
        query.setParameter("hora", hora);
        return (long) query.getSingleResult();
    }

}
