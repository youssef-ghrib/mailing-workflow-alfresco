package com.pfe.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONException;
import org.primefaces.model.UploadedFile;

import com.pfe.DAO.CourrierArriveDAO;
import com.pfe.DAO.CourrierDepartDAO;
import com.pfe.DAO.EmployeDAO;
import com.pfe.DAO.EtablissementDAO;
import com.pfe.DAO.FichierDAO;
import com.pfe.DAO.HistoriqueDAO;
import com.pfe.DAO.Language;
import com.pfe.DAO.OpenCMIS;
import com.pfe.DAO.PersonneExterneDAO;
import com.pfe.DAO.RestApi;
import com.pfe.DAO.ServiceExterneDAO;
import com.pfe.DAO.ServiceInterneDAO;
import com.pfe.entities.CourrierArrive;
import com.pfe.entities.CourrierDepart;
import com.pfe.entities.Employe;
import com.pfe.entities.Etablissement;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.PersonneExterne;
import com.pfe.entities.ServiceExterne;
import com.pfe.entities.ServiceInterne;

@ManagedBean(name = "courrierDepartMB")
@ViewScoped
public class CourrierDepartMB {

	@ManagedProperty(value = "#{connexion}")
	private Connexion cnx;
	private InputStream file;

	private Date date1;
	private Date date2;
	
	private String commentaires = null;
	private String text = null;
	private int selectedEtablissement = 0;
	private int selectedService = 0;
	private Fichier fichier;
	
	private CourrierDepart courrierDepart= new CourrierDepart();
	
	public CourrierDepart getCourrierDepart() {
		return courrierDepart;
	}







	public void setCourrierDepart(CourrierDepart courrierDepart) {
		this.courrierDepart = courrierDepart;
	}

	private RestApi restApi;
	private OpenCMIS openCMIS ;
	private List<CourrierDepart> courriersDepart ;
	
	private List<Etablissement> etablissements = new ArrayList<Etablissement>();
	private List<ServiceExterne> servicesExternes = new ArrayList<ServiceExterne>();
	private List<PersonneExterne> personnesExternes = new ArrayList<PersonneExterne>();
	
	private List<Employe> employes = new ArrayList<Employe>();
	
	private List<String> selectedTags = new ArrayList<String>();
	
	




	public List<CourrierDepart> getCourriersDepart() {
		return courriersDepart;
	}







	public void setCourriersDepart(List<CourrierDepart> courriersDepart) {
		this.courriersDepart = courriersDepart;
	}

	FichierDAO fichierDAO = new FichierDAO();
	HistoriqueDAO historiqueDAO = new HistoriqueDAO();
	CourrierDepartDAO courrierDepartDAO = new CourrierDepartDAO();
	EtablissementDAO etablissementDAO = new EtablissementDAO();
	ServiceExterneDAO serviceExterneDAO = new ServiceExterneDAO();
	PersonneExterneDAO personneExterneDAO = new PersonneExterneDAO();
	ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO();
	EmployeDAO employeDAO = new EmployeDAO();

public void initCourriersDepart(){
	courriersDepart=courrierDepartDAO.getCourriersDepart();
}
public void initAlfresco(){
 
	restApi = new RestApi(cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
	 openCMIS = new OpenCMIS(cnx.getEmploye(),cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
}



	
	 


	public Fichier getFichier() {
		return fichier;
	}

	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}

	public void initEtablissements() {
		 etablissements = etablissementDAO.lister();
	}

	// intialiser les services suivant l'etablissement selectionné
	public void initService(AjaxBehaviorEvent e) {
		servicesExternes = courrierDepartDAO
				.getServiceExterne(selectedEtablissement);
	}






	

	public void onEtablissementChange() {
		if(courrierDepart.getEtablissementDest()==null){
			
		} else  {
			
		}
		servicesExternes = serviceExterneDAO.refresh(courrierDepart
				.getEtablissementDest());
		
	}

	public void onServiceExterneChange() {
		personnesExternes = personneExterneDAO.refresh(courrierDepart
				.getServiceExtDest());
	}


	public void getAttribute(ActionEvent event)
			throws UnsupportedOperationException, org.json.JSONException,
			IOException {

		courrierDepart = (CourrierDepart) event.getComponent().getAttributes()
				.get("selectedItem");
		
		fichier = fichierDAO.getFichier(courrierDepart);
		
	
		
		

	}

	public String getTicket() throws UnsupportedOperationException,
			org.json.JSONException, IOException {
	

		return restApi.getTicket();
	}


	



	

	
	
	
	

	public String add() throws IOException, JSONException, org.json.JSONException {
		if(file==null){
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Error!",
					Language.message("uploader_fichier"));
			FacesContext context = FacesContext.getCurrentInstance();
		

				context.addMessage("form:fichier", message);
				
		
				RequestContext.getCurrentInstance().update("@form");
				//RequestContext.getCurrentInstance().update(":form:msgFichier");
		}
		else
		{
			
		
		
		Date date = new Date();
		courrierDepart.setDateEnvoi(date);
		
		courrierDepart.setRef(courrierDepartDAO.generateName(courrierDepart.getDateEnvoi()
				));

		courrierDepartDAO.ajouter(courrierDepart);

		Fichier tempFichier = openCMIS.uploadCourrierDepart(courrierDepart, file);

		tempFichier.setCourrierDepart(courrierDepart);
		fichierDAO.ajouter(tempFichier);
		if(!courrierDepart.getTags().equals("")){
		restApi.addTag(tempFichier, courrierDepart.getTags());
		}
		return("/restreint/CourrierDepart/listerCourriersDeparts?faces-redirect=true");
		}
		return null;
		
	}

	

	public void upload(FileUploadEvent event) throws IOException, JSONException {
		file = event.getFile().getInputstream();
		
		// courrierArrive.set
		// openCMIS.moveFile( courrierArrive.getEmpDest(), courrierArrive );
	}


	
	
	

	public Connexion getCnx() {
		return cnx;
	}

	public void setCnx(Connexion cnx) {
		this.cnx = cnx;
	}



	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}


	

	

	public String getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSelectedEtablissement() {
		return selectedEtablissement;
	}

	public void setSelectedEtablissement(int selectedEtablissement) {
		this.selectedEtablissement = selectedEtablissement;
	}

	public int getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(int selectedService) {
		this.selectedService = selectedService;
	}

	
	
	public List<Etablissement> getEtablissements() {
		return etablissements;
	}

	public void setEtablissements(List<Etablissement> etablissements) {
		this.etablissements = etablissements;
	}

	public List<ServiceExterne> getServicesExternes() {
		return servicesExternes;
	}

	public void setServicesExternes(List<ServiceExterne> servicesExternes) {
		this.servicesExternes = servicesExternes;
	}

	public List<PersonneExterne> getPersonnesExternes() {
		return personnesExternes;
	}

	public void setPersonnesExternes(List<PersonneExterne> personnesExternes) {
		this.personnesExternes = personnesExternes;
	}


	public List<Employe> getEmployes() {
		return employes;
	}

	public void setEmployes(List<Employe> employes) {
		this.employes = employes;
	}

	

	public List<String> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<String> selectedTags) {
		this.selectedTags = selectedTags;
	}
}
