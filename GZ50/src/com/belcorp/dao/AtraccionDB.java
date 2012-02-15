package com.belcorp.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
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

import com.belcorp.entidades.Atraccion;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;

public class AtraccionDB {
    private static final String metodoWeb = "BBWS05ObtenerAtraccionInformacionPostulante"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x96d46f30785045baL; // com.belcorp.entidades.atraccion
    private static Vector objetos;
    private Usuario usuario;
    private String docIdentidad = null;

    /**
     * Constructor del DAO para las atracciones
     */
	public AtraccionDB() {
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

	/**
	 * Metodo privado para la configuración de la información a enviar por POST al servicio web
	 */
    private void setUrl() {
    	URL = Cadenas.URLBASE + "/" + metodoWeb;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    		+ "&IDAPP=" + Sistema.getIdapp() 
        	+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
        	+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
        	+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CampanaActual=" + usuario.getCampana() + "&DocumentoIdentidad=" + docIdentidad;
    }
	
    /**
     * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
     * @param node
     * @return si se puede formatear la información del XML a los objetos respectivos de Atraccion
     * @throws Exception
     */
    private boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        //objetos = new Vector();
        Node contactNode = node.item(1);
        String registro = contactNode.getChildNodes().item(0).getNodeValue();
        String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
        Atraccion atraccion = new Atraccion();
        atraccion.setDocIdentidad(docIdentidad);
        atraccion.setResultado( fields[0] );
        //TODO: la fecha debe ser del ws o del bb
        atraccion.setFechaConsulta(Fechas.dateToString("yyyy/MM/dd HH:mm aa"));
        //atraccion.setFechaConsulta(fields[1]); //Fechas.dateToString("yyyy/MM/dd hh:mm"));
        objetos.addElement(atraccion);
        persist.setContents(objetos);
        persist.commit();
        return true;
    }
    
    /**
     * Metodo privada, usado para la llamara al servicio web por HTTP POST
     * @return si la conexion HTTP es correcta
     */
    private boolean getRemote() {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
        	setUrl();
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS() );
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

    /**
     * Metodo publico, usado para obtener el vector con los objetos de la base de datos persistente
     * @return
     */
    public  Vector getObjetos() {
        return objetos;
    }
    
    /**
     * Metodo publico, usado para la búsqueda de atracciones almacendas en la base de datos persistente por número de docucmento
     * @param docIdentidad
     * @return
     */
    public Atraccion getAtraccionByDoc(String docIdentidad) {
    	this.docIdentidad = docIdentidad;
        Atraccion atraccion = null;
        int i, n;
        n = objetos.size();
        for (i = 0; i < n; i++) {
            atraccion = (Atraccion) objetos.elementAt(i);
            if(docIdentidad.equals(atraccion.getDocIdentidad())){
                return atraccion;
            }
        }
        if ( getRemote() ) {
        	atraccion = (Atraccion) objetos.elementAt(objetos.size() - 1);
        }
        return atraccion;
    }
}
