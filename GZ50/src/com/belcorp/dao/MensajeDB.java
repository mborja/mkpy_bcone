package com.belcorp.dao;

import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.belcorp.entidades.Mensaje;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;

public class MensajeDB {
    private static final String metodoWeb = "mensajes.xml"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x8a6480c9696106e5L; // com.belcorp.entidades.Mensaje    
    private Vector objetos;
    private Usuario usuario;

    /**
     * Constructor del DAO para los mensajes
     */
    public MensajeDB() {
        URL = "http://www.makipuray.com/belcorp/xml/" + metodoWeb;
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
     * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
     * @param node
     * @return
     * @throws Exception
     */
    private boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        objetos = new Vector();
        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
//            Dialog.inform("2.1 " + registro);
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Mensaje mensaje = new Mensaje();
            mensaje.setId(fields[0]);
            mensaje.setMensaje(fields[1]);
            objetos.addElement(mensaje);
        }
        persist.setContents(objetos);
        persist.commit();
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
        try {
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.BIS);
            httpConn.setRequestMethod(HttpConnection.GET);
//            Dialog.inform("0 " + URL);
            if ( httpConn.getResponseCode() == HttpConnection.HTTP_OK ) {
//            	Dialog.inform("1");
                is = httpConn.openInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
//                Dialog.inform("2");
                rootElement.normalize();
                if ( fillObjectos(rootElement.getChildNodes()) ) {
//                	Dialog.inform("3");
                    resultado = true;
                }
//                Dialog.inform("4");
                is.close();
                is = null;
            } else {
                resultado = false;
            }
            httpConn.close();
            httpConn = null;
        } catch(Exception ex) {
//        	Dialog.inform("5");
            try { is.close(); } catch(Exception e) { }
            is = null;
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
            resultado = false;
        } finally {
        }
        return resultado;
    }
    
    /**
     * Metodo publico, usado para obtener el vector con los objetos de la base de datos persistente
     * @return Vector con el listado de Objetos
     */
    public Vector getObjetos() {
        return objetos;
    }
}
