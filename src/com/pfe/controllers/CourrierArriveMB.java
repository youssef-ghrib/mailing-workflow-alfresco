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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
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
import com.pfe.entities.Employe;
import com.pfe.entities.Etablissement;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.PersonneExterne;
import com.pfe.entities.ServiceExterne;
import com.pfe.entities.ServiceInterne;





@ManagedBean(name = "courrierArriveMB")
@ViewScoped
public class CourrierArriveMB {

	@ManagedProperty(value = "#{connexion}")
	private Connexion cnx;
	private InputStream file;
	private UploadedFile uploadedFile;
	
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}


	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	private Date dateReception;
    private boolean traite=false;
	private Date date1;
	private Date date2;
	private String radio = "personne";
	private String searchType;
	private String recherche;
	private String priorite;
	private String espace="Boite de reception";
	private String selectedType = "Mot cle";
	private String commentaires = null;
	private String text = null;
	private int selectedEtablissement = 0;
	private int selectedService = 0;
	private Fichier fichier;
	private CourrierArrive courrierArrive = new CourrierArrive();
	private Historique historique = new Historique();
	private Employe employe = new Employe();
	private RestApi restApi;
	private OpenCMIS openCMIS ;
	private List<Historique> historiques = new ArrayList<Historique>();
	private List<CourrierArrive> courriersArrives = new ArrayList<CourrierArrive>();
	private List<CourrierArrive> filteredCourriers = new ArrayList<CourrierArrive>();
	private List<Etablissement> etablissements = new ArrayList<Etablissement>();
	private List<ServiceExterne> servicesExternes = new ArrayList<ServiceExterne>();
	private List<PersonneExterne> personnesExternes = new ArrayList<PersonneExterne>();
	private List<ServiceInterne> servicesInternes = new ArrayList<ServiceInterne>();
	private List<Employe> employes = new ArrayList<Employe>();
	private List<Object[]> courriersAdmin = new ArrayList<Object[]>();
	private List<CourrierArrive> courriersTraites ;
	private List<String> selectedTags = new ArrayList<String>();
	
	


	public List<CourrierArrive> getCourriersTraites() {
		return courriersTraites;
	}


	public void setCourriersTraites(List<CourrierArrive> courriersTraites) {
		this.courriersTraites = courriersTraites;
		
	}

	FichierDAO fichierDAO = new FichierDAO();
	HistoriqueDAO historiqueDAO = new HistoriqueDAO();
	CourrierArriveDAO courrierArriveDAO = new CourrierArriveDAO();
	EtablissementDAO etablissementDAO = new EtablissementDAO();
	ServiceExterneDAO serviceExterneDAO = new ServiceExterneDAO();
	PersonneExterneDAO personneExterneDAO = new PersonneExterneDAO();
	ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO();
	EmployeDAO employeDAO = new EmployeDAO();

	
	public void initTags(){
		selectedTags= new ArrayList<String>();
		selectedTags.add("sdf");
		selectedTags.add("sdfdf");
		selectedTags.add("sdfdddf");
		
	}

