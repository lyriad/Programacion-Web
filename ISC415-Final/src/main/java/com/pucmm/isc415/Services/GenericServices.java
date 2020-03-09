package com.pucmm.isc415.Services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.Field;
import java.util.List;

public class GenericServices<T> {

    private EntityManagerFactory factory;
    private Class<T> entityClass;

    public GenericServices() {
        if(factory == null) {
            factory = Persistence.createEntityManagerFactory("PersistenceUnit");
        }
        this.entityClass = entityClass;
    }

    public GenericServices(Class<T> entityClass) {

        try {

            this.entityClass = entityClass;
            this.factory = Persistence.createEntityManagerFactory("PersistenceUnit");

        } catch(Throwable ex) {

            System.out.println("ERROR: Failed to create sessionFactory object." + ex);

            throw new ExceptionInInitializerError(ex);
        }
    }

    public EntityManager getEntityManager() {

        return factory.createEntityManager();
    }

    private Object getFieldValue(T entity) {

        if(entity == null) return null;

        for (Field field : entity.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Id.class)) {

                try {
                    field.setAccessible(true);
                    return field.get(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void create(T entity) {

        EntityManager em = getEntityManager();

        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();

        } catch (Exception ex) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public void update(T entity) {

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entity);
            em.getTransaction().commit();
        }catch (Exception ex){
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public void delete(Object id) {

        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        try {
            T entity = em.find(entityClass, id);
            em.remove(entity);
            em.getTransaction().commit();

        } catch (Exception ex) {

            em.getTransaction().rollback();

        } finally {
            em.close();
        }
    }

    public T get (Object id) {

        EntityManager em = getEntityManager();

        try {
            return em.find(entityClass, id);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public List<T> list() {

        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(entityClass);
            criteriaQuery.select(criteriaQuery.from(entityClass));
            return em.createQuery(criteriaQuery).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            em.close();
        }
    }
}
