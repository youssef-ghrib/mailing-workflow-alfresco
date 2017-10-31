package com.pfe.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.Etablissement;

public class EtablissementDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public EtablissementDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    

	public boolean findEtablissement(String name) {
		// TODO Auto-generated method stub
		boolean result = false;
		Long count;
		Query q = null;

		String sql = "select count(f.id) from Etablissement f where f.nom=:nom";

		q = em.createQuery(sql);
		q.setParameter("nom", name);
		count = (Long) q.getSingleResult();

	

		if (count.intValue() != 0) {
			result = true;

		}
		return result;
	}
    // Ajouter un établissement
    public void ajouter( Etablissement e ) {
        tx.begin();
        em.persist( e );
        tx.commit();
    }

    // Modifier un établissement
    public void modifier( Etablissement e ) {
        tx.begin();
        em.merge( e );
        tx.commit();
    }

    // Supprimer un établissement
    public void supprimer( Etablissement e ) {
        tx.begin();
        e = em.merge( e );
        em.remove( e );
        tx.commit();
    }

    // Consulter un établissement
    public Etablissement consulter( int id ) {

        Etablissement e = em.find( Etablissement.class, id );

        return e;
    }

    // Lister tous les établissements
    public List<Etablissement> lister() {

        String sql = "select e from Etablissement e";
        Query q = em.createQuery( sql );
        List<Etablissement> data = q.getResultList();

        return data;
    }

    // Lister les établissements selon leurs noms
    public List<Etablissement> select( String name ) {

        String sql = "select e from Etablissement e where e.nom =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", name );
        List<Etablissement> data = q.getResultList();

        return data;
    }
}
