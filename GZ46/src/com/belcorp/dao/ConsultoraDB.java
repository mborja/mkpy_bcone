package com.belcorp.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.compress.GZIPInputStream;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.belcorp.entidades.Concurso;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.HistorialAnotacion;
import com.belcorp.entidades.PedidosVenta;
import com.belcorp.entidades.PedidosxMarca;
import com.belcorp.entidades.PosVenta;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;

public class ConsultoraDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "BBWS09ObtenerInformacionConsultora"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebAct0 = "BBWS10ActualizarConsultoraDato"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebAct1 = "BBWS11ActualizarConsultoraMeta"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebAct2 = "BBWS12ActualizarConsultoraComentario"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebVExitosa = "BBWS13RegistraVisita"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebVNExitosa = "BBWS13RegistraVisita"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebBusca = "BBWS21ObtenerConsultoraBusqueda"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebBuscaLista = "BBWS20ObtenerConsultoraGenerarLista"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static final String metodoWebBuscaUna = "BBWS09ObtenerInformacionConsultora"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0xd4644e21e04152f1L; // com.belcorp.entidades.Consultora        
    private Vector objetos;
    private Usuario usuario;
    private String idSeccion = null;
    private String clasificacion = null;
    private String idNombre = null;
    private String idDocIdent = null;
    private String idCodConsultora = null;
    private String idNivel = null;
    private String idEstado = null;
    private String idDeuda = null;
	private String estadoPedido = null;
	private String visitada = null;

    /**
     * Constructor del DAO para las consultoras
     */
    public ConsultoraDB() {
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
    	setUrl1();
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
	 * Metodo privado, para obtener la información de las consultoras a enviar por POST al servicio web
	 */
    private void setUrl1() {
    	URL = Cadenas.URLBASE + "/" + metodoWeb;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    		+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" + usuario.getZonaRegionPais() 
			+ "&codConsultora=&ChrAnoCampana=" + usuario.getCampana();
    }
    
    /**
     * Metodo privado, para obtener la información de una consultora a enviar por POST al servicio web
     * @param seccion filtro de seccion a la que pertenece
     * @param nombre filtro de nombre de la consultora a buscar
     * @param docIdent filtro del documento de identidad a buscar
     * @param codigoConsultora filtro de codigo de consultora a buscar
     */
    private void setUrl2(String seccion, String nombre, String docIdent, String codigoConsultora) {
    	URL = Cadenas.URLBASE + "/" + metodoWebBusca;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    		+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() 
			+ "&CodigoConsultora=" + codigoConsultora
			+ "&DocumentoIdentidad=" + docIdent
			+ "&NombreConsultora=" + nombre
			+ "&CodigoSeccion=" + seccion;
    }

    /**
     * Metodo privado, para obtener la información de busqueda de consultoras a enviar por POST al servicio web
     * @param campana filtro de campana 
     * @param seccion filtro de seccion a la que pertenece las consultoras
     * @param idNivel filtro del nivel de las consultoras a busacr
     * @param idEstado filtro del estado de las consultoras
     * @param deuda filtro de si tiene deuda o no
     * @param estadoPedido filtro de estado de las consultoras a buscar
     */
    private void setUrl3(String campana, String seccion, String idNivel, String idEstado, String deuda, String estadoPedido) {
    	URL = Cadenas.URLBASE + "/" + metodoWebBuscaLista;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
			+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() 
			+ "&Seccion=" + seccion
			+ "&Nivel=" + idNivel
			+ "&Estado=" + idEstado
			+ "&Deuda=" + deuda
			+ "&EstadoPedido=" + estadoPedido
			+ "&chrAnoCampana=" + campana;
	   	
    }

    /**
     * Metodo privado, para obtener la información de una consultora a enviar por POST al servicio web
     * @param codId codigo interno de la consultora a buscar, trate todo la información de la consultora 
     */
    private void setUrl4(String codId) {
    	URL = Cadenas.URLBASE + "/" + metodoWebBuscaUna;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    		+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" + usuario.getZonaRegionPais() 
			+ "&codConsultora=" + codId + "&ChrAnoCampana=" + usuario.getCampana();
    }

    /**
     * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
     * @param node
     * @return si se puede formatear la información del XML a los objetos respectivos de Consultora
     * @throws Exception
     */
    private boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        //mkpyStatusProgress progress = new mkpyStatusProgress("Consultoras...", 0, n, GaugeField.PERCENT);   
        objetos = new Vector();
        //progress.open();
        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            objetos.addElement( agregarConsultora(fields));
            //progress.setProgress(i);
        }
        //progress.close();
        sortConsultoras();
        persist.setContents(objetos);
        persist.commit();
        return true;
    }
    
    /**
     * Metodo private, para ordenar a las consultoras pro nombre
     */
    private void sortConsultoras() {
        int n, i, j;
        if (objetos == null) {
        	n = 0;
        } else {
            n = objetos.size();
        }
        for ( i = 0; i < n; i++ ) {
            for ( j = 0; j < n; j++ ) {
            	Consultora consI = (Consultora) objetos.elementAt(i); 
            	Consultora consJ = (Consultora) objetos.elementAt(j);
            	try {
                	if ( consI.getNombre().compareTo(consJ.getNombre()) <= 0 ) { // I es menor que J
                		objetos.setElementAt(consI, j);
                		objetos.setElementAt(consJ, i);
                	}
            	} catch(Exception e) {
            		
            	}
            }
        }
    }

    /**
     * Metodo public, usado para la llamara al servicio web de listado de todas las consultoras por HTTP POST
     * @return si la conexion HTTP es correcta
     */
	public boolean getRemote() {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        //GZIPInputStream is = null;
        try {
            //httpConn.setRequestProperty("Accept-Encoding", "gzip");
        	setUrl1();
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
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
            	//is = new GZIPInputStream(httpConn.openInputStream());
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
    public Vector getObjetos() {
        return objetos;
    }
    
    /**
     * Metodo public, usado para buscar consultoras en la base de datos persistente segun criterios ingresados
     * @return Vector con la lista de coincidencias en la busqueda
     */
    public Vector getConsultoras() {
        int i, n;
        Vector resultado = new Vector();
        n = objetos.size();
        for ( i = 0; i < n; i++ ) {
            boolean criterio1 = true, criterio2 = true,criterio3 = true,criterio4 = true,criterio5 = true,criterio6 = true;
            Consultora consultora = (Consultora) objetos.elementAt(i);
            if ( idSeccion != null ) {
                if (idSeccion.equals("-1") || consultora.getIdSeccion().equals(idSeccion) ) {
                    criterio1 = true;
                } else {
                    criterio1 = false;
                }
            }
            if ( clasificacion != null ) {
                if ( consultora.getClasificacionMetodologica().equals(clasificacion) ) {
                    criterio2 = true;
                } else {
                    criterio2 = false;
                }
            }
            if ( idNombre != null ) {
            	
                if (idNombre.equals("")|| consultora.getNombre().toLowerCase().indexOf(idNombre.toLowerCase())!=-1 ) {
                    criterio3 = true;
                } else {
                    criterio3 = false;
                }
            }
            if ( idDocIdent != null ) {
                if (idDocIdent.equals("")|| consultora.getDocIdentidad().indexOf(idDocIdent)!=-1 ) {
                    criterio4 = true;
                } else {
                    criterio4 = false;
                }
            }
            if ( idCodConsultora != null ) {
                if (idCodConsultora.equals("")|| consultora.getCodigo().indexOf(idCodConsultora)!=-1 ) {
                    criterio5 = true;
                } else {
                    criterio5 = false;
                }
            }
            if ( visitada != null ) {
                if (!visitada.equals(consultora.getVisitada()) ) {
                    criterio6 = true;
                } else {
                    criterio6 = false;
                }
            }
            if ( criterio1 && criterio2 && criterio3 && criterio4 && criterio5 && criterio6) {
                resultado.addElement(consultora);
            }
        }
        return resultado;
    }

    /**
     * Metodo public, usado para la llamara al servicio web de busqueda de consultoras por HTTP POST
     * @return si la conexion HTTP es correcta
     */
    public Vector getRemoteBusqueda(String seccion, String nombre, String docIdent, String codigoConsultora) {
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        Vector objResultado = null;
        try {
        	setUrl2(seccion, nombre, docIdent, codigoConsultora);
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
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
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                objResultado = fillObjectosBusqueda(rootElement.getChildNodes(), seccion, nombre, docIdent, codigoConsultora);
                is.close();
                is = null;
            } else {
            	return null;
            }
            httpConn.close();
            httpConn = null;
        } catch(Exception ex) {
            try { is.close(); } catch(Exception e) { }
            is = null;
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
        } 
        return objResultado;
    }
    
    /**
     * Metodo public, usado para la llamara al servicio web de generar lista de consultoras por HTTP POST
     * @return si la conexion HTTP es correcta
     */
    public Vector getRemoteBusqueda(String campana, String seccion, String idNivel, String idEstado, String deuda, String estadoPedido) {
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        Vector objResultado = null;
        try {
        	setUrl3(campana, seccion, idNivel, idEstado, deuda, estadoPedido);
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
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
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                objResultado = fillObjectosBusqueda(rootElement.getChildNodes(),seccion, idNivel, idEstado, deuda, estadoPedido);
                is.close();
                is = null;
            } else {
            	return null;
            }
            httpConn.close();
            httpConn = null;
        } catch(Exception ex) {
            try { is.close(); } catch(Exception e) { }
            is = null;
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
        } 
        return objResultado;
    }
    
    /**
     * Metodo para llenar un vector con la validacion del XML del servicio web de generar lista de consultoras
     * @param node
     * @param seccion
     * @param idNivel
     * @param idEstado
     * @param deuda
     * @param estadoPedido
     * @return
     * @throws Exception
     */
    private Vector fillObjectosBusqueda(NodeList node, String seccion, String idNivel, String idEstado, String deuda, String estadoPedido) throws Exception {
        int index;
        int n = node.getLength();
        Vector objResultado = new Vector();
        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Consultora consultora = new Consultora();
            index = 0;
         
            consultora.setId(fields[index++]);
            consultora.setCodigo(fields[index++]);
            consultora.setNombre(fields[index++]);
            consultora.setSaldo(fields[index++]);
            index++;
            consultora.setOperacion(fields[index++]);
            objResultado.addElement(consultora);
        }
        return objResultado;
     }

    /**
     * Metodo para llenar un vector con la validacion del XML del servicio web de busqueda de consultoras
     * @param node
     * @param seccion
     * @param nombre
     * @param docIdent
     * @param codigoConsultora
     * @return
     * @throws Exception
     */
    private Vector fillObjectosBusqueda(NodeList node, String seccion, String nombre,
    		String docIdent, String codigoConsultora) throws Exception {
        int index;
        int n = node.getLength();
        Vector objResultado = new Vector();
        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Consultora consultora = new Consultora();
            index = 0;
         
            consultora.setId(fields[index++]);
            index++; //TODO: Codigo de zona
            consultora.setCodigo(fields[index++]);
            consultora.setNombre(fields[index++]);
            consultora.setSaldo(fields[index++]);
            consultora.setIdSeccion(fields[index++]);
            objResultado.addElement(consultora);
        }
        return objResultado;
     }

    /**
     * Metodo public, usado para la llamara al servicio web de busqueda de una consultora por HTTP POST
     * @return si la conexion HTTP es correcta
     */
    public Consultora getRemoteConsultora(String codId) {
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        Consultora consultora = null;
        try {
        	setUrl4(codId);
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
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
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                consultora = fillConsultora(rootElement.getChildNodes());
                is.close();
                is = null;
            } else {
                return  null;
            }
            httpConn.close();
            httpConn = null;
        } catch(Exception ex) {
            try { is.close(); } catch(Exception e) { }
            is = null;
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
        }
        return consultora;
    }   
    
    /**
     * Metodo para adicionar una consultora a la base de datos persistente desde el XML del servicio web
     * @param node
     * @return el objeto de la consultora adicionado
     * @throws Exception
     */
    private Consultora fillConsultora(NodeList node) throws Exception {
        int n = node.getLength();
        int i = 1;
        //for (int i = 1; i < n; i = i + 2) {
        if ( n >= 1 ) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();

            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Consultora consultora = null;
            consultora = agregarConsultora(fields);
            //TODO: MB Revisar si es necesario
            // ¿Por que se agrega un elemento?
            //objetos.addElement( consultora );
            sortConsultoras();
            persist.commit();
        	return consultora;
        }
        return null;
    }
    
    /**
     * Metodo para filtrar las consultaras en la base de datos persistente segun parametros
     * @return
     */
    public Vector getConsultorasGenerar() {
        int i, n;
        Vector resultado = new Vector();
        n = objetos.size();
        for ( i = 0; i < n; i++ ) {
            boolean criterio1 = true, criterio2 = true,
            criterio3 = true,criterio4 = true, criterio5 = true;
            Consultora consultora = (Consultora) objetos.elementAt(i);
            if ( idSeccion != null ) {
                if (idSeccion.equals("-1") || consultora.getIdSeccion().equals(idSeccion) ) {
                    criterio1 = true;
                } else {
                    criterio1 = false;
                }
            }
            if ( idNivel != null ) {
                if (idNivel.equals("-1") || consultora.getIdNivel().equals(idNivel) ) {
                    criterio2 = true;
                } else {
                    criterio2 = false;
                }
            }
            if ( idEstado != null ) {
            	
                if (idEstado.equals("-1") || consultora.getEstadoConsultora().equals(idEstado) ) {
                    criterio3 = true;
                } else {
                    criterio3 = false;
                }
            }
            if ( idDeuda != null ) {
            	String opcion;
            	double valor;
            	valor = Double.parseDouble(consultora.getSaldo());
            	if(valor>0)
            		opcion ="Sí";
            	else
            		opcion ="No";

                if (idDeuda.equals("-1")||  opcion.equals(idDeuda) ) {
                    criterio4 = true;
                } else {
                    criterio4 = false;
                }

            }
            if ( estadoPedido != null ) {
            	String opcion;
            	/*valor = consultora.getPasoPedido();
            	if (valor.equals("1")) 
            		opcion ="Sí";
            	else
            		opcion ="No";
            	*/
            	opcion = consultora.getPasoPedido();
                if (estadoPedido.equals("-1")|| opcion.equals(estadoPedido) ) {
                    criterio5 = true;
                } else {
                    criterio5 = false;
                }

            }

            if ( criterio1 && criterio2 && criterio3 && criterio4 && criterio5 ) {
                resultado.addElement(consultora);
            }
        }
        return resultado;
    }

    /**
     * Metodo para filtrar las consultoras que no tienen una visita existosa registrada
     * @param filtro Vector con la lista de consultoras a filtrar con el criterio de no tener una visita exitosa
     * @return Vector con las consultoras filtradas que no tienen visita exitosa
     */
    public Vector getCantidadPorVisitar(Vector filtro) {
        Vector resultado = new Vector();
        if ( filtro != null ) {
            int n = filtro.size();
            for ( int i = 0; i < n; i++ ) {
                Consultora consultora = (Consultora) filtro.elementAt(i);
                if ( !consultora.getVisitada().equals("VE") ) {
                    resultado.addElement(consultora);
                }
            }
        }
        return resultado;
    }

    /**
     * Metodo generico para adicionar a la base de datos persistente seguin los campos del XML
     * @param fields
     * @return
     */
    private Consultora agregarConsultora(String[] fields) {
    	int index;
       int m;
       Consultora consultora = new Consultora();
       index = 0;

       //Dialog.inform("00 " + fields[0]);
       consultora.setId(fields[index++]);
       consultora.setNombre(fields[index++]);
       consultora.setDireccion(fields[index++]);
       consultora.setCodigo(fields[index++]);
       consultora.setDocIdentidad(fields[index++]);
       consultora.setTelefono1(fields[index++]);
       consultora.setTelefono2(fields[index++]);
       consultora.setTelefono3(fields[index++]);
       consultora.setEmail(fields[index++]);
       consultora.setCampanaIngreso(fields[index++]);
       consultora.setAutorizadoPasarPedido(fields[index++]);
       consultora.setClasificacionMetodologica(fields[index++]);
       consultora.setIdSeccion(fields[index++]);
       consultora.setAsisteCompartamos(fields[index++]);
       consultora.setSaldo(fields[index++]);
       consultora.setIdNivel(fields[index++]);
       consultora.setEstadoConsultora(fields[index++]);
       consultora.setPasoPedido(fields[index++]);
       consultora.setGananciaUltimaCampana(Double.parseDouble( fields[index++] ));
       consultora.setMontoUltimoPedido(fields[index++]);
       consultora.setOperacion("");

       //INICIO: Pedidos
       m = Integer.parseInt(fields[index++]);
       for (int j = 0; j < m; j++) {
           PedidosVenta venta = new PedidosVenta();
           venta.setCampana(fields[index++]);
           venta.setVenta( Double.parseDouble(fields[index++]) );
           venta.setGanancia( Double.parseDouble(fields[index++]) );
           venta.setAsistencia(fields[index++]);
           consultora.getPedidos().addElement(venta);
       }
       consultora.sortPedidos();
       //FIN: Pedidos  
       //INICIO: Pedidos x marca
       m = Integer.parseInt(fields[index++]);
       for (int j = 0; j < m; j++) {
           PedidosxMarca ventaxmarca = new PedidosxMarca();
           ventaxmarca.setCampana(fields[index++]);
           ventaxmarca.setVentaEbel( Double.parseDouble(fields[index++]) );
           ventaxmarca.setVentaCyzone( Double.parseDouble(fields[index++]) );
           ventaxmarca.setVentaEsika( Double.parseDouble(fields[index++]) );
           ventaxmarca.setVentaOtros(Double.parseDouble(fields[index++]));
           ventaxmarca.setPorcentajeVentaEbel( Double.parseDouble(fields[index++]) );
           ventaxmarca.setPorcentajeVentaCyzone( Double.parseDouble(fields[index++]) );
           ventaxmarca.setPorcentajeVentaEsika( Double.parseDouble(fields[index++]) );
           ventaxmarca.setPorcentajeOtros( Double.parseDouble(fields[index++]) );

           consultora.getPedidosxMarca().addElement(ventaxmarca);
       }
       consultora.sortPedidosxMarca();
       //FIN: Pedidos x marca
       index++; //TODO: mensaje concuros
       //INICIO: Consursos
       m = Integer.parseInt(fields[index++]);
       for (int j = 0; j < m; j++) {
         Concurso concurso = new Concurso();
         concurso.setCodigo(fields[index++]);
         concurso.setDescripcion(fields[index++]);
         concurso.setPuntaje(fields[index++]);
         concurso.setResultado(fields[index++]);
         concurso.setNivel(fields[index++]);
         concurso.setPremio(fields[index++]);
         concurso.setCampanaInicio(fields[index++]);
         concurso.setCampanaFin(fields[index++]);
         consultora.getConcursos().addElement(concurso);
       }
       //FIN: Consursos
       //INICIO: postventa
       m = Integer.parseInt(fields[index++]);
       for (int j = 0; j < m; j++) {
           PosVenta posventa = new PosVenta();
           posventa.setCodigo(fields[index++]);
           posventa.setProducto(fields[index++]);
           posventa.setUnidad(fields[index++]);
           posventa.setOperacion(fields[index++]);
           posventa.setCampanaOrigen(fields[index++]);
           posventa.setCampanaProceso(fields[index++]);
           posventa.setResultado(fields[index++]);
           posventa.setMotivo(fields[index++]);
           consultora.getPostVentas().addElement(posventa);
       }
       //FIN: postventa
       index++; //TODO: mensaje estado meta
       consultora.setIdMeta(fields[index++]);
       String meta = fields[index++];
       if ( meta.trim().equals("") ) {
           consultora.setMontoMeta(Double.parseDouble("0" + meta));
       } else {
           consultora.setMontoMeta(Double.parseDouble(meta));
       }
       consultora.setDescripcionMeta(fields[index++]);
       consultora.setAsistenciaTalleres(fields[index++]);
       index++; //TODO: mensaje etado dupla
              
       consultora.setDatosDupla(fields[index++]);
       consultora.setAnotaciones(fields[index++]);

       index++; //TODO: noticia 1
       index++; //TODO: noticia 2

       String visitaRegistrada = fields[index++];
       if ( visitaRegistrada.equals("1") ) {
           consultora.setVisitada("VE");
       } else {
           consultora.setVisitada("");
       }
       //INICIO: historial de anotaciones
       try {
           m = Integer.parseInt(fields[index++]);
           for (int j = 0; j < m; j++) {
        	   HistorialAnotacion historial =  new HistorialAnotacion();
        	   historial.setFechaHora(fields[index++]);
        	   historial.setAnotacion(fields[index++]);
               consultora.getHistorialAnotaciones().addElement(historial);
           }
       } catch(Exception e) {
    	   //
       }
       //FIN: historial de anotaciones

       consultora.setMotivoNoVista(null);
       consultora.setModificada("");
       consultora.setAnotada("");
       consultora.setConMeta("");

       return consultora;
     }

    /**
     * Metodo para preparara la informacion a enviar por POST para actualizar o registrar, dependiendo de la accion, información de la consultora
     * @param accion 
     * 	0 = actualizar datos de consultora
     * 	1 = Registrar meta de la consultora
     *  2 = Actualizar comentario
     *  3 = Registrar visita exitosa
     *  4 = Registrar visita no exitosa 
     * @param cons Objeto de la consulta a actualizar o registrar
     */
    private void setURLA(int accion, Consultora cons) {
    	switch(accion) {
        	case 0: 
        		URL = Cadenas.URLBASE + "/" + metodoWebAct0;
            	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
            		+ "&IDAPP=" + Sistema.getIdapp() 
	    			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
	    			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() 
	    			+ "&IdRol=" + Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" 
	    			+ usuario.getZonaRegionPais() + "&IdCodigoConsultora=" + cons.getId()
	    			+ "&CodigoConsultora=" + cons.getCodigo() + "&Telefono01=" + cons.getTelefono1()
	    			+ "&Telefono02=" + cons.getTelefono2() + "&Telefono03=" + cons.getTelefono3()
	    			+ "&Email=" + cons.getEmail() + "&FechaHora=" + Fechas.dateToString();
        		break;
        	case 1: 
        		URL = Cadenas.URLBASE + "/" + metodoWebAct1;
            	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
            		+ "&IDAPP=" + Sistema.getIdapp() 
	    			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
	    			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() 
	    			+ "&IdRol=" + Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" 
	    			+ usuario.getZonaRegionPais() + "&IdCodigoConsultora=" + cons.getId()
	    			+ "&CodigoConsultora=" + cons.getCodigo() + "&IdTipoLogro=" + cons.getIdMeta()
	    			+ "&MontoLogro=" + cons.getMontoMeta() + "&DescripcionLogro=" + cons.getDescripcionMeta()
	    			+ "&FechaHora=" + Fechas.dateToString();
        		break;
        	case 2: 
        		URL = Cadenas.URLBASE + "/" + metodoWebAct2;
            	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
            		+ "&IDAPP=" + Sistema.getIdapp() 
	    			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
	    			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() 
	    			+ "&IdRol=" + Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" 
	    			+ usuario.getZonaRegionPais() + "&IdCodigoConsultora=" + cons.getId()
	    			+ "&CodigoConsultora=" + cons.getCodigo() + "&Comentario=" + cons.getAnotaciones()
	    			+ "&FechaHora=" + Fechas.dateToString();
        		break;
        	case 4:
        		URL = Cadenas.URLBASE + "/" + metodoWebVExitosa;
            	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    				+ "&IDAPP=" + Sistema.getIdapp() 
	    			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
	    			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() 
	    			+ "&IdRol=" + Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" 
	    			+ usuario.getZonaRegionPais() + "&IdCodigoConsultora=" + cons.getId()
	    			+ "&CodigoConsultora=" + cons.getCodigo() + "&IdTipoClasificacionMetodologia=" + cons.getClasificacionMetodologica()
	    			+ "&IdMotivo=0&CampanaActual=" + usuario.getCampana()
	    			+ "&FechaHora=" + Fechas.dateToString();
        		break;
        	case 5:
        		URL = Cadenas.URLBASE + "/" + metodoWebVNExitosa;
            	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
    				+ "&IDAPP=" + Sistema.getIdapp() 
	    			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
	    			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() 
	    			+ "&IdRol=" + Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&CodigoZona=" 
	    			+ usuario.getZonaRegionPais() + "&IdCodigoConsultora=" + cons.getId()
	    			+ "&CodigoConsultora=" + cons.getCodigo() + "&IdTipoClasificacionMetodologia=" + cons.getClasificacionMetodologica()
	    			+ "&IdMotivo=" + cons.getIdMotivosNoVisita() + "&CampanaActual=" + usuario.getCampana()
	    			+ "&FechaHora=" + Fechas.dateToString();
        		break;
    	}
    }
    
    /**
     * 
     * @param consultora
     * @param accion:
     * 0 = actualizacion de datos de la consultora (telefon1,2,3 email
     * 1 = actualizacion de meta
     * 2 = actualizacion de anotaciones
     * 3 = visita exitosa
     * 4 = visita no exitosa
     * @return
     */
    public boolean sendUpdate(Consultora consultora, int accion) {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
        	setURLA(accion, consultora);
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
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
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                if ( responseUpdate(rootElement.getChildNodes()) ) {
                    resultado = true;
                } else {
                	resultado = false;
                }
            	switch(accion) {
		        	case 0: 
		        		if ( resultado )
		        			consultora.setModificada("ME");
		        		else
		        			consultora.setModificada("MNE");
		        		break;
		        	case 1: 
		        		if ( resultado )
		        			consultora.setConMeta("ME");
		        		else
		        			consultora.setConMeta("MNE");
		        		break;
		        	case 2: 
		        		if ( resultado )
		        			consultora.setAnotada("AE");
		        		else
		        			consultora.setAnotada("ANE");
		        		break;
		        	case 4: 
		        		if ( resultado )
		        			consultora.setVisitada("VE");
		        		else
		        			consultora.setVisitada("VNE");
		        		break;
		        	case 5: 
		        		if ( resultado )
		        			consultora.setVisitada("NVE");
		        		else
		        			consultora.setVisitada("NVNE");
		        		break;
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
    
    public void commit() {
    	persist.commit();
    }

//    private boolean updateConsultora(Consultora cons) {
//    	int i, n;
//    	n = objetos.size();
//    	for ( i = 0; i < n; i++ ) {
//    		Consultora tmp = (Consultora) objetos.elementAt(i);
//    		if ( tmp.getId().equals(cons.getId()) ) {
//                //objetos.setElementAt(cons, i);
//                //persist.setContents(objetos);
//                persist.commit();
//    			return true;
//    		}
//    	}
//    	return false;
//    }

    private boolean responseUpdate(NodeList node) {
        Node contactNode = node.item(1);
        String registro = contactNode.getChildNodes().item(0).getNodeValue();        
        if ( registro.equals(RESPONSE_OK) ) {
        	return true;
        } else {
        	return false;
        }
    }

    public void setIdSeccion(String seccion) {
        this.idSeccion = seccion;
    }
    
    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }	

	public void setIdNombre(String idNombre) {
		this.idNombre = idNombre;
	}

	public String getIdNombre() {
		return idNombre;
	}

	public void setIdDocIdent(String idDocIdent) {
		this.idDocIdent = idDocIdent;
	}

	public String getIdDocIdent() {
		return idDocIdent;
	}

	public void setIdCodConsultora(String idCodConsultora) {
		this.idCodConsultora = idCodConsultora;
	}

	public String getIdCodConsultora() {
		return idCodConsultora;
	}

	public void setIdNivel(String idNivel) {
		this.idNivel = idNivel;
	}

	public String getIdNivel() {
		return idNivel;
	}

	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}

	public String getIdEstado() {
		return idEstado;
	}
	public String getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(String idDeuda) {
		this.idDeuda = idDeuda;
	}
	public void setVisitada(String visitada) {
		this.visitada = visitada;
	}

	public String getVisitada() {
		return visitada;
	}

	public String getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	
}
