package com.belcorp.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Facturacion;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;

public class FacturacionDB {
    private static final String metodoWeb = "BBWS15Facturacion"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x3c94bef2124dd927L; // com.belcorp.entidades.Facturacion
    private static Vector objetos;
    private Usuario usuario;

    public FacturacionDB() {
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
    	
    	setUrl();
        persist = PersistentStore.getPersistentObject( IDSTORE ); 
        try {
                objetos = (Vector) persist.getContents();
        } catch (Exception e) {
                objetos = null;
        }
        try {
            if ( objetos == null) {
                objetos = new Vector();
                persist.setContents(objetos);
                persist.commit();
            }
        } catch (Exception e) {
        }
    }    

    private void setUrl() {
    	URL = Cadenas.URLBASE + "/" + metodoWeb;
    	
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    		+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&AnhoCampanha=" + usuario.getCampana();
    	
    }
    
    private boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        objetos = new Vector();
        
        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Facturacion facturacion = new Facturacion();
            facturacion.setCampana( fields[2] );
            if ( fields[3].trim().equals("") ) {
                facturacion.setUbicacion("N/S");
            } else {
                facturacion.setUbicacion(fields[3]);
            }
            facturacion.setPedidos( Double.parseDouble(fields[4]) );
            facturacion.setPeg( Double.parseDouble(fields[5]) );
            facturacion.setPrimerpedido( Double.parseDouble(fields[6]) );
            facturacion.setEnviado( Double.parseDouble(fields[7]) );
            facturacion.setObservado( Double.parseDouble(fields[8]) );
            facturacion.setRechazado( Double.parseDouble(fields[9]) );
            facturacion.setFacturado( Double.parseDouble(fields[10]) );
            facturacion.setMonto( Double.parseDouble(fields[11]) );
            facturacion.setActivassinpedido( Double.parseDouble(fields[12]) );
            objetos.addElement(facturacion);
        }
        sortFacturas();
        persist.setContents(objetos);
        persist.commit();
        return true;
    }

    private void sortFacturas() {
        int n, i, j;
        if (objetos == null) {
        	n = 0;
        } else {
            n = objetos.size();
        }
        for ( i = 0; i < n; i++ ) {
            for ( j = 0; j < n; j++ ) {
            	Facturacion facI = (Facturacion) objetos.elementAt(i); 
            	Facturacion facJ = (Facturacion) objetos.elementAt(j);
            	try {
                	if ( facI.getCampana().compareTo(facJ.getCampana()) > 0 ) { // I es menor que J
                		objetos.setElementAt(facI, j);
                		objetos.setElementAt(facJ, i);
                	}
            	} catch(Exception e) {
            		
            	}
            }
        }
    }
    
    public boolean getRemote() {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
        	setUrl();
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
            httpConn.setRequestMethod(HttpConnection.POST);
            httpConn.setRequestProperty("Host", Cadenas.URIServer);
            httpConn.setRequestProperty("Connection", "close");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Content-Length", "" + DATA.length());
            os = httpConn.openOutputStream();
            os.write(DATA.getBytes("UTF-8"));
            os.flush();
            int responseCode = httpConn.getResponseCode();
            if ( responseCode == HttpConnection.HTTP_OK ) {
                is = httpConn.openInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                if ( fillObjectos(rootElement.getChildNodes()) ) {
                    resultado = true;
                }
                is.close();
                is = null;
            } else {
                resultado = false;
            }
            httpConn.close();
            httpConn = null;
        } catch(Exception ex) {
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
            resultado = false;
        } finally {
            try { os.close(); } catch(Exception e) { }
            os = null;
            try { is.close(); } catch(Exception e) { }
            is = null;
        }
        return resultado;
    }
    
    public  Vector getObjetos() {
        return objetos;
    }
    
    public Vector getObjetosByCampana(String campana) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for(int i = 0; i < n; i++) {
    		Facturacion fac = (Facturacion) objetos.elementAt(i);
    		if ( fac.getCampana().equals(campana) ) {
    			result.addElement(fac);
    		}
    	}
        return result;
    }

    public double[] ArrayTotalesxFila(Vector vec) {
        double specificationArray[] = null;
        Vector vFacturacion = null;
        Facturacion facturacion;
	        try { 
	        	if(vec == null )
	        		vec = objetos;
	        	vFacturacion = vec;
	            specificationArray = new double[9];
	            int count = 0;
	            for (count = 0; count < vFacturacion.size(); count++) {
	            	facturacion = (Facturacion) vFacturacion.elementAt(count);
	            	
	                specificationArray[0] = specificationArray[0] + facturacion.getPedidos();
	                specificationArray[1] = specificationArray[1] + facturacion.getPeg();
	                specificationArray[2] = specificationArray[2] + facturacion.getPrimerpedido();
	                specificationArray[3] = specificationArray[3] + facturacion.getEnviado();
	                specificationArray[4] = specificationArray[4] + facturacion.getObservado();
	                specificationArray[5] = specificationArray[5] + facturacion.getRechazado();
	                specificationArray[6] = specificationArray[6] + facturacion.getFacturado();
	                specificationArray[7] = specificationArray[7] + Sistema.round(facturacion.getMonto());
	                specificationArray[8] = specificationArray[8] + facturacion.getActivassinpedido();
	               
	                if (specificationArray[count] == 0.0 )
	                     	specificationArray[count] = 0.0;
	                }
	        } catch (Exception e) {
	            Dialog.alert("Ex: " + e);
	        }
        return specificationArray;
    }

    
    /***
     * 
     * @param vec 
     * @return arreglo bidimensional. filas: campañas, columnas: categorias 
     */
    public double[][] arrayTotalesxFilaAgrup(Vector vec) {
        double specificationArray[][] = null;
        Vector vFacturacion = null;
        Facturacion facturacion;
        String campanas[] = this.mostrarCampanas(vec);
        int camp = campanas.length; 
        try { 
        	if(vec == null )
        		vec = objetos;
        	vFacturacion = vec;
            //specificationArray = new double[vFacturacion.size()][camp];
            specificationArray = new double[9][camp];
            int count = 0;
            for(int i = 0; i < camp; i++){
	            for (count = 0; count < vFacturacion.size(); count++) {
	            	facturacion = (Facturacion) vFacturacion.elementAt(count);
	            	if(facturacion.getCampana().equals(campanas[i])){
	            		specificationArray[0][i] = Sistema.round(specificationArray[0][i]) + Sistema.round(facturacion.getPedidos());
		                specificationArray[1][i] = Sistema.round(specificationArray[1][i]) + Sistema.round(facturacion.getPeg());
		                specificationArray[2][i] = Sistema.round(specificationArray[2][i]) + Sistema.round(facturacion.getPrimerpedido());
		                specificationArray[3][i] = Sistema.round(specificationArray[3][i]) + Sistema.round(facturacion.getEnviado());
		                specificationArray[4][i] = Sistema.round(specificationArray[4][i]) + Sistema.round(facturacion.getObservado());
		                specificationArray[5][i] = Sistema.round(specificationArray[5][i]) + Sistema.round(facturacion.getRechazado());
		                specificationArray[6][i] = Sistema.round(specificationArray[6][i]) + Sistema.round(facturacion.getFacturado());
		                specificationArray[7][i] = specificationArray[7][i] + facturacion.getMonto();
		                specificationArray[8][i] = Sistema.round(specificationArray[8][i]) + Sistema.round(facturacion.getActivassinpedido());
		               
//		        	    Dialog.inform("1.8");
//		                if (specificationArray[count][i] == 0.0 ) {
//	                     	specificationArray[count][i] = 0.0;
//		                }
	            		
	            	}
	            }            	
            }
        } catch (Exception e) {
            Dialog.alert("ExMC: " + e);
        }
        return specificationArray;
    }
    
    public String[] mostrarSecciones(Vector vec){
    	 Vector vFacturacion = null;
         Facturacion facturacion;
         String specificationArray[] = null;
         try { 
        	 if ( vec == null) {
        		 vFacturacion = objetos;
        	 } else {
        		 vFacturacion = vec;
        	 }
            specificationArray = new String[vFacturacion.size()];
            int count = 0;
            for (count = 0; count < specificationArray.length; count++) {
            	facturacion = (Facturacion) vFacturacion.elementAt(count);
                    specificationArray[count] = facturacion.getUbicacion();
                    if (specificationArray[count] == null)
                            specificationArray[count] = "";
                }
	        } catch (Exception e) {
	            Dialog.alert("Ex MS: " + e);
	        }
    	return this.eliminarValoresRepetidos(specificationArray);
    }
    
    public String[] mostrarCampanas(Vector vec){
   	 	Vector vFacturacion = null;
        Facturacion facturacion;
        String specificationArray[] = null;
        try { 
	   	   	 if ( vec == null) {
				 vFacturacion = objetos;
			 } else {
				 vFacturacion = vec;
			 }
           specificationArray = new String[vFacturacion.size()];
           int count = 0;
           for (count = 0; count < specificationArray.length; count++) {
        	   facturacion = (Facturacion) vFacturacion.elementAt(count);
        	   specificationArray[count] = facturacion.getCampana();  
        	   if (specificationArray[count] == null)
        		   specificationArray[count] = "";
               }
	    } catch (Exception e) {
	    	Dialog.alert("Ex: " + e);
	    }
	 return this.eliminarValoresRepetidos(specificationArray);   
   }
    
    private String[] eliminarValoresRepetidos(String[] arr){
		Vector v = new Vector();
		for(int i = 0; i < arr.length; i++){
			if(i > 0){
				if(!this.isExiste(arr[i], v)){
					v.addElement(arr[i]);
				}
			}else{
				v.addElement(arr[i]);
			}
		}	
		String[] str = new String[v.size()];
		for(int i = 0;i < str.length; i++){
			str[i] = (String) v.elementAt(i);
		}
    	return str;
    }
    
	private boolean isExiste(String str, Vector v){
		for(int i = 0; i < v.size(); i++){
				if(v.elementAt(i).equals(str))
					return true;
		}
		return false;
	}
	
	public Facturacion getRemoteChild(String ubica) {
		
	        HttpConnection httpConn = null;
	        InputStream is = null;
	        Vector child =null;
	        Facturacion facturacion = null ;
	        try {
	        	
	            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
	            httpConn.setRequestMethod(HttpConnection.GET);
	            if ( httpConn.getResponseCode() == HttpConnection.HTTP_OK ) {
	                is = httpConn.openInputStream();
	                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	                DocumentBuilder builder = factory.newDocumentBuilder();
	                Document document = builder.parse( is );
	                Element rootElement = document.getDocumentElement();
	                rootElement.normalize();
	               child = bajarNivelDetalle(rootElement.getChildNodes(),ubica);
	              for(int n = 0; n < objetos.size(); n++){
	            	   facturacion = ((Facturacion)objetos.elementAt(n));
	            	  if(facturacion.getUbicacion().equalsIgnoreCase(ubica)){
	            		  facturacion.setFacturacion(child);
	            		  return facturacion;
	            	  }
	              }    
	                is.close();
	                is = null;
	            } 
	            httpConn.close();
	            httpConn = null;
	        } catch(Exception ex) {
	            try { is.close(); } catch(Exception e) { }
	            is = null;
	            try { httpConn.close(); } catch(Exception e) { }
	            httpConn = null;
	          
	        }
			return facturacion; 
	}
    
	private Vector bajarNivelDetalle(NodeList node, String  ubica) {
		 int n = node.getLength();
//		 Facturacion fac = (Facturacion) objetos.elementAt(index);
		 Vector obj = null;
	        for (int i = 1; i < n; i = i + 2) {
	        	Node contactNode = node.item(i);
	            String registro = contactNode.getChildNodes().item(0).getNodeValue();
	            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
	        	if(ubica.equals(fields[0])){
	        		obj = new Vector();
		            Facturacion facturacion = new Facturacion();
		            facturacion.setUbicacion(fields[0]);
		            facturacion.setPedidos( Double.parseDouble(fields[1]) );
		            facturacion.setPeg( Double.parseDouble(fields[2]) );
		            facturacion.setPrimerpedido( Double.parseDouble(fields[3]) );
		            facturacion.setEnviado( Double.parseDouble(fields[4]) );
		            facturacion.setObservado( Double.parseDouble(fields[5]) );
		            facturacion.setRechazado( Double.parseDouble(fields[6]) );
		            facturacion.setFacturado( Double.parseDouble(fields[7]) );
		            facturacion.setMonto( Double.parseDouble(fields[8]) );
		            facturacion.setActivassinpedido( Double.parseDouble(fields[9]) );
		            obj.addElement(facturacion);
		           
	        	}
	        }
	        return obj;
	}
	
}
