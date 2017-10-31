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
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.pfe.entities.CourrierArrive;
import com.pfe.entities.Employe;
import com.pfe.entities.Etablissement;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.Parametre;
import com.pfe.entities.ServiceExterne;
import com.pfe.entities.ServiceInterne;

public class ParametreDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public ParametreDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }
    
    
    public void modifier( Parametre parametre ) {
        tx.begin();
        em.merge( parametre );
        tx.commit();
    }
    public HashMap getParametres(){
    	HashMap<String, String> map = new HashMap<String, String>();
    	List<Parametre> parametres=null;
  	  String sql = "select p from Parametre p ";
        Query q = em.createQuery( sql );
       
      
        try{
        	parametres = q.getResultList();
        }
      catch(NoResultException e){
      	
      }

        
        for(Parametre parametre:parametres){
        	map.put(parametre.getNom(), parametre.getValeur());
        }
        return map;
  	
   
    }



}