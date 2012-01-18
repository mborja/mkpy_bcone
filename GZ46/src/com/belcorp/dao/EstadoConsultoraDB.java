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
import com.belcorp.entidades.EstadoConsultora;
import com.belcorp.entidades.Nivel;
import com.belcorp.entidades.Pais;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;

public class EstadoConsultoraDB {
    private static final String metodoWeb = "BBWS18ObtenerEstadosConsultora"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    public static PersistentObject persist;
    private static final long IDSTORE = 0xa8cd1b767e7d8a1eL; // com.belcorp.entidades.EstadoConsultora
    private static Vector objetos;
    private Usuario usuario;

    /**
     * Constructor del DAO para los estados de la consultora
     */
   public EstadoConsultoraDB() {
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
    * Metod para configurar la informacion que se va a enviar al servicio web por POST
    */
   private void setUrl() {
	   	URL = Cadenas.URLBASE + "/" + metodoWeb;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
			+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT();
   }

   /**
    * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
    * @param node
    * @return si se puede formatear la información del XML a los objetos respectivos de estados de la consultora
    * @throws Exception
    */
   private static boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        objetos = new Vector();

        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            EstadoConsultora estadoConsultora = new EstadoConsultora();
            estadoConsultora.setId(fields[0]);
            estadoConsultora.setDescripcion(fields[1]);
            objetos.addElement(estadoConsultora);
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
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.BIS);
            httpConn.setRequestMethod(HttpConnection.POST);
            httpConn.setRequestProperty("Host", Cadenas.URIServer);
            httpConn.setRequestProperty("Connection", "close");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Content-Length", "" + DATA.length());
            os = httpConn.openOutputStream();
            os.write(DATA.getBytes());
            os.flush();
            int responseCode = httpConn.getResponseCode();
            if ( responseCode == HttpConnection.HTTP_OK ) {
                is = httpConn.openInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory
                                .newInstance();
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
            httpConn = null;
            resultado = false;
        } finally {
            try {
                os.close();
            } catch (Exception e) {
            }
            os = null;
            try {
                is.close();
            } catch (Exception e) {
            }
            is = null;
            try {
                httpConn.close();
            } catch (Exception e) {
            }
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

    /**
     * Busqueda de estados de consultoras por un filtro de codigo
     * @param codigo valor del codigo de consultora a buscar
     * @return String de la descripcion del estado de consultora
     */
    public String getDescripcionByCodigo(String codigo) {
        EstadoConsultora estado = null;
        int i, n;
        n = objetos.size();
        for (i = 0; i < n; i++) {
        	estado = (EstadoConsultora) objetos.elementAt(i);
            if(codigo.equals(estado.getId())){
                return estado.getDescripcion();
            }
        }
        return null;
    }
    
    /**
     * Busqueda de estados de consultoras por un filtro de descripcion
     * @param sDescrip valor de la descripcion de consultora a buscar
     * @return String del id del estado de consultora
     */
    public String getEstadoByNombre(String sDescrip) {
        EstadoConsultora estado = null;
        int i, n;
        n = objetos.size();
        for (i = 0; i < n; i++) {
        	estado = (EstadoConsultora) objetos.elementAt(i);
            if(sDescrip.equals(estado.getDescripcion())){
                return estado.getId();
            }
        }
        return null;
    }
}

