package com.pfe.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.primefaces.context.RequestContext;

import com.pfe.DAO.CourrierArriveDAO;
import com.pfe.DAO.HistoriqueDAO;
import com.pfe.DAO.EmployeDAO;
import com.pfe.DAO.FichierDAO;
import com.pfe.DAO.OpenCMIS;
import com.pfe.DAO.ParametreDAO;
import com.pfe.DAO.RestApi;
import com.pfe.DAO.ServiceInterneDAO;
import com.pfe.entities.CourrierArrive;
import com.pfe.entities.Employe;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.ServiceInterne;

@ManagedBean
@SessionScoped
public class NotificationMB {
private  OpenCMIS openCMIS ;
private List<Employe> employes = new ArrayList<Employe>();

    public List<Employe> getEmployes() {
	return employes;
}
public void setEmployes(List<Employe> employes) {
	this.employes = employes;
}

public String getCommentaires() {
	return commentaires;
}



private ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO();
	private Long           nbrCourriers = null;
    private Long           counts       = null;
    private String commentaires = null;
    private String radio = "personne";
    private List<ServiceInterne> servicesInternes = new ArrayList<ServiceInterne>();
private CourrierArriveDAO courrierArriveDAO = new CourrierArriveDAO();
    private List<Historique> historiques = new ArrayList<Historique>();
   private HistoriqueDAO historiqueDAO  = new HistoriqueDAO ();
    public List<Historique> getHistoriques() {
		return historiques;
	}
	public void setHistoriques(List<Historique> historiques) {
		this.historiques = historiques;
	}
	
	public List<ServiceInterne> getServicesInternes() {
		return servicesInternes;
	}
	public void setServicesInternes(List<ServiceInterne> servicesInternes) {
		this.servicesInternes = servicesInternes;
	}
	public String getRadio() {
		return radio;
	}
	public void setRadio(String radio) {
		this.radio = radio;
	}
	public String dlgNotUnlock() {
		return commentaires;
	}
	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

	private Long           previousNbrCourriers;
private Long nbrCourriersService=null;
    private boolean        loggedIn;
    @ManagedProperty(value = "#{connexion}")
	private Connexion cnx;
    
    public Connexion getCnx() {
		return cnx;
	}
	public void setCnx(Connexion cnx) {
		this.cnx = cnx;
	}

	private CourrierArrive courrierArrive;
    private Fichier fichier ;
  private FichierDAO  fichierDAO=new FichierDAO();
    EmployeDAO             employeDAO   = new EmployeDAO();
   ParametreDAO     parametreDAO  = new   ParametreDAO  ();
    CourrierArriveDAO      courrierDAO  = new CourrierArriveDAO();
    private Employe        employe      ;

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


    public String getTicket() throws UnsupportedOperationException,
	org.json.JSONException, IOException {


return restApi.getTicket();
}
   
    
  
   
    
 
	public String getLastUser() {
		if(courrierArrive==null){
			return null;
		}
	Employe emetteur = courrierArriveDAO.getLastUser(courrierArrive.getRef());
		return emetteur.getNom()+""+emetteur.getPrenom();
	}

	public void countCourrier() {

        

        Long tempCounts = courrierDAO.countCourrier( employe.getId() );

        nbrCourriers = tempCounts;

    }
	public void countCourrierService(){
		nbrCourriersService=courrierDAO.countCourrierService( employe.getServiceInt().getId() );
	}


   
@PostConstruct
    public void test() {
	employe=cnx.getEmploye();
	counts=courrierDAO.countCourrier( employe.getId() );
	 nbrCourriers = counts;
	initAlfresco();
	servicesInternes = serviceInterneDAO.lister();
	
   
     
               
    }




    public void execute() {

    	//
    	counts=courrierDAO.countCourrier( employe.getId() );
    	if(cnx.getEmploye().getId()==3){
    		
    		
    	}
  
        if ( cnx.getCounts()<cnx.getNbrCourriers())
        {
            
            
            courrierArrive = courrierDAO.getLastCourrier( employe.getId() );
          
            fichier = fichierDAO.getFichier(courrierArrive);
            // Employe emp = courrierDAO.getLastUser( courrier.getRef() );
            // courrier.setEmpDest( emp );
           
            RequestContext.getCurrentInstance().execute( "showDialog();"
                    );
             
     
        }
        cnx.resetCounts();
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
    
    public void initAlfresco(){
    	 
    		restApi = new RestApi(cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
    		 openCMIS = new OpenCMIS(cnx.getEmploye(),cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
    	}

    public void onServiceInterneChange() {
		employes = employeDAO.refresh(courrierArrive.getServiceIntDest());
	}
    
	public void traiter() throws org.json.JSONException, IOException {
		courrierArrive.setStatut("traité");
		Historique historique = new Historique();
		historique.setCourrierArr(courrierArrive);
		historique.setDateReception(courrierArriveDAO.getDate(courrierArrive
				.getRef()));
		historique.setStatut("traité");
		historique.setEmploye(cnx.getEmploye());
		historique.setCommentaires(commentaires);
		Date date = new Date();

		historique.setDateFinTraitement(date);
		historiqueDAO.ajouter(historique);

		courrierArriveDAO.modifier(courrierArrive);
		
	
		openCMIS.moveToArchive(courrierArrive, fichier);
		
		restApi.addComment(fichier, commentaires);
		commentaires = null;
	}
	public void unlock() {
		courrierArrive.setStatut("libéré");
		courrierArrive.setEmpDest(null);
		courrierArrive.setServiceIntDest(employe.getServiceInt());
		Historique historique = new Historique();
		historique.setCourrierArr(courrierArrive);
		historique.setEmploye(employe);
		historique.setStatut("libéré");
		historique.setDateReception(courrierArriveDAO.getDate(courrierArrive
				.getRef()));
		Date date = new Date();
		historique.setCommentaires(commentaires);
		historique.setDateFinTraitement(date);
		historiqueDAO.ajouter(historique);
		courrierArriveDAO.modifier(courrierArrive);

		openCMIS.moveFileToService(courrierArrive, fichier);
		commentaires = null;
	}
	
	
	
	public void sendEmail(String toOne, String subject, String body) {
		final String username = "no_reply.oratech";
		final String password = "mypworatech";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.mail.yahoo.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("no_reply.oratech@yahoo.com"));

			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toOne));

			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void send() {
		Historique historique = new Historique();
		historique.setStatut("envoye");
		Date date = new Date();
		Date dateReception = historiqueDAO.getDate(courrierArrive.getRef());
		historique.setDateFinTraitement(date);
		historique.setEmploye(cnx.getEmploye());
		historique.setCourrierArr(courrierArrive);
		historique.setDateReception(dateReception);
		historique.setCommentaires(commentaires);
		historiqueDAO.ajouter(historique);
		
	
		commentaires = null;

		if (courrierArrive.getEmpDest() != null) {
			sendEmail(courrierArrive.getEmpDest().getEmail(),
					"Nouveau courrier", "Vous avez reçu un nouveau courrier");
			openCMIS.moveFile(courrierArrive, fichier);
		} else {

			openCMIS.moveFileToService(courrierArrive, fichier);
		}
		
		courrierArrive.setStatut("envoye");
		courrierArriveDAO.modifier(courrierArrive);

		// RequestContext.getCurrentInstance().update( ":form:table" );
	}
	
	public void voirHistorique(ActionEvent event) {

		historiques = courrierArriveDAO.GetHistorique(courrierArrive.getRef());
		courrierArrive = historiques.get(0).getCourrierArr();
		
		for (Historique h : historiques) {
			
		}
    
    
    
    
    
}
}