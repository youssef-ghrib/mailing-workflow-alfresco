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

public class CourrierDepartDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public CourrierDepartDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }
    
    
    
    public  List<CourrierDepart>  getCourriersDepart(){
    	   String sql = "select f from  CourrierDepart f ";
           Query query = em.createQuery( sql );

           List<CourrierDepart> courriers = query.getResultList();

           return courriers;

    	
    }
    
public String generateName(Date date){
		
		
		
		Long count;
		String sql ="select count(c.ref) from CourrierDepart c where c.dateEnvoi=:date ";
	Query query =em.createQuery(sql);
	query.setParameter("date", date);
	
	count = (Long) query.getSingleResult();
	
	Format formatter = new SimpleDateFormat("yyyy-MM-dd");
	String formattedDate = formatter.format(date);
count=count+1;

	return  (formattedDate+"-"+count );
		
	}
    
    public List<Etablissement> getEtablissement() {

        String sql = "select f from  Etablissement f ";
        Query query = em.createQuery( sql );

        List<Etablissement> etablissements = query.getResultList();

        return etablissements;

    }
    
    public List<ServiceExterne> getServiceExterne( int etablissementId ) {

        String sql = "select f from  ServiceExterne f where f.etablissement.id=:id";

        Query query = em.createQuery( sql );
        query.setParameter( "id", etablissementId );
        List<ServiceExterne> services = query.getResultList();

        return services;
    }
    public void ajouter( CourrierDepart courrier) {
        tx.begin();
        em.persist(courrier );
        tx.commit();
    }
    
    public void modifier( CourrierDepart courrier ) {
        tx.begin();
        em.merge( courrier);
        tx.commit();
    }
    
   
}