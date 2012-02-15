package com.belcorp.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Estadistica;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;

public class EstadisticaDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWebEstadistica = "BBWS19Estadistica";
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x7175bd53bf859c88L; // com.belcorp.entidades.Estadistica    
    private static Vector objetos;
    private Usuario usuario;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    
    /**
     * Constructor del DAO para las Estadisticas
     */
    public EstadisticaDB() {
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
    	//setUrl();
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

    /**
     * Metodo para adicionar una estadistica a la base de datos persistente
     * @param est Objeto de una estadistica
     */
    public void addEstadistica(Estadistica est) {
    	int i, n;
    	n = objetos.size();
    	for ( i = 0; i < n; i++) {
    		Estadistica item = (Estadistica) objetos.elementAt(i);
    		if ( item.getOpcion().equals(est.getOpcion()) ) {
    			item.setCantidad(item.getCantidad() + est.getCantidad());
    			item.setFechaHora(est.getFechaHora());
    			return;
    		}
    	}
    	Estadistica item = new Estadistica();
    	item.setOpcion(est.getOpcion());
    	item.setCantidad(est.getCantidad());
    	item.setFechaHora(est.getFechaHora());
    	objetos.addElement(item);
    	persist.commit();
    }

    /**
     * Metodo para enviar todas las estadisticas pendientes de envio al servicio web
     */
    public void sendAll() {
    	int i, n;
    	try {
        	n = objetos.size();
        	for ( i = 0; i < n; i++) {
        		Estadistica item = (Estadistica) objetos.elementAt(i);
        		if ( item.getCantidad() > 0 ) {
            		if ( sendUpdate(item) ) {
            			item.setCantidad(0); // reinicia el contador
            		}
        		}
        	}
        	persist.commit();
    	} catch(Exception e) {
    		Dialog.inform("Error enviando las estadisticas");
    	}
    	
    }

    /**
     * Metodo para registrar la información a enviar al servicio web
     * @param opcion el codigo de pantalla respectiva
     * @param cantidad la cantidad de ingresos en el codigo de pantalla
     * @param fecha fecha del ultimo ingreso en el codigo de pantalla
     */
    private void setURLEstadistica(String opcion, int cantidad, String fecha) {
        URL = Cadenas.URLBASE + "/" + metodoWebEstadistica;
        
        DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() 
        	+ "&IMSI=" + Sistema.getImsi() + "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() 
			+ "&IdRol=" + Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() 
			+ "&CodigoZona=" + usuario.getZonaRegionPais()
			+ "&AnoCampana=" + usuario.getCampana()
			+ "&Opcion=" + opcion
			+ "&cantidad=" + cantidad 
			+ "&FechaHora=" + fecha;
    }

    /**
     * Metodo para enviar una estadística al servicio web por POST 
     * @param est objetos de estadistica a enviar
     * @return si el envio es exitoso o no
     */
    public boolean sendUpdate(Estadistica est) {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
        	setURLEstadistica(est.getOpcion(), est.getCantidad(), est.getFechaHora());
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
                if ( responseUpdate(rootElement.getChildNodes()) ) {
                    resultado = true;
                } else {
                	resultado = false;
                }
            	persist.commit();
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

    /**
     * Metodo para procesar el resultado del envio y la respuesta por XML
     * @param node nodo del XMl de la respuesta del servicio web
     * @return si la respuesta es exitosa o no
     */
    private boolean responseUpdate(NodeList node) {
        Node contactNode = node.item(1);
        String registro = contactNode.getChildNodes().item(0).getNodeValue();        
        if ( registro.equals(RESPONSE_OK) ) {
        	return true;
        } else {
        	return false;
        }
    }    
}
