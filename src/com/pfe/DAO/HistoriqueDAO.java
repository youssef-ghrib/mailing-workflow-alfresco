package com.pfe.DAO;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.CourrierArrive;
import com.pfe.entities.Employe;
import com.pfe.entities.Historique;

public class HistoriqueDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public HistoriqueDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    
    
    
    public void supprimer(Object o) {

		o = em.merge(o);
		em.remove(o);

	}
    public Date getDate( String ref ) {
   	 Date data=null;
       String sql = "select h.dateFinTraitement from Historique h where h.courrierArr.ref=:param order by h.dateFinTraitement DESC";
       Query q = em.createQuery( sql );
       q.setParameter( "param", ref );
       try{
        data = (Date) q.setMaxResults( 1 ).getSingleResult();

       }
       catch (NoResultException ex){
       	
       }
       return data;
   }

    // Ajouter un employé
    public void ajouter( Historique h ) {
        tx.begin();
        em.persist( h );
        tx.commit();
    }

    // Modifier un employé
    public void modifier( Historique h ) {
        tx.begin();
        em.merge( h );
        tx.commit();
    }

    // Lister les employés selon leurs statuts et services
    public List<CourrierArrive> getCourriers( Employe e,String statut ) {

        String sql = "select distinct( h.courrierArr ) from Historique h where h.employe=:param and h.courrierArr.statut=:statut ";
        Query q = em.createQuery( sql );
        q.setParameter( "param", e );
        q.setParameter( "statut", statut );
        List<CourrierArrive> data = q.getResultList();

        return data;
    }
    
    public List<Historique> getCourriersTraites( Employe e,String statut ) {

        String sql = "select h from Historique h where h.employe=:param and h.statut=:statut ";
        Query q = em.createQuery( sql );
        q.setParameter( "param", e );
        q.setParameter( "statut", statut );
        List<Historique> data = q.getResultList();

        return data;
    }
    public List<Historique>findHistorique(CourrierArrive courrier){
    	 String sql = "select h from Historique h where h.courrierArr=:courrier ";
         Query q = em.createQuery( sql );
         q.setParameter( "courrier", courrier);
        
         List<Historique> data = q.getResultList();

         return data;
    }
    
}
