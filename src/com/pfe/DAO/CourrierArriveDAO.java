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
import com.pfe.entities.Historique;
import com.pfe.entities.ServiceExterne;
import com.pfe.entities.ServiceInterne;

public class CourrierArriveDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public CourrierArriveDAO() {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    // Ajouter un employé
    public void ajouter( CourrierArrive c ) {
    tx.begin();
    	
        em.persist( c );
     tx.commit();
    }

    public CourrierArrive getCourrier(String id){
    	CourrierArrive courrier =null;
         String sql = "select h from CourrierArrive h where h.ref=:param ";
         Query q = em.createQuery( sql );
         q.setParameter( "param", id );
         try{
        courrier=(CourrierArrive) q.getSingleResult();

         }
         catch (NoResultException ex){
         	
         }
       
    	
    	
    	return courrier;
    }
    public List<CourrierArrive> getCourrierTraite(Employe employe){
    	
    	
    	  String sql = "select h from Historique h where c.employe.id =:id and h.courrierArr.statut='traité' ";
          Query q = em.createQuery( sql );
          q.setParameter( "id", employe.getId() );
          List<CourrierArrive> data=null;
          try{
        data = q.getResultList();
          }
        catch(NoResultException e){
        	
        }

          return data;
    	
    }
    // Lister les employés selon leurs statuts et services
    public List<CourrierArrive> select( ServiceInterne s ) {

        String sql = "select c from CourrierArrive c where c.serviceIntDest =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", s );
        List<CourrierArrive> data = q.getResultList();

