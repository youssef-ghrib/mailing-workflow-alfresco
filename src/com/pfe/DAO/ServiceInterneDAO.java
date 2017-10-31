package com.pfe.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.Employe;
import com.pfe.entities.ServiceInterne;

public class ServiceInterneDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public ServiceInterneDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    // Ajouter un service
    public void ajouter( ServiceInterne s ) {
        tx.begin();
        em.persist( s );
        tx.commit();
    }

    // Modifier un service
    public void modifier( ServiceInterne s ) {
        tx.begin();
        em.merge( s );
        tx.commit();
    }

    // Consulter un service
    public ServiceInterne consulter( int id ) {
        ServiceInterne s = em.find( ServiceInterne.class, id );
        return s;
    }

    // supprimer un service
    public void supprimer( ServiceInterne s ) {
        tx.begin();
        s = em.merge( s );
        em.remove( s );
        tx.commit();
    }

    // Lister tous les services
    public List<ServiceInterne> lister() {
        String sql = "select s from ServiceInterne s";
        Query q = em.createQuery( sql );
        List<ServiceInterne> data = q.getResultList();

        return data;
    }

    public List<Employe> listerEmploye(ServiceInterne s){
    	 String sql = "select e from Employe e where e.serviceInt=:service";
         Query q = em.createQuery( sql );
         q.setParameter("service",s );
         List<Employe> data = q.getResultList();

         return data;
    }
    
    // Lister les services selon leurs noms
    public List<ServiceInterne> select( String name ) {

        String sql = "select s from ServiceInterne s where s.nom =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", name );
        List<ServiceInterne> data = q.getResultList();

        return data;
    }
    
    
    public boolean findService(String name) {
		// TODO Auto-generated method stub
		boolean result = false;
		Long count;
		Query q = null;

		String sql = "select count(f.id) from ServiceInterne f where f.nom=:nom";

		q = em.createQuery(sql);
		q.setParameter("nom", name);
		count = (Long) q.getSingleResult();

	

		if (count.intValue() != 0) {
			result = true;

		}
		return result;
	}
}
