package com.pfe.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;



import com.pfe.entities.Employe;
import com.pfe.entities.ServiceInterne;

public class EmployeDAO {
    private static final String        PERSISTENCE_UNIT_NAME = "pfe4";
    public static EntityManagerFactory factory;
    public static EntityManager        em;
    public static EntityTransaction    tx;

    public  EmployeDAO  () {
        if ( factory == null ) {
            factory = Persistence
                    .createEntityManagerFactory( PERSISTENCE_UNIT_NAME );
            em = factory.createEntityManager();
            tx = em.getTransaction();
        }
    }

    
    public Boolean findUserName(String userName) {

		boolean result = false;
		Long count;
		Query q = null;

		String sql = "select count(f.id) from Employe f where f.username=:username";

		q = em.createQuery(sql);
		q.setParameter("username", userName);
		count = (Long) q.getSingleResult();

	

		if (count.intValue() != 0) {
			result = true;

		}
		return result;
	}
    
    
    // Vérifier le nom d'utilisateur et le mot de passe
    public Employe verify( Employe user ) {

        Employe testUser;

        Query query = null;

        String sql = "select E from Employe E where E.username=:user and E.password=:pw";
        query = em.createQuery( sql );
        query.setParameter( "user", user.getUsername() );
        query.setParameter( "pw", user.getPassword() );

        try {

            testUser = (Employe) query.getSingleResult();

            

        }

        catch ( NoResultException e ) {
            
            return null;

        }

        
        return testUser;

    }

    public String getRole( String username ) {
        Query query = null;
        String sql = null;
        String role = null;

        sql = "select e.role from Employe e where e.username='" + username + "'";
        query = em.createQuery( sql );

        role = (String) query.getSingleResult();
        return role;
    }

    public Employe getFirstLogin( String username ) {
        String sql = "select e from Employe e where e.username='" + username + "'";
        Query query = em.createQuery( sql );

        Employe firstLogin = (Employe) query.getSingleResult();
        return firstLogin;
    }

    // Ajouter un employé
    public void ajouter( Employe e ) {
        tx.begin();
        em.persist( e );
        tx.commit();
    }

    // Modifier un employé
    public void modifier( Employe e ) {
        tx.begin();
        em.merge( e );
        tx.commit();
    }

    // Supprimer un employé
    public void supprimer( Employe e ) {
        tx.begin();
        e = em.merge( e );
        em.remove( e );
        tx.commit();
    }

    // Consulter un employé
    public Employe consulter( int id ) {

        Employe e = em.find( Employe.class, id );

        return e;
    }

    // Lister tous les employés
    public List<Employe> lister() {

        String sql = "select e from Employe e";
        Query q = em.createQuery( sql );
        List<Employe> data = q.getResultList();

        return data;
    }

    // Lister les employés selon leurs noms
    public List<Employe> select( String name ) {

        String sql = "select e from Employe e where e.nom =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", name );
        List<Employe> data = q.getResultList();

        return data;
    }

    public List<String> getEmails( ServiceInterne s ) {

        String sql = "select e.email from Employe e where e.serviceInt =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", s );
        List<String> data = q.getResultList();

        return data;
    }

    // Lister les personnes externes selon leur service
    public List<Employe> refresh( ServiceInterne s ) {

        String sql = "select e from Employe e where e.serviceInt =:param";
        Query q = em.createQuery( sql );
        q.setParameter( "param", s );
        List<Employe> data = q.getResultList();

        return data;
    }
}
