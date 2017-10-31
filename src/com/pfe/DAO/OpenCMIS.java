package com.pfe.DAO;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.primefaces.model.UploadedFile;

import com.pfe.entities.CourrierArrive;
import com.pfe.entities.CourrierDepart;
import com.pfe.entities.Employe;
import com.pfe.entities.Fichier;
import com.pfe.entities.ServiceInterne;

public class OpenCMIS {
	Session session;
	final String PATH ="/sites/gce/documentLibrary/";
	private Employe user;
	private FichierDAO fichierDao = new FichierDAO();
	
	private String url;
	
	
	public OpenCMIS(Employe user,String serverAdress,String serverPort){
	
		connexion();
		this.user=user;
		url="http://"+serverAdress+":"+serverPort;
	}
	
	
	public void connexion(){
		Map<String, String> parameter = new HashMap<String, String>();

		// user credentials
		parameter.put(SessionParameter.USER, "admin");
		parameter.put(SessionParameter.PASSWORD, "admin");

		// connection settings
		parameter.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/alfresco/cmisatom");
		parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());

		// set the alfresco object factory
		parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

		// create session
		SessionFactoryImpl factory = SessionFactoryImpl.newInstance();
		session= factory.getRepositories(parameter).get(0).createSession();
		
	}

	
	public InputStream getDocument(CourrierArrive courrier){
	String sourcePath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/";
		if(courrier.getStatut().equals("non envoye") ){
			sourcePath=sourcePath+"courriersNonEnvoyes/";
		
		}
		Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
		return doc.getContentStream().getStream();
		
	
	}
	public void changeServiceFolder(String nom,ServiceInterne service){
		Folder folder = (Folder) session.getObjectByPath(PATH+nom+"/");
		 Map<String, Object> properties = new HashMap<String, Object>();
			
		 properties.put(PropertyIds.NAME, service.getNom());
		
		folder.updateProperties(properties);
	}
	public void removeServiceFolder(ServiceInterne service){
		
Folder folder = (Folder) session.getObjectByPath(PATH+service.getNom()+"/");
		
		List<CmisObject> objList = new ArrayList<CmisObject>();
		
		String queryString = "select * from cmis:document where  in_folder('" + folder.getId() + "')";
		 // execute query
		ItemIterable<QueryResult> results = session. query(queryString, false);
	
		
		for (QueryResult qResult : results) {
			 PropertyData<?> propData = qResult.getPropertyById("cmis:objectId");
			 String objectId = (String) propData.getFirstValue();
			 CmisObject obj = session.getObject(session.createObjectId(objectId));
			 objList.add(obj);
			 }
		for(CmisObject object:objList){
			object.delete();
		}

		ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO ();
		List<Employe>employes=serviceInterneDAO.listerEmploye(service);
		for(Employe emp:employes){
			removeUserFolder(emp);
		}
		
		folder.delete();
		
	}
	
	public void removeUserFolder(Employe emp){

		Folder folder = (Folder) session.getObjectByPath(PATH+emp.getServiceInt().getNom()+"/"+emp.getUsername()+"/");
	
		if(emp.getRole().equals("ABO")){
			List<CmisObject> objList = new ArrayList<CmisObject>();
			Folder folder2 = (Folder) session.getObjectByPath(PATH+emp.getServiceInt().getNom()+"/"+emp.getUsername()+"/"+"courriersNonEnvoyes/");
			String queryString = "select * from cmis:document where  in_folder('" + folder2.getId() + "')";
			 // execute query
			ItemIterable<QueryResult> results = session. query(queryString, false);
		
			
			for (QueryResult qResult : results) {
				 PropertyData<?> propData = qResult.getPropertyById("cmis:objectId");
				 String objectId = (String) propData.getFirstValue();
				 CmisObject obj = session.getObject(session.createObjectId(objectId));
				 objList.add(obj);
				 }
			for(CmisObject object:objList){
				object.delete();
			}
		folder2.delete();
		}
		List<CmisObject> objList = new ArrayList<CmisObject>();
		String queryString = "select * from cmis:document where  in_folder('" + folder.getId() + "')";
		 // execute query
		ItemIterable<QueryResult> results = session. query(queryString, false);
	
		
		for (QueryResult qResult : results) {
			 PropertyData<?> propData = qResult.getPropertyById("cmis:objectId");
			 String objectId = (String) propData.getFirstValue();
			 CmisObject obj = session.getObject(session.createObjectId(objectId));
			 objList.add(obj);
			 }
		for(CmisObject object:objList){
			object.delete();
		}
		
		
		folder.delete(); 
		
	}
	public void createUserFolder(Employe emp) throws InterruptedException{
		Thread.sleep(1000);
		
	 System.out.println(emp.getUsername())
	 ;
		
		 Folder folder = (Folder) session.getObjectByPath(PATH+emp.getServiceInt().getNom()+"/");
		 Map<String, Object> properties = new HashMap<String, Object>();
		
		 properties.put(PropertyIds.NAME, emp.getUsername());
		 properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		 
		 folder.createFolder(properties);
		 if(emp.getRole().equals("ABO")){
			 createCourriersNonEnvoye(emp);
		 }
		
		
	}
	public  void createCourriersNonEnvoye(Employe emp){
		 Folder userFolder = (Folder) session.getObjectByPath(PATH+emp.getServiceInt().getNom()+"/"+emp.getUsername()+"/");
		 Map<String, Object> properties2 = new HashMap<String, Object>();
		 properties2.put(PropertyIds.NAME,"courriersNonEnvoyes");
		 properties2.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		 userFolder.createFolder(properties2);
	}
	public void createServiceFolder(ServiceInterne service){
		 Folder folder = (Folder) session.getObjectByPath(PATH);
		 Map<String, Object> properties = new HashMap<String, Object>();
		 properties.put(PropertyIds.NAME, service.getNom());
		 properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		 folder.createFolder(properties);
	}
	public void addTAg(){
		
	}
	public void deleteUserFolder(){
		String path =PATH+user.getUsername()+"/";
		 session.getObjectByPath(path).delete();	
	}
	
	public void removeFile(CourrierArrive courrier){
		String sourcePath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/"+"courriersNonEnvoyes/";
		
		Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
		doc.delete();
	}
	
	
	public void modifyDocument(CourrierArrive courrier){
		String sourcePath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/courriersNonEnvoyes/";	
		Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
		 Map<String, Object> properties = new HashMap<String, Object>();
	        properties.put( PropertyIds.NAME, courrier.getRef());
	        properties.put( PropertyIds.OBJECT_TYPE_ID, "D:ca:doc,P:cm:titled" );
	        properties.put( "ca:objet", courrier.getObjet() );
	        properties.put( "ca:ref",courrier.getRef() );
	        properties.put( "ca:etablissement", courrier.getEtablissementSource().getNom() );
	        properties.put( "ca:service", courrier.getServiceExtSource().getNom() );
	        properties.put( "ca:priorite", courrier.getPriorite() );
	        properties.put( "ca:dateReception", courrier.getDateReception() );
	        doc.updateProperties(properties);
	}
	
	public  void uploadFile(CourrierArrive courrier){
		
		try {
			List<String> tags = new ArrayList<String>();
			tags.add("workspace://SpacesStore/15749ffb-dd90-49e8-8bc9-4b8a36d62a70");
			InputStream is = new FileInputStream("c://chap.pdf");
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.NAME, "qdd");
			properties.put(PropertyIds.OBJECT_TYPE_ID, "D:ca:doc,P:cm:titled");
			properties.put("cm:description", "My document");
			properties.put("ca:objet", "dd");
			properties.put("ca:ref", "4242");
			properties.put("ca:etablissement", "telecom");
			properties.put("ca:service", "telecom");
		
			ContentStream contentStream =  session.getObjectFactory().createContentStream("oussama",122222, ".pdf", is);

			 Folder folder = (Folder) session.getObjectByPath(PATH+user.getUsername()+"/");
		
			Document doc = folder.createDocument(properties, contentStream, null);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Fichier uploadDocument( CourrierArrive courrier, InputStream file) throws IOException {
		  Fichier fichier= new Fichier();
	
		  
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put( PropertyIds.NAME, courrier.getRef());
        properties.put( PropertyIds.OBJECT_TYPE_ID, "D:ca:doc,P:cm:titled" );
        properties.put( "ca:objet", courrier.getObjet() );
        properties.put( "ca:ref",courrier.getRef() );
        properties.put( "ca:etablissement", courrier.getEtablissementSource().getNom() );
        properties.put( "ca:service", courrier.getServiceExtSource().getNom() );
        properties.put( "ca:priorite", courrier.getPriorite() );
        properties.put( "ca:dateReception", courrier.getDateReception() );


        try {

            ContentStream contentStream = session.getObjectFactory().createContentStream( null,
                   0, "application/pdf", file );
            // ContentStream contentStream = new
            // ContentStreamImpl(file.getAbsolutePath(), null,
            // "application/pdf",
            // new ByteArrayInputStream(bytes));

        
            Folder folder = null;
            if(courrier.getStatut().equals("non envoye")){
            	folder = (Folder) session.getObjectByPath( PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/courriersNonEnvoyes/");
            	
            }
          if(courrier.getServiceIntDest()!=null){
        	  folder = (Folder) session.getObjectByPath( PATH+courrier.getServiceIntDest().getNom()+"/");
        	  
          }
         
         if( courrier.getEmpDest()!=null){
         folder = (Folder) session.getObjectByPath( PATH+courrier.getEmpDest().getServiceInt().getNom()+"/"+courrier.getEmpDest().getUsername()+"/");
         properties.put( "ca:empDest", courrier.getEmpDest().getNom());
          
         }

            Document doc = folder.createDocument( properties, contentStream, null );
         
         
          int index=doc.getId().indexOf(";");
          String url ="workspace/SpacesStore/";
          String chemin=doc.getId().substring(24, index);
            fichier.setChemin(url+chemin);

        } catch ( Exception e ) {
            e.printStackTrace();
            /*
             * Same error here.
             * org.apache.chemistry.opencmis.commons.exceptions.
             * CmisRuntimeException: Internal Server Error
             */
        }
return fichier;
    }

	
	
	
	public Fichier uploadCourrierDepart( CourrierDepart courrier, InputStream file) throws IOException {
		  Fichier fichier= new Fichier();
		
		  
      Map<String, Object> properties = new HashMap<String, Object>();
      properties.put( PropertyIds.NAME, courrier.getRef());
      properties.put( PropertyIds.OBJECT_TYPE_ID, "D:cd:doc,P:cm:titled" );
      properties.put( "cd:objet", courrier.getObjet() );
      properties.put( "cd:ref",courrier.getRef() );
      properties.put( "cd:etablissement", courrier.getEtablissementDest().getNom() );
      properties.put( "cd:service", courrier.getServiceExtDest().getNom());
      properties.put( "cd:personne", courrier.getPersonneExtDest().getNom());
      properties.put( "cd:priorite", courrier.getPriorite() );
      properties.put( "cd:dateEnvoi", courrier.getDateEnvoi() );


      try {

          ContentStream contentStream = session.getObjectFactory().createContentStream( null,
                 0, "application/pdf", file );
          // ContentStream contentStream = new
          // ContentStreamImpl(file.getAbsolutePath(), null,
          // "application/pdf",
          // new ByteArrayInputStream(bytes));

      
   
        Folder folder = (Folder) session.getObjectByPath( PATH+"CourriersDepart/");

          Document doc = folder.createDocument( properties, contentStream, null );
        int index=doc.getId().indexOf(";");
        String url ="workspace/SpacesStore/";
        String chemin=doc.getId().substring(24, index);
          fichier.setChemin(url+chemin);

      } catch ( Exception e ) {
          e.printStackTrace();
          /*
           * Same error here.
           * org.apache.chemistry.opencmis.commons.exceptions.
           * CmisRuntimeException: Internal Server Error
           */
      }
return fichier;
  }
public void moveFile(CourrierArrive courrier,Fichier fichier){
	
	String sourcePath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/";
if(courrier.getStatut().equals("non envoye")){
	 sourcePath= sourcePath+"courriersNonEnvoyes/";
	}
	String destPath=PATH+courrier.getEmpDest().getServiceInt().getNom()+"/"+ courrier.getEmpDest().getUsername()+"/";
	Folder SourceFolder = (Folder) session.getObjectByPath(sourcePath);
	Folder DestFolder = (Folder) session.getObjectByPath(destPath);
	Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
	doc.move(SourceFolder, DestFolder);
	
}


public void moveUserToService(Employe emp,ServiceInterne service){
    String sourcePath=PATH+emp.getServiceInt().getNom()+"/";
	String destPath=PATH+service.getNom()+"/";
	Folder SourceFolder = (Folder) session.getObjectByPath(sourcePath);
	Folder DestFolder = (Folder) session.getObjectByPath(destPath);
	Folder UserFolder = (Folder) session.getObjectByPath(sourcePath+emp.getUsername()+"/");
	UserFolder.move(SourceFolder, DestFolder);
	
}
public void moveFileToService(CourrierArrive courrier,Fichier fichier){
	
	String sourcePath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/";
	if(courrier.getStatut().equals("non envoye")){
		 sourcePath= sourcePath+"courriersNonEnvoyes/";
		}
	String destPath=PATH+courrier.getServiceIntDest().getNom()+"/";
	Folder SourceFolder = (Folder) session.getObjectByPath(sourcePath);
	Folder DestFolder = (Folder) session.getObjectByPath(destPath);
	Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
	doc.move(SourceFolder, DestFolder);
	
}


public void moveFileToEmp(CourrierArrive courrier,Fichier fichier){
	
	String sourcePath=PATH+user.getServiceInt().getNom()+"/";
	
	String destPath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/";
	Folder SourceFolder = (Folder) session.getObjectByPath(sourcePath);
	Folder DestFolder = (Folder) session.getObjectByPath(destPath);
	Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
	doc.move(SourceFolder, DestFolder);
	
}
public void moveToArchive(CourrierArrive courrier,Fichier fichier){
	String sourcePath=PATH+user.getServiceInt().getNom()+"/"+user.getUsername()+"/";
	String destPath=PATH+"Archive/";
	Folder SourceFolder = (Folder) session.getObjectByPath(sourcePath);
	Folder DestFolder = (Folder) session.getObjectByPath(destPath);
	Document doc = (Document) session.getObjectByPath(sourcePath+courrier.getRef());
	doc.move(SourceFolder, DestFolder);
	
}
public void test(){
	Document doc = (Document) session.getObjectByPath("/sites/gce/documentLibrary/amine/bizzz");
	
	
}


	
}
