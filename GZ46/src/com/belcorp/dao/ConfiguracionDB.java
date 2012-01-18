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
import com.belcorp.entidades.Meta;
import com.belcorp.entidades.Pais;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;

public class ConfiguracionDB {
    private static final String metodoWeb = "BBWS01ObtenerConfiguracionInicial"; // BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x174dc4399057c508L; // com.belcorp.entidades.Configuracion
    private Configuracion conf;
    private Usuario usuario;

    /**
     * Constructor del DAO para las configuracion
     */
    public ConfiguracionDB() {
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
    	//setUrl();
        persist = PersistentStore.getPersistentObject( IDSTORE ); 
        try {
        	conf = (Configuracion) persist.getContents();
        } catch (Exception e) {
        	conf = null;
        }
        try {
            if ( conf == null) {
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
    		+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT();
    }
    
    private Configuracion fillOne() {
        conf = new Configuracion();
        conf.setTipoDocIdentidad("1");
        conf.setMaxDocIdentidad(8);
        conf.setMinDocIdentidad(8);
        conf.setMinBuscaNombre(3);
        conf.setMinBuscaCodigo(6);
        conf.setSimboloMoneda("S/. ");
        conf.setSimboloDecimal(".");
        conf.setSimboloMiles(",");
        conf.setCantidadDecimales(2);
        conf.setMostrarDocIdentidad("1");
        conf.setMostrarAsistencia("1");
    	return conf; 
    }
    
    /**
     * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
     * @param node
     * @return si se puede formatear la información del XML a los objetos respectivos de configuraciones
     * @throws Exception
     */
    private boolean fillObjectos(NodeList node) throws Exception {
    	int index = 0;
        conf = new Configuracion();
        if ( node.getLength()  == 0 ) {
        	conf = fillOne();
        } else {
        	try {
	            Node contactNode = node.item(1);
	            String registro = contactNode.getChildNodes().item(0).getNodeValue();
	            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
	            
	            conf.setTipoDocIdentidad(fields[index++]);
	            conf.setMaxDocIdentidad(Integer.parseInt("0" + fields[index++].trim()));
	            conf.setMinDocIdentidad(Integer.parseInt("0" + fields[index++].trim()));
	            conf.setMinBuscaNombre(Integer.parseInt("0" + fields[index++].trim()));
	            conf.setMinBuscaCodigo(Integer.parseInt("0" + fields[index++].trim()));
	            conf.setSimboloMoneda(fields[index++]);
	            conf.setSimboloDecimal(fields[index++]);
	            conf.setSimboloMiles(fields[index++]);
	            conf.setCantidadDecimales(Integer.parseInt(fields[index++]));
	            int n;
	            n = Integer.parseInt(fields[index++]); // cantidad de modulos 8
	            for(int i = 0; i < n; i++) {
	            	conf.getModulo().addElement(fields[index++]);
	            }
	            n = Integer.parseInt(fields[index++]); // cantidad de modulos 8
	            for(int i = 0; i < n; i++) {
	            	conf.getSubModulo().addElement(fields[index++]);
	            }
	            n = Integer.parseInt(fields[index++]); // cantidad de modulos 8
	            for(int i = 0; i < n; i++) {
	            	conf.getOpcion().addElement(fields[index++]);
	            }
	            conf.setMostrarDocIdentidad(fields[index++]);
	            conf.setMostrarAsistencia(fields[index++]);
        	} catch(Exception e) {
        		fillOne();
        	}
        }
        
        persist.setContents(conf);
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
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(is);
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                if (fillObjectos(rootElement.getChildNodes())) {
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
	        httpConn = null;
        }
        return resultado;
    }

    /**
     * Metodo publico, usado para obtener el objeto de configuracion de la base de datos persistente
     * @return
     */
    public Configuracion getObjeto() {
        return conf;
    }
     
}
