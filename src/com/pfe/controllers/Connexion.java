package com.pfe.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.pfe.DAO.CourrierArriveDAO;
import com.pfe.DAO.EmployeDAO;
import com.pfe.DAO.FichierDAO;
import com.pfe.DAO.Language;
import com.pfe.DAO.ParametreDAO;
import com.pfe.DAO.RestApi;
import com.pfe.entities.CourrierArrive;
import com.pfe.entities.Employe;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.ServiceInterne;


@ManagedBean
@SessionScoped
public class Connexion {

    private Long           nbrCourriers = new Long(0);
    private Long           counts       = new Long(0);
    private String commentaires = null;
  
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot()
			.getLocale();
    

	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Long getCounts() {
		return counts;
	}
	public void setCounts(Long counts) {
		this.counts = counts;
	}
	public String getCommentaires() {
		return commentaires;
	}
	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

	private Long           previousNbrCourriers;
private Long nbrCourriersService=null;
    private boolean        loggedIn;
  private String alfrescoServer;
private String alfrescoPort;
    private CourrierArrive courrierArrive;
    private Fichier fichier ;
  private FichierDAO  fichierDAO=new FichierDAO();
    EmployeDAO             employeDAO   = new EmployeDAO();
   ParametreDAO     parametreDAO  = new   ParametreDAO  ();
    CourrierArriveDAO      courrierDAO  = new CourrierArriveDAO();
    private Employe        employe      = new Employe();

    private RestApi restApi;
    
    
    public Fichier getFichier() {
		return fichier;
	}
	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}
	public Long getNbrCourriersService() {
		return nbrCourriersService;
	}
	public void setNbrCourriersService(Long nbrCourriersService) {
		this.nbrCourriersService = nbrCourriersService;
	}
	public String logout() {
        loggedIn = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }
    public void initParametre() {
		HashMap<String, String> map = parametreDAO.getParametres();
		alfrescoServer = map.get("alfrescoServer");
		alfrescoPort = map.get("alfrescoPort");
		
	}

    public String getTicket() throws UnsupportedOperationException,
	org.json.JSONException, IOException {


return restApi.getTicket();
}
    public String login() {
        Employe validuser = null;
        
        
        validuser = employeDAO.verify( employe );
        

        if ( validuser == null ) {
            
        	FacesContext context = FacesContext.getCurrentInstance();
    		context.addMessage("test", new FacesMessage(FacesMessage.SEVERITY_INFO,
    				Language.message("error_connect"), null));
            return null;
        }
        else {
            employe = validuser;
            countCourrier();
            countCourrierService();
            initParametre();
            locale = new Locale(employe.getLanguage());
			Language.setLanguage(employe.getLanguage());
            loggedIn = true;
            restApi = new RestApi(alfrescoServer,alfrescoPort);
        

            if ( employeDAO.getFirstLogin( employe.getUsername() ).isFirstLogin() )
                return "/firstLogin";
            else
            {
                counts = nbrCourriers= courrierDAO.countCourrier( employe.getId() );
                return "/restreint/CourrierArrive/boiteReception?faces-redirect=true";
            }
        }
    }
public  void resetCounts(){
	 counts=nbrCourriers;
 }
    
    @PreDestroy
    void onDestroy() {
        
    }

    void onremove() {
        
    }

    
    public String getAlfrescoServer() {
		return alfrescoServer;
	}

	public void setAlfrescoServer(String alfrescoServer) {
		this.alfrescoServer = alfrescoServer;
	}

	public String getAlfrescoPort() {
		return alfrescoPort;
	}

	public void setAlfrescoPort(String alfrescoPort) {
		this.alfrescoPort = alfrescoPort;
	}

	public void countCourrier() {

       // 

        Long tempCounts = courrierDAO.countCourrier( employe.getId() );

        nbrCourriers = tempCounts;

    }
	public void countCourrierService(){
		nbrCourriersService=courrierDAO.countCourrierService( employe.getServiceInt().getId() );
	}

    public void remote() {
        
    }

    public void change() {
        courrierArrive = new CourrierArrive();
        courrierArrive.setObjet( "dfsdsf" );
    }

    public void changee() {
        courrierArrive = new CourrierArrive();
        courrierArrive.setObjet( "zamzoum" );
    }

    public void test() {
     courrierArrive=courrierDAO.getCourrier("2015-06-03-3");
   fichier=fichierDAO.getFichier(courrierArrive);
     
               
    }

    public void execute() {

    	
        if ( counts < nbrCourriers )
        {
            
            courrierArrive = courrierDAO.getLastCourrier( employe.getId() );
            fichier = fichierDAO.getFichier(courrierArrive);
            // Employe emp = courrierDAO.getLastUser( courrier.getRef() );
            // courrier.setEmpDest( emp );
            RequestContext.getCurrentInstance().execute( "showDialog();"
                    );
            counts = nbrCourriers;
        }
    }

    public Long getNbrCourriers() {
        return nbrCourriers;
    }
    
 
    public void setNbrCourriers( Long nbrCourriers ) {
        this.nbrCourriers = nbrCourriers;
    }

    public Long getPreviousNbrCourriers() {
        return previousNbrCourriers;
    }

    public void setPreviousNbrCourriers( Long previousNbrCourriers ) {
        this.previousNbrCourriers = previousNbrCourriers;
    }

    public CourrierArrive getCourrierArrive() {
        return courrierArrive;
    }

    public void setCourrierArrive( CourrierArrive courrier ) {
        this.courrierArrive = courrier;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye( Employe employe ) {
        this.employe = employe;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn( boolean loggedIn ) {
        this.loggedIn = loggedIn;
    }
    

    
    
    
    
    
}
