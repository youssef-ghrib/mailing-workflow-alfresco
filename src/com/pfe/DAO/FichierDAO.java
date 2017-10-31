package com.pfe.DAO;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.CourrierArrive;
import com.pfe.entities.CourrierDepart;
import com.pfe.entities.Employe;
import com.pfe.entities.Etablissement;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.ServiceExterne;
import com.pfe.entities.ServiceInterne;

public class FichierDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public FichierDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }
    
    public void ajouter( Fichier fichier) {
        tx.begin();
        em.persist(fichier );
        tx.commit();
    }
    public void supprimer(Object o) {

		o = em.merge(o);
		em.remove(o);

	}
    
    public void modifier( Fichier fichier ) {
        tx.begin();
        em.merge(fichier);
        tx.commit();
    }
    
    public Fichier getFichier(CourrierArrive courrier){

        String sql = "select f from Fichier f where f.courrierArrive.ref=:ref";
        Query q = em.createQuery( sql );
        q.setParameter( "ref", courrier.getRef() );
        Fichier data = (Fichier) q.getSingleResult();
    	
    	
    	
    	return data;
    	
    }
    public Fichier getFichier(CourrierDepart courrier){

        String sql = "select f from Fichier f where f.courrierDepart.ref=:ref";
        Query q = em.createQuery( sql );
        q.setParameter( "ref", courrier.getRef() );
        Fichier data = (Fichier) q.getSingleResult();
    	
    	
    	
    	return data;
    	
    }
    



}