package com.pucmm.isc415.Services;

import com.pucmm.isc415.Models.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class UserServices extends GenericServices<User> {

    private static UserServices instance;

    private UserServices(){
        super(User.class);
    }

    public static UserServices getInstance() {

        if(instance == null)
            instance = new UserServices();

        return instance;
    }

    public List<User> getAdmins () {

        Query query = getEntityManager().createQuery("FROM User WHERE admin = true");

        try {
            return query.getResultList();
        } catch(NoResultException e) {
            return null;
        }
    }
}
