package com.pfe.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.PersonneExterne;
import com.pfe.entities.ServiceExterne;

public class PersonneExterneDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public PersonneExterneDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    
    public boolean findPersonne(PersonneExterne persone) {
		// TODO Auto-generated method stub
		boolean result = false;
		Long count;
		Query q = null;

		String sql = "select count(f.id) from PersonneExterne f where f.nom=:nom and f.prenom=:prenom";

		q = em.createQuery(sql);
		q.setParameter("nom", persone.getNom());
		q.setParameter("prenom", persone.getPrenom());
		count = (Long) q.getSingleResult();

	

		if (count.intValue() != 0) {
			result = true;

		}
		return result;
	}
    
    // Ajouter une personne externe
    public void ajouter( PersonneExterne p ) {
        tx.begin();
        em.persist( p );
        tx.commit();
    }

    // Modifier une personne externe
    public void modifier( PersonneExterne p ) {
        tx.begin();
        em.merge( p );
        tx.commit();
    }

    // Consulter une personne externe
    public PersonneExterne consulter( int id ) {
        PersonneExterne p = em.find( PersonneExterne.class, id );
        return p;
    }

    // supprimer une personne externe
    public void supprimer( PersonneExterne p ) {
        tx.begin();
        p = em.merge( p );
        em.remove( p );
        tx.commit();
    }

    // Lister toutes les personnes externes
    public List<PersonneExterne> lister() {
        String sql = "select p from PersonneExterne p";
        Query q = em.createQuery( sql );
        List<PersonneExterne> data = q.getResultList();

        return data;
    }

    // Lister les personnes externes selon leurs noms
    public List<PersonneExterne> select( String name ) {

        String sql = "select p from PersonneExterne p where p.nom =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", name );
        List<PersonneExterne> data = q.getResultList();

        return data;
    }

    // Lister les personnes externes selon leur service
    public List<PersonneExterne> refresh( ServiceExterne s ) {

        String sql = "select p from PersonneExterne p where p.serviceExt =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", s );
        List<PersonneExterne> data = q.getResultList();

        return data;
    }
}