public void initAlfresco(){
 
	restApi = new RestApi(cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
	 openCMIS = new OpenCMIS(cnx.getEmploye(),cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
}


	public boolean isTraite() {
	return traite;

	}

	public void delete(){
	List<Historique>	historiques =historiqueDAO.findHistorique(courrierArrive);
	for(Historique h:historiques){
		historiqueDAO.supprimer(h);
	}
	fichierDAO.supprimer(fichier);
	courrierArriveDAO.supprimer(courrierArrive);
	openCMIS.removeFile(courrierArrive);
	
	}
	 
public void setTraite(boolean traite) {
	this.traite = traite;
}


	public Fichier getFichier() {
		return fichier;
	}

	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}

	@PostConstruct
	void init() {
		employe = cnx.getEmploye();
		
	}

	public void initEtablissements() {
		etablissements = courrierArriveDAO.getEtablissement();
	}

	// intialiser les services suivant l'etablissement selectionné
	public void initService(AjaxBehaviorEvent e) {
		servicesExternes = courrierArriveDAO
				.getServiceExterne(selectedEtablissement);
	}

	// Initialiser le "SelectOneMenu" avec la liste des services internes
	public void initSelectOneMenu() {
		
		courrierArrive = new CourrierArrive();
		etablissements = etablissementDAO.lister();
		servicesInternes = serviceInterneDAO.lister();

	}

	// Initialiser le "DataTable" avec la liste des employés
	public void initEspaceService() {
		courriersArrives = courrierArriveDAO.select(cnx.getEmploye()
				.getServiceInt());
	}

	// Initialiser le "DataTable" avec la liste des employés
	public void initBoiteReception() {
		courriersArrives = courrierArriveDAO.lister(cnx.getEmploye());
		
	}

	

	// Initialiser le "DataTable" avec la liste des employés
	public void initCourriersNonTraites() {
		courriersArrives = courrierArriveDAO.listerCourriersNonEnvoyes(cnx.getEmploye());
		servicesInternes = serviceInterneDAO.lister();
	}

	public void onEtablissementChange() {
		servicesExternes = serviceExterneDAO.refresh(courrierArrive
				.getEtablissementSource());
	}

	public void onServiceExterneChange() {
		personnesExternes = personneExterneDAO.refresh(courrierArrive
				.getServiceExtSource());
	}

	public void onServiceInterneChange() {
		employes = employeDAO.refresh(courrierArrive.getServiceIntDest());
	}

	public void getAttribute(ActionEvent event)
			throws UnsupportedOperationException, org.json.JSONException,
			IOException {

		courrierArrive = (CourrierArrive) event.getComponent().getAttributes()
				.get("selectedItem");
		
		fichier = fichierDAO.getFichier(courrierArrive);
		
		onEtablissementChange();
		onServiceExterneChange();
		
	
		
		
		
		
		
		if(courrierArrive.getServiceIntDest()==null){
			
		}
		else {
			
			
		}

	}

	public String getTicket() throws UnsupportedOperationException,
			org.json.JSONException, IOException {
	

		return restApi.getTicket();
	}

	public void traiter() throws org.json.JSONException, IOException {
		courrierArrive.setStatut("traité");
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
		courriersArrives.remove(courrierArrive);
	
		openCMIS.moveToArchive(courrierArrive, fichier);
		
		restApi.addComment(fichier, commentaires);
		commentaires = null;
	}

	public void rechercher() {
		
		

		   if(traite==false){
				if (selectedType.equals("Mot cle")) {
					courriersArrives = courrierArriveDAO.getCourriers(priorite,
							employe, date1, date2, selectedEtablissement,
							selectedService, selectedTags, selectedType, espace);
				
				} else {
					courriersArrives = courrierArriveDAO.getCourriers(priorite,
							employe, date1, date2, selectedEtablissement,
							selectedService, text, selectedType, espace);
				}
		   }
		   else{
			   if ( selectedType.equals( "Mot cle" ) ) {
	               courriersTraites = courrierArriveDAO.getCourriersEmploye(  cnx.getEmploye(),date1, date2, selectedEtablissement,
	                       selectedService, selectedTags, selectedType, priorite );

	           }
	           else {
	        	   courriersTraites = courrierArriveDAO.getCourriersEmploye(  cnx.getEmploye(), date1, date2, selectedEtablissement,
	                       selectedService, text, selectedType, priorite );
	           }
		   }
		   
	

	}

	// Supprimer un employé
	public void unlock() {
		courrierArrive.setStatut("libéré");
		courrierArrive.setEmpDest(null);
		courrierArrive.setServiceIntDest(cnx.getEmploye().getServiceInt());
		historique.setCourrierArr(courrierArrive);
		historique.setEmploye(cnx.getEmploye());
		historique.setStatut("libéré");
		historique.setDateReception(courrierArriveDAO.getDate(courrierArrive
				.getRef()));
		Date date = new Date();
		historique.setCommentaires(commentaires);
		historique.setDateFinTraitement(date);
		historiqueDAO.ajouter(historique);
		courrierArriveDAO.modifier(courrierArrive);
		courriersArrives.remove(courrierArrive);
	

		openCMIS.moveFileToService(courrierArrive, fichier);
		commentaires = null;
		cnx.countCourrier();
cnx.resetCounts();
		RequestContext.getCurrentInstance().update(":form:table");
	}

	// Supprimer un employé
	public void lock() {
		
		historique.setCourrierArr(courrierArrive);
		historique.setEmploye(cnx.getEmploye());
		historique.setStatut("vérouillé");
		historique.setCommentaires(commentaires);
		historique.setDateReception(courrierArriveDAO.getDate(courrierArrive
				.getRef()));
		Date date = new Date();
		historique.setDateFinTraitement(date);
		historiqueDAO.ajouter(historique);

		courrierArrive.setStatut("vérouillé");
		courrierArrive.setServiceIntDest(null);
		courrierArrive.setEmpDest(cnx.getEmploye());
		courrierArriveDAO.modifier(courrierArrive);
		
		openCMIS.moveFileToEmp(courrierArrive,fichier);
		courriersArrives.remove(courrierArrive);
		cnx.countCourrier();
cnx.resetCounts();
		commentaires = null;

	}

	public void send() {
	
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
			Thread thread = new Thread(){
				public void run(){
					sendEmail(courrierArrive.getEmpDest().getEmail(),
							"Nouveau courrier", "Vous avez reçu un nouveau courrier");
				}
			};
			thread.start();
			
			openCMIS.moveFile(courrierArrive, fichier);
		} else {

			openCMIS.moveFileToService(courrierArrive, fichier);
		}
		
		courrierArrive.setStatut("envoye");
		courrierArriveDAO.modifier(courrierArrive);
		courriersArrives.remove(courrierArrive);

		cnx.countCourrier();
cnx.resetCounts();
		// RequestContext.getCurrentInstance().update( ":form:table" );
	}

	public Employe getLastUser(String ref) {
		Employe emetteur = courrierArriveDAO.getLastUser(ref);
		return emetteur;
	}

	public Date getCreationDate(String ref) {
		Date date = courrierArriveDAO.getCreationDate(ref);

		return date;
	}

	public Employe getFirstUser(String ref) {
		Employe employe = courrierArriveDAO.getFirstUser(ref);
		return employe;
	}
	
	
	public void edit() throws IOException, org.json.JSONException{
		Fichier tempFichier =null;
		if(file!=null){
			
			openCMIS.removeFile(courrierArrive);
			
			tempFichier = openCMIS.uploadDocument(courrierArrive, file);
			tempFichier.setCourrierArrive(courrierArrive);
			
			
			
			
		}
		else{
			file=openCMIS.getDocument(courrierArrive);
			openCMIS.removeFile(courrierArrive);
			tempFichier = openCMIS.uploadDocument(courrierArrive, file);
			tempFichier.setCourrierArrive(courrierArrive);
		}
		
		courrierArriveDAO.modifier(courrierArrive);
		fichierDAO.modifier(tempFichier);
		
		restApi.addComment(fichier, historique.getCommentaires());
		restApi.addTag(fichier, courrierArrive.getTags());
			
		
	}
	


	public String add() throws IOException, JSONException, org.json.JSONException {
		String redirect=null;
		if(file==null){
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Error!",
					Language.message("uploder_fichier"));
			FacesContext context = FacesContext.getCurrentInstance();
		
		
				context.addMessage("form:fichier", message);
				
		
				RequestContext.getCurrentInstance().update("@form");
				//RequestContext.getCurrentInstance().update(":form:msgFichier");
		}
		else{
			
		
		
		
		
		courrierArrive.setRef(courrierArriveDAO.generateName(courrierArrive
				.getDateReception()));
		if ((courrierArrive.getEmpDest() == null)
				&& (courrierArrive.getServiceIntDest() == null)) {
			courrierArrive.setStatut("non envoye");
			historique.setStatut("non envoye");
		} 
		else {
			courrierArrive.setStatut("envoye");
			historique.setStatut("envoye");
		}

		if (courrierArrive.getEmpDest() != null) {
			courrierArrive.setServiceIntDest(null);
	
		}
		Date date = new Date();
		historique.setDateFinTraitement(date);
		historique.setEmploye(cnx.getEmploye());
		historique.setCourrierArr(courrierArrive);
		historique.setDateReception(courrierArrive.getDateReception());

		
	
	

		Fichier tempFichier = openCMIS.uploadDocument(courrierArrive, file);

		tempFichier.setCourrierArrive(courrierArrive);
		if(!historique.getCommentaires().equals("")){
		
			restApi.addComment(tempFichier, historique.getCommentaires());
		}
		if(!courrierArrive.getTags().equals("")){
		restApi.addTag(tempFichier, courrierArrive.getTags());
		}
	//	courrierArriveDAO.ajouter(courrierArrive);
		fichierDAO.ajouter(tempFichier);
		historiqueDAO.ajouter(historique);
		System.out.println("finish");
		if(courrierArrive.getStatut().equals("non envoye")){
			redirect="/restreint/CourrierArrive/CourrierNonEnvoye/courriersNonEnvoye?faces-redirect=true";
		}
		else{
			redirect="/presets/successmailSend?faces-redirect=true";
		}
		// sendEmail( courrierArrive.getEmpDest().getEmail(),
		// "Nouveau courrier", "Vous avez reçu un nouveau courrier" );
		}
		return redirect;
	}

	public void upload(FileUploadEvent event) throws IOException, JSONException {
		 
	
		
		uploadedFile=event.getFile();
		file = uploadedFile.getInputstream();
		
		
		

		// courrierArrive.set
		// openCMIS.moveFile( courrierArrive.getEmpDest(), courrierArrive );
	}

	public List<String> completeTag(String query) {
		HashSet<String> tags = courrierArriveDAO.getTags(query);
		List<String> list = new ArrayList<String>(tags);
		return list;
	}

	public List<String> completeObjet(String query) {
		
		List<String> texts=null;
		if(selectedType.equals("Objet")){
			texts= courrierArriveDAO.getObjet(query,cnx.getEmploye(),espace);
		}
		else{
			
			texts= courrierArriveDAO.getRef(query,cnx.getEmploye(),espace);
		}
		
		
		
		return texts;
	}

	
	
	public void voirHistorique(ActionEvent event) {

		historiques = courrierArriveDAO.GetHistorique(courrierArrive.getRef());
		courrierArrive = historiques.get(0).getCourrierArr();
		
		for (Historique h : historiques) {
			
		}

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
			
		}
	}

	public Connexion getCnx() {
		return cnx;
	}

	public void setCnx(Connexion cnx) {
		this.cnx = cnx;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
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

	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getRecherche() {
		return recherche;
	}

	public void setRecherche(String recherche) {
		this.recherche = recherche;
	}

	public String getPriorite() {
		return priorite;
	}

	public void setPriorite(String priorite) {
		this.priorite = priorite;
	}

	public String getEspace() {
		return espace;
	}

	public void setEspace(String espace) {
		this.espace = espace;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
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

	public CourrierArrive getCourrierArrive() {
		return courrierArrive;
	}

	public void setCourrierArrive(CourrierArrive courrierArrive) {
		this.courrierArrive = courrierArrive;
	}

	public Historique getHistorique() {
		return historique;
	}

	public void setHistorique(Historique historique) {
		this.historique = historique;
	}

	public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	public List<Historique> getHistoriques() {
		return historiques;
	}

	public void setHistoriques(List<Historique> historiques) {
		this.historiques = historiques;
	}

	public List<CourrierArrive> getCourriersArrives() {
		return courriersArrives;
	}

	public void setCourriersArrives(List<CourrierArrive> courriersArrives) {
		this.courriersArrives = courriersArrives;
	}

	public List<CourrierArrive> getFilteredCourriers() {
		return filteredCourriers;
	}

	public void setFilteredCourriers(List<CourrierArrive> filteredCourriers) {
		this.filteredCourriers = filteredCourriers;
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

	public List<ServiceInterne> getServicesInternes() {
		return servicesInternes;
	}

	public void setServicesInternes(List<ServiceInterne> servicesInternes) {
		this.servicesInternes = servicesInternes;
	}

	public List<Employe> getEmployes() {
		return employes;
	}

	public void setEmployes(List<Employe> employes) {
		this.employes = employes;
	}

	public List<Object[]> getCourriersAdmin() {
		return courriersAdmin;
	}

	public void setCourriersAdmin(List<Object[]> courriersAdmin) {
		this.courriersAdmin = courriersAdmin;
	}

	public List<String> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<String> selectedTags) {
		this.selectedTags = selectedTags;
	}
}