        return data;
    }
    
    
    public void supprimer(Object o) {

		o = em.merge(o);
		em.remove(o);

	}

    // Lister les employés selon leurs statuts et services
    public List<CourrierArrive> lister( Employe e ) {

        String sql = "select c from CourrierArrive c where c.empDest =:param and c.statut!='traité'";
        Query q = em.createQuery( sql );
        q.setParameter( "param", e );
        List<CourrierArrive> data = q.getResultList();

        return data;
    }

    // Lister les employés selon leurs statuts et services
    public List<CourrierArrive> list( String statut,Employe e ) {

        String sql = "select c from CourrierArrive c where c.statut =:statut and c.empDest =:emp";
        Query q = em.createQuery( sql );
        q.setParameter( "statut", statut );
        q.setParameter( "emp", e );
        List<CourrierArrive> data = q.getResultList();

        return data;
    }

    
    public List<CourrierArrive> listerCourriersNonEnvoyes( Employe e ) {

        String sql = "select h.courrierArr from Historique h where h.employe =:emp and h.courrierArr.statut='non envoye' ";
        Query q = em.createQuery( sql );
       
        q.setParameter( "emp", e );
        List<CourrierArrive> data = q.getResultList();

        return data;
    }
    // Modifier un employé
    public void modifier( CourrierArrive c ) {
        tx.begin();
        em.merge( c );
        tx.commit();
    }

    // Lister les employés selon leurs statuts et services
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

    
public String generateName(Date date){
		
		
		
		Long count;
		String sql ="select count(c.ref) from CourrierArrive c where c.dateReception=:date ";
	Query query =em.createQuery(sql);
	query.setParameter("date", date);
	
	count = (Long) query.getSingleResult();
	
	Format formatter = new SimpleDateFormat("yyyy-MM-dd");
	String formattedDate = formatter.format(date);
count=count+1;

	return  (formattedDate+"-"+count );
		
	}
    // Lister les employés selon leurs statuts et services
    public Employe getABO( String ref ) {

        String sql = "select h.employe from Historique h where h.courrierArr.ref=:param order by h.dateReception";
        Query q = em.createQuery( sql );
        q.setParameter( "param", ref );
        Employe data = (Employe) q.setMaxResults( 1 ).getSingleResult();

        return data;
    }

    // retourner tous les etablissements
    public List<Etablissement> getEtablissement() {

        String sql = "select f from  Etablissement f ";
        Query query = em.createQuery( sql );

        List<Etablissement> etablissements = query.getResultList();

        return etablissements;

    }

    // retourner les services d'un etablissement
    public List<ServiceExterne> getServiceExterne( int etablissementId ) {

        String sql = "select f from  ServiceExterne f where f.etablissement.id=:id";

        Query query = em.createQuery( sql );
        query.setParameter( "id", etablissementId );
        List<ServiceExterne> services = query.getResultList();

        return services;
    }

    public List<CourrierArrive> getCourriers( String priorite, Employe user, Date dateEnvoi, Date maxDate,
            int etablissement, int service, Object recherche, String type, String espace ) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametres = new HashMap<String, Object>();

        sql.append( "select c from CourrierArrive c where c.priorite =:pr and c.statut!='traité' " );

        if ( recherche != null && !recherche.equals( "" ) ) {
            if ( type.equals( "Mot cle" ) ) {
                List<String> tags = (List<String>) recherche;
                int i = 0;

                for ( String tag : tags ) {

                    sql.append( " and c.tags like :tag" + i + " " );
                    parametres.put( "tag" + i, "%" + tag + "%" );
                    i++;
                }

            }
        
           
            	
            	if(type.equals( "Reference" )){
            		 sql.append( " and c.ref=:ref " );
                     parametres.put( "ref", recherche );
            	}
            
            	if(type.equals( "Objet" )){
                sql.append( " and c.objet=:obj " );
                parametres.put( "obj", recherche );
            
        
                                               }
      
        }

        /*
         * if(userId!=0){ sql.append(" empSource.id:id");
         * parametres.put("id",userId); }
         */

        if ( dateEnvoi == null ) {
            if ( maxDate != null ) {
                sql.append( " and c.dateReception < :maxdate " );
                parametres.put( "maxdate", maxDate );

            }

        }
        else {
            if ( maxDate != null ) {
                sql.append( " and c.dateReception < :maxdate and c.dateReception > :dateReception " );
                parametres.put( "maxdate", maxDate );
                parametres.put( "dateReception", dateEnvoi );
            }
            else {
                sql.append( " and c.dateReception > :dateEnvoi" );
                parametres.put( "dateEnvoi", dateEnvoi );
            }

        }
        // tous les etablissements
        if ( etablissement != 0 )
        {
            // tous les services
            if ( service == 0 ) {
                sql.append( " and c.etablissementSource.id =:etabId " );
                parametres.put( "etabId", etablissement );
            }
            else {
                sql.append( " and c.etablissementSource.id =:etabId and c.serviceExtSource.id =:servId" );
                parametres.put( "servId", service );
                parametres.put( "etabId", etablissement );

            }
        }

        if ( espace.equals( "Boite de reception" ) ) {
            sql.append( "and c.empDest.id=:userId" );
            parametres.put( "userId", user.getId() );
        }
        else {
            sql.append( "and c.serviceIntDest.id=:userService" );
            parametres.put( "userService", user.getServiceInt().getId() );
        }

      
        
        String finalsql = sql.toString();
        

        Query query =
                em.createQuery( finalsql );

        query.setParameter( "pr", priorite );

        for ( Map.Entry<String, Object> entry : parametres.entrySet() ) {
            
            query.setParameter( entry.getKey(), entry.getValue() );
        }

        return query.getResultList();
    }

    public List<CourrierArrive> getCourriersAdmin(  Date dateEnvoi, Date maxDate, int etablissement,
            int service, Object recherche, String type, String priorite ) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametres = new HashMap<String, Object>();

        sql.append( "select distinct (h.courrierArr)   from Historique h  where h.courrierArr.priorite=:pr " );

        if ( recherche != null && !recherche.equals( "" ) ) {
            if ( type.equals( "Mot cle" ) ) {
                List<String> tags = (List<String>) recherche;
                int i = 0;

                for ( String tag : tags ) {

                    sql.append( " and  h.courrierArr.tags like :tag" + i + " " );
                    parametres.put( "tag" + i, "%" + tag + "%" );
                    i++;
                }

            }
        
           
            	
            	if(type.equals( "Reference" )){
            		 sql.append( " and h.courrierArr.ref=:ref " );
                     parametres.put( "ref", recherche );
            	}
            
            	if(type.equals( "Objet" )){
                sql.append( " and  h.courrierArr.objet=:obj " );
                parametres.put( "obj", recherche );
            
        
                                               }
      
        }

        
        
        /*
         * if(userId!=0){ sql.append(" empSource.id:id");
         * parametres.put("id",userId); }
         */

        if ( dateEnvoi == null ) {
            if ( maxDate != null ) {
                sql.append( " and h.dateReception < :maxdate " );
                parametres.put( "maxdate", maxDate );

            }

        }
        else {
            if ( maxDate != null ) {
                sql.append( " and h.dateReception < :maxdate and h.dateReception > :dateEnvoi" );
                parametres.put( "maxdate", maxDate );
                parametres.put( "dateEnvoi", dateEnvoi );
            }
            else {
                sql.append( " and h.dateReception > :dateEnvoi" );
                parametres.put( "dateEnvoi", dateEnvoi );
            }

        }
        // tous les etablissements
        if ( etablissement != 0 )
        {
            // tous les services
            if ( service == 0 ) {
                sql.append( " and h.courrierArr.etablissementSource.id =:etabId " );
                parametres.put( "etabId", etablissement );
            }
            else {
                sql.append( " and h.courrierArr.etablissementSource.id =:etabId and h.courrierArr.serviceExtSource.id =:servId" );
                parametres.put( "servId", service );
                parametres.put( "etabId", etablissement );

            }
        }

        String finalsql = sql.toString();
        

        Query query =
                em.createQuery( finalsql );

        query.setParameter( "pr", priorite );

        for ( Map.Entry<String, Object> entry : parametres.entrySet() ) {
            
            query.setParameter( entry.getKey(), entry.getValue() );
        }

        List<CourrierArrive> lists = query.getResultList();
        

      
        return lists;

    }

    public List<CourrierArrive> getCourriersEmploye( Employe emp, Date dateEnvoi, Date maxDate, int etablissement,
            int service, Object recherche, String type, String priorite ) {

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parametres = new HashMap<String, Object>();

        sql.append( "select distinct (h.courrierArr)  from Historique h  where h.courrierArr.priorite=:pr and h.statut='traité'" );

      
        if ( recherche != null && !recherche.equals( "" ) ) {
            if ( type.equals( "Mot cle" ) ) {
                List<String> tags = (List<String>) recherche;
                int i = 0;

                for ( String tag : tags ) {

                    sql.append( " and  h.courrierArr.tags like :tag" + i + " " );
                    parametres.put( "tag" + i, "%" + tag + "%" );
                    i++;
                }

            }
        
           
            	
            	if(type.equals( "Reference" )){
            		 sql.append( " and h.courrierArr.ref=:ref " );
                     parametres.put( "ref", recherche );
            	}
            
            	if(type.equals( "Objet" )){
                sql.append( " and  h.courrierArr.objet=:obj " );
                parametres.put( "obj", recherche );
            
        
                                               }
      
        }

        /*
         * if(userId!=0){ sql.append(" empSource.id:id");
         * parametres.put("id",userId); }
         */

        if ( dateEnvoi == null ) {
            if ( maxDate != null ) {
                sql.append( " and h.dateReception < :maxdate " );
                parametres.put( "maxdate", maxDate );

            }

        }
        else {
            if ( maxDate != null ) {
                sql.append( " and h.dateReception < :maxdate and h.dateReception > :dateEnvoi" );
                parametres.put( "maxdate", maxDate );
                parametres.put( "dateEnvoi", dateEnvoi );
            }
            else {
                sql.append( " and h.dateReception > :dateEnvoi" );
                parametres.put( "dateEnvoi", dateEnvoi );
            }

        }
        // tous les etablissements
        if ( etablissement != 0 )
        {
            // tous les services
            if ( service == 0 ) {
                sql.append( " and h.courrierArr.etablissementSource.id =:etabId " );
                parametres.put( "etabId", etablissement );
            }
            else {
                sql.append( " and h.courrierArr.etablissementSource.id =:etabId and h.courrierArr.serviceExtSource.id =:servId" );
                parametres.put( "servId", service );
                parametres.put( "etabId", etablissement );

            }
        }
        
        sql.append(" and h.employe=:emp");
        parametres.put("emp", emp);

        String finalsql = sql.toString();
        

        Query query =
                em.createQuery( finalsql );

        query.setParameter( "pr", priorite );

        for ( Map.Entry<String, Object> entry : parametres.entrySet() ) {
            
            query.setParameter( entry.getKey(), entry.getValue() );
        }

        List<CourrierArrive> lists = query.getResultList();
        

      
        return lists;

    }
    public List<Historique> GetHistorique( String id) {

        String sql = "select h from  Historique h where h.courrierArr.ref=:id ORDER BY h.dateReception";

        Query query = em.createQuery( sql );
        query.setParameter( "id", id );

        return query.getResultList();
    }

    public HashSet<String> getTags( String word ) {
        HashSet<String> tags = new HashSet<String>();

        String sql = "select c.tags from CourrierArrive c where c.tags like :tag";

        Query query = em.createQuery( sql );
        query.setParameter( "tag", "%" + word + "%" );
        List<String> results = query.getResultList();
        /*
         * String[] sqlTAgs = new String[results.size()]; sqlTAgs =
         * results.toArray(sqlTAgs);
         */

        for ( String value : results ) {
            String[] tagTable = value.split( " " );

            for ( String tag : tagTable ) {

                if ( tag.matches( ".*" + word + ".*" ) ) {
                    tags.add( tag );
                }

            }
            for ( String name : tags ) {
                
            }
        }
        return tags;
    }

    public Date getCreationDate( String ref ) {
        String sql = "select h.dateReception from Historique h where h.courrierArr.ref=:param order by h.id ";
        Query q = em.createQuery( sql );
        q.setParameter( "param", ref );
        Date data = (Date) q.setMaxResults( 1 ).getSingleResult();
        return data;
    }

    public Employe getFirstUser( String ref ) {
        String sql = "select h.employe from Historique h where h.courrierArr.ref=:param order by h.id ";
        Query q = em.createQuery( sql );
        q.setParameter( "param", ref );
        Employe employe = (Employe) q.setMaxResults( 1 ).getSingleResult();
        return employe;
    }

    public Employe getLastUser( String ref ) {

        String sql = "select h.employe from Historique h where h.courrierArr.ref=:param order by h.id DESC";
        Query q = em.createQuery( sql );
        q.setParameter( "param", ref );
        Employe employe = null;
        try {
       employe = (Employe) q.setMaxResults( 1 ).getSingleResult();
      }
      catch(NoResultException ex){
    	  
      }
        return employe;
    }

    public List<String> getObjet( String text ,Employe emp,String espace) {
        String sql = null;
        Query query=null;
        if(espace.equals("Service")){
            	sql="select c.objet from  CourrierArrive  c  where c.objet like :query  and c.serviceIntDest=:service";
            	  query = em.createQuery( sql );
                  query.setParameter( "query", "%" + text + "%" );
            	query.setParameter( "service", emp.getServiceInt() );
            }
        if(espace.equals("Boite de reception")){
            	sql="select c.objet from  CourrierArrive  c  where c.objet like :query  and c.empDest=:emp";
          	  query = em.createQuery( sql );
                query.setParameter( "query", "%" + text + "%" );
          	query.setParameter( "emp", emp );
         
        }
        return query.getResultList();

    }
    
    
    public List<String> getObjet( String text) {
        String sql = null;
        Query query=null;
      
            	sql="select c.objet from  CourrierArrive  c  where c.objet like :query  ";
            	  query = em.createQuery( sql );
                  query.setParameter( "query", "%" + text + "%" );
            
            
      
        return query.getResultList();

    }
    public List<String> getRef( String text ,Employe emp,String espace) {
        String sql = null;
        Query query=null;
        if(espace.equals("Service")){
            	sql="select c.ref from  CourrierArrive  c  where c.ref like :query  and c.serviceIntDest=:service";
            	  query = em.createQuery( sql );
                  query.setParameter( "query",  text + "%" );
            	query.setParameter( "service", emp.getServiceInt() );
            }
        if(espace.equals("Boite de reception")){
            	sql="select c.ref from  CourrierArrive  c  where c.ref like :query  and c.empDest=:emp";
          	  query = em.createQuery( sql );
                query.setParameter( "query", text + "%" );
          	query.setParameter( "emp", emp );
         
        }
        return query.getResultList();

    }
    
    
    public List<String> getRef( String text) {
        String sql = null;
        Query query=null;
       
     
            	sql="select c.ref from  CourrierArrive  c  where c.ref like :query ";
          	  query = em.createQuery( sql );
                query.setParameter( "query", text + "%" );
        
         
      
        return query.getResultList();

    }
    
    

    public Long countCourrier( int userId ) {

        String sql = "select count( distinct c.ref) from  CourrierArrive c where c.empDest.id=:id and c.statut!='traité' ";

        Query query = em.createQuery( sql );
        query.setParameter( "id", userId );
Long count =null;
try{
count=(Long) query.getSingleResult();
}
catch (NoResultException ex){
	
}
return count;
    }
    
    public Long countCourrierService( int userId ) {

        String sql = "select count(distinct c.ref) from  CourrierArrive c where c.serviceIntDest.id=:id  ";

        Query query = em.createQuery( sql );
        query.setParameter( "id", userId );


        Long count =null;
        try{
        count=(Long) query.getSingleResult();
        }
        catch (NoResultException ex){
        	
        }
        return count;

    }
    public CourrierArrive getLastCourrier( int userId ) {

        String sql = "select c from CourrierArrive c  where c.empDest.id=:id order by substring(c.ref, 12) DESC";
        Query q = em.createQuery( sql );
        q.setParameter( "id", userId );
        CourrierArrive courrier = (CourrierArrive) q.setMaxResults( 1 ).getSingleResult();
        return courrier;
    }
}
