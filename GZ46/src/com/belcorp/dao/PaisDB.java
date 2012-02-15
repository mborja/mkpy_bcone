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

import com.belcorp.entidades.Pais;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;

public class PaisDB {
    private static final String metodoWeb = "BBWS22ObtenerPais"; // BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x88783dac879b9bf0L; // com.belcorp.entidades.Pais    
    private static Vector objetos;
    private Usuario usuario;

    /**
     * Constructor del DAO para los Paises
     */
    public PaisDB() {
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
     * Metodo para configurar la informacion que se va a enviar al servicio web por POST
     */
    private void setUrl() {
    	URL = Cadenas.URLBASE + "/" + metodoWeb;
        
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
			+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdRol=" + Sistema.getTipoRol() 
			+ "&GMT=" + Fechas.getGMT();
    }
   
    /**
     * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
     * @param node
     * @return si se puede formatear la información del XML a los objetos respectivos de los Paises
     * @throws Exception
     */
    private boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        objetos = new Vector();

        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Pais pais = new Pais();
            pais.setFuerzaVenta(fields[0]);
            pais.setId(fields[1]);
            pais.setDescripcion(fields[2].trim());
            objetos.addElement(pais);
        }
        return true;   
    }

    /**
     * Metodo privada, usado para la llamara al servicio web por HTTP POST
     * @return si la conexion HTTP es correcta
     */
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
                Document document = builder.parse(is);
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                if (fillObjectos(rootElement.getChildNodes())) {
                    persist.setContents(objetos);
                    persist.commit();
                    resultado = true;
                }
                is.close();
                is = null;
            } else {
                resultado = false;
            }
            httpConn.close();
            httpConn = null;
        } catch (Exception ex) {
            try {
                is.close();
            } catch (Exception e) {
            }
            is = null;
            try {
                httpConn.close();
            } catch (Exception e) {
            }
            httpConn = null;
            resultado = false;
        } finally {
        }
        return resultado;
    }

    /**
     * Metodo para adicionar los paises en forma fija cuando no se pueda cargar por conectividad
     */
    private void fillFix() {
    	objetos = new Vector();
    	Pais item1 = new Pais();
    	item1.setId("16");
    	item1.setFuerzaVenta("FS");
    	item1.setDescripcion("CHILE");
    	objetos.addElement(item1);
    	
    	Pais item2 = new Pais();
    	item2.setId("13");
    	item2.setFuerzaVenta("FS");
    	item2.setDescripcion("COLOMBIA");
    	objetos.addElement(item2);

    	Pais item3 = new Pais();
    	item3.setId("2");
    	item3.setFuerzaVenta("FS");
    	item3.setDescripcion("PERU");
    	objetos.addElement(item3);
    
    	Pais item4 = new Pais();
    	item4.setId("0154");
    	item4.setFuerzaVenta("FL");
    	item4.setDescripcion("MEXICO");
    	objetos.addElement(item4);
    	
        persist.setContents(objetos);
        persist.commit();
    	
    }
    
    /**
     * Metodo publico, usado para obtener el vector con los objetos de la base de datos persistente
     * @return Vector con el listado de Objetos
     */
    public Vector getObjetos() {
    	if ( objetos == null ) {
    		fillFix();
    	} else if ( objetos.size() == 0 ) {
    		fillFix();
    	}
        return objetos;
    }
}

