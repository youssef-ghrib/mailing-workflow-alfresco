package com.pfe.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.Etablissement;
import com.pfe.entities.ServiceExterne;

public class ServiceExterneDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public ServiceExterneDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    // Ajouter un service externe
    public void ajouter( ServiceExterne s ) {
        tx.begin();
        em.persist( s );
        tx.commit();
    }

    // Modifier un service externe
    public void modifier( ServiceExterne s ) {
        tx.begin();
        em.merge( s );
        tx.commit();
    }

    // Consulter un service externe
    public ServiceExterne consulter( int id ) {
        ServiceExterne s = em.find( ServiceExterne.class, id );
        return s;
    }

    // supprimer un service externe
    public void supprimer( ServiceExterne s ) {
        tx.begin();
        s = em.merge( s );
        em.remove( s );
        tx.commit();
    }

    // Lister tous les services externes
    public List<ServiceExterne> lister() {
        String sql = "select s from ServiceExterne s";
        Query q = em.createQuery( sql );
        List<ServiceExterne> data = q.getResultList();

        return data;
    }

    // Lister les services externes selon leurs noms
    public List<ServiceExterne> select( String name ) {

        String sql = "select s from ServiceExterne s where s.nom =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", name );
        List<ServiceExterne> data = q.getResultList();

        return data;
    }

    // Lister les services externes selon leur établissement
    public List<ServiceExterne> refresh( Etablissement e ) {

        String sql = "select s from ServiceExterne s where s.etablissement =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", e );
        List<ServiceExterne> data = q.getResultList();

        return data;
    }

	public boolean findService(String name) {
		// TODO Auto-generated method stub
		boolean result = false;
		Long count;
		Query q = null;

		String sql = "select count(f.id) from ServiceExterne f where f.nom=:nom";

		q = em.createQuery(sql);
		q.setParameter("nom", name);
		count = (Long) q.getSingleResult();

	

		if (count.intValue() != 0) {
			result = true;

		}
		return result;
	}
}
