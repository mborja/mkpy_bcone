package com.belcorp.dao;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.i18n.SimpleDateFormat;
//solo 5.0 import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.belcorp.entidades.Usuario;
import com.belcorp.ui.MenuOpciones;
import com.belcorp.ui.MetodosGlobales;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;

public class UsuarioDB {
	private static final String RESPONSE_OK = "1";
    private static final String metodoWeb = "BBWS02ObtenerUsuario";
    private static final String metodoWebClave = "BBWS03ObtenerPassword";
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x1cffddff9d0c5dc6L; // com.belcorp.entidades.Usuario    
    private Usuario usuario;
    //private  int intentosSincroniza = 0;    
    private String codigo;
    private String clave;
    private String idPais;
    private String idFuerza;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    private String versionApp, versionAppUsr;
    
    /**
     * Constructor del DAO para el Usuario
     */
    public UsuarioDB() {
    	//setUrl();
        persist = PersistentStore.getPersistentObject( IDSTORE ); 
        try {
            usuario = (Usuario) persist.getContents();
        } catch (Exception e) {
            usuario = null;
        }
        try {
            if ( usuario == null) {
                //usuario = new Usuario();
                //persist.setContents(usuario);
                persist.commit();
            }
        } catch (Exception e) {
        }
    }
    
    /**
     * Metodo para configurar la informacion que se va a enviar al servicio web por POST para la validacion
     */
    private void setUrl() {
        URL = Cadenas.URLBASE + "/" + metodoWeb;
        String mPais, mFuerza;
        //usuario = this.getUsuario();
    	mPais = this.getIdPais();
    	mFuerza = this.getIdFuerza();
        
        DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() 
    		+ "&IMSI=" + Sistema.getImsi() + "&IDAPP=" + Sistema.getIdapp() 
			//+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&IdEmpresa=" + mFuerza + "&IdPais=" + mPais 
			+ "&Zonaregionpais=&NombreUsuario=" + codigo + "&IdRol=" 
			+ Sistema.getTipoRol(mFuerza) + "&GMT=" + Fechas.getGMT() + "&Clave=" + clave;
        //Dialog.inform(DATA);
    }
    
    /**
     * Metodo para configurar la informacion que se va a enviar al servicio web por POST para el cambio de clave
     */
    private void setURLClave(String codigo) {
        URL = Cadenas.URLBASE + "/" + metodoWebClave;
        String mPais, mFuerza;
        usuario = this.getUsuario();
        if ( usuario == null  ) {
        	mPais = this.getIdPais();
        	mFuerza = this.getIdFuerza();
        } else {
        	mPais = usuario.getIdPais();
        	mFuerza = usuario.getIdEmpresa();
        }
        
        DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() 
        	+ "&IMSI=" + Sistema.getImsi() + "&IDAPP=" + Sistema.getIdapp() 
			//+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&IdEmpresa=" + mFuerza + "&IdPais=" + mPais 
			+ "&Zonaregionpais=&NombreUsuario=" + codigo + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&Clave=" + clave;
    }
    
    /**
     * Metodo que permite actualizar el objeto de usuario en la base de datos persistente
     * @return
     */
    private boolean actualizar() {
    	try {
            //persist.setContents(usuario);
            persist.commit();
    		return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
            this.codigo = codigo;
    }

    public String getClave() {
            return clave;
    }

    public void setClave(String clave) {
            this.clave = clave;
    }

    public String getIdPais() {
            return idPais;
    }

    public void setIdPais(String idPais) {
            this.idPais = idPais;
    }
    
    public String getIdFuerza() {
		return idFuerza;
	}

	public void setIdFuerza(String idFuerza) {
		this.idFuerza = idFuerza;
	}

    /**
     * Metodo privado usado para registrar la información del servicio web en XML a la base de datos persistente
     * @param node
     * @return si se puede formatear la información del XML a el objeto de usuario
     * @throws Exception
     */
	private boolean fillObjectos(NodeList node) throws Exception {
        Node contactNode = node.item(1);
        String registro = contactNode.getChildNodes().item(0).getNodeValue();
        //Dialog.inform(registro);
        String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
        usuario = new Usuario();
        usuario.setCodigo(getCodigo());
        usuario.setClave(getClave());
        usuario.setNombre(fields[0]);
        usuario.setZonaRegionPais(fields[1]);
        usuario.setCampana(fields[2]);
        if( fields[3].equals("1") ) {
            usuario.setPuedeAutoValidar(true);
        } else {
            usuario.setPuedeAutoValidar(false);
        }
        
        usuario.setFechaHoraServidor( fields[4] );
        usuario.setFechaHoraLocal(Fechas.dateToString());
        usuario.setVersionApp(fields[5]);
        usuario.setUrlWS(fields[6]);
        usuario.setTiempoAutoValidacion(Long.parseLong(fields[7]));

        usuario.setTiempoDatosConsultora(Long.parseLong(fields[8]));
        usuario.setTiempoSaldosConsultora(Long.parseLong(fields[9]));
        if ( Sistema.getPerfil() == Sistema.perfilGGZZ ) {
            usuario.setTiempoTotales(Long.parseLong(fields[10]));
        }
        if ( Sistema.getPerfil() == Sistema.perfilGGRR ) {
            usuario.setTiempoTotales(Long.parseLong(fields[11]));
        }
        if ( Sistema.getPerfil() == Sistema.perfilDV ) {
            usuario.setTiempoTotales(Long.parseLong(fields[12]));
        }

        usuario.setFechaHoraUltimaSincronizacionConsultora(Fechas.dateToString());
        usuario.setFechaHoraUltimaSincronizacionSaldos(Fechas.dateToString());
        usuario.setFechaHoraUltimaSincronizacionOtros(Fechas.dateToString());
        usuario.setFechaHoraUltimaSincronizacionTotales(Fechas.dateToString());
        usuario.setEstadoUltimaSincronizacionConsultora(false);
        usuario.setEstadoHoraUltimaSincronizacionSaldos(false);
        usuario.setEstadoHoraUltimaSincronizacionOtros(false);
        usuario.setEstadoHoraUltimaSincronizacionTotales(false);
        usuario.setUsuarioVersion(Sistema.getVersion());
        
        //usuario.setConfiguracion(conf);
        usuario.setImsi(Sistema.getImsi());
        persist.setContents(usuario);
        persist.commit();
        return true;
    }
    
	/**
	 * Metodo que permite validar si los datos ingresados son de un usuario valido
	 * @return si es que el usuario es valido 
	 */
	public boolean validar() {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        String temp = "";
        try {
        	setUrl();
        	temp = URL + Cadenas.BIS;
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
            resultado = false;
        } finally {
            try { os.close(); } catch(Exception e) { }
            os = null;
            try { is.close(); } catch(Exception e) { }
            is = null;
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
        }
        return resultado;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        actualizar();
    }
    
    /*MBL 15/11/2011 : Limpia información del usuario*/
    public void Clear(){
    	usuario = null;
    	persist.setContents(usuario);
    	setUsuario(usuario);
    }

    /**
     * Metodo de llamada a la sincronizacion de la informacion general de la aplicación
     */
    public void sincronizar() {
    	if ( ! Sistema.isCoverage() ) {
    		Dialog.inform("No se puede inicar la sincronización error en la conexión a internet, intentelo más tarde.");
    		return;
    	}
    	
    	usuario.setSincronizado(false);
    	this.actualizar();
    	mkpyStatusProgress progressSync = new mkpyStatusProgress("Sincronizando...", 0, 100, GaugeField.PERCENT);
    	progressSync.open();
        ConfiguracionDB confs = new ConfiguracionDB();
        if(!confs.getRemote())
        	msjReintentar("Configuracion");
        confs = null;
        
    	progressSync.setProgress(0);
        SeccionDB secciones = new SeccionDB();
        if(!secciones.getRemote())
        	msjReintentar("Secciones");
        secciones = null;
        progressSync.setProgress(4);

        EstadoConsultoraDB estadosConsultora = new EstadoConsultoraDB();   
        if(!estadosConsultora.getRemote())
        	msjReintentar("Estados de las consultoras");
        estadosConsultora = null;
        progressSync.setProgress(12);
        
        MetaDB metas = new MetaDB();   
        if(!metas.getRemote())
        	msjReintentar("Metas");
        metas = null;
        progressSync.setProgress(19);
        
        MotivoNoVistaDB motivosNoVista = new MotivoNoVistaDB();   
        if(!motivosNoVista.getRemote())
        	msjReintentar("Motivos de no visita");
        motivosNoVista = null;
        progressSync.setProgress(21);
        
        NivelDB niveles = new NivelDB();
        if(!niveles.getRemote())
        	msjReintentar("Niveles");
        niveles = null;
        progressSync.setProgress(32);

    	ConsultoraDB consultoras = new ConsultoraDB();   
        if(!consultoras.getRemote())
        	msjReintentar("Consultoras");
        consultoras = null;
        progressSync.setProgress(69);

        FacturacionDB facturaciones = new FacturacionDB();
        if(!facturaciones.getRemote())
        	msjReintentar("Facturacion");
        facturaciones = null;
        progressSync.setProgress(84);
        
        IndicadorDB indicadores = new IndicadorDB();
        if(!indicadores.getRemote())
        	msjReintentar("Indicadores");
        indicadores = null;
        progressSync.setProgress(91);
        
        EstadisticaDB estadisticas = new EstadisticaDB();
        estadisticas.sendAll();
        estadisticas = null;

        usuario.setSincronizado(true);
    	usuario.setFechaHoraLocal(Fechas.dateToString("yyyyMMddHHmm"));
    	this.actualizar();
        progressSync.setProgress(100);
    	progressSync.close();
    	//progressSync = null;
    }

//	private void msjReintentar() {
//		String msj = "Error de sincronización de los datos. ¿Desea reintentar el proceso? ";
//		if(intentosSincroniza == 3){
//			msj = "Por favor, intentelo pasado unos minutos. ";
//			Dialog.alert(msj);
//			MetodosGlobales.salirAplicativo();
//		}
//		if( Dialog.ask(Dialog.D_YES_NO, msj, Dialog.D_OK) == Dialog.YES ){
//			intentosSincroniza++;
//			sincronizar();
//		} else { // if ( ask == -1 )
//			MetodosGlobales.salirAplicativo();
//		}
//	}

    /**
     * Mensaje generico de error en alguna de la sincronizacion de las tablas
     */
	private void msjReintentar(String table) {
		String msj = "Error de sincronización de los datos de " + table + ", se mostrará información anterior.";
		Dialog.inform(msj);
	}

	/**
	 * Metodo de solicitud de recordar clave
	 * @param documento
	 * @return Si el envio es exitoso o no
	 */
    public boolean enviaClave(String documento) {
        boolean resultado = false;
        HttpConnection httpConn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
        	setURLClave(documento);
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
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                if ( responseClave(rootElement.getChildNodes()) ) {
                    resultado = true;
                } else {
                	resultado = false;
                }
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
     * Metodo para manejar la respuestas del envio de clave
     * @param node objeto con el XML de la respuesta del servicio web
     * @return Si la respuesta es exitosa o no
     */
    private boolean responseClave(NodeList node) {
        Node contactNode = node.item(1);
        String registro = contactNode.getChildNodes().item(0).getNodeValue();        
        if ( registro.equals(RESPONSE_OK) ) {
        	return true;
        } else {
        	return false;
        }
    }
	
    /**
     * Metodo para validar si alguna de las fechas caducó
     * @return si es que se necseita sincronizar o no
     */
    public boolean validaCaducidades() {
    	progress.setTitle("Recibiendo paises ... ");
        PaisDB paises = new PaisDB();
        paises.getRemote();
        paises = null;
		progress.setTitle("Validando ... ");
		versionApp = Sistema.getVersion();
		versionAppUsr = usuario.getVersionApp();
		progress.setTitle("Verificando versiones ... ");
		Sistema.versionApp(versionApp, versionAppUsr);
        /*
        * fecha hora local es la ultima vez q se logueo + tiempoautovalidacion = nos da una fecha
        * que debe  ser menor a la del sistma fechas.dateToString(); para poder seguir
        */
		progress.setTitle("Validando fechas ... ");
		String fechaUser, fechaServidorString;
		fechaUser = usuario.getFechaHoraLocal();
		String fechaUserSgteValidar = Fechas.stringToFechaAValidacion(fechaUser, String.valueOf(usuario.getTiempoAutoValidacion()));
	    fechaServidorString = usuario.getFechaHoraServidor();    
	    String fechaBB = Fechas.dateToString();
	    
	    boolean resultado = true;

		if ( ! usuario.getUsuarioVersion().equals(Sistema.getVersion()) ) { // Si las versiones de la aplicación con la del ultimo usuario validado son distintas es decir que se actualizo la versión teniend un usuaro validado y se debe borrar
			// limpiar el objeto de usuario
			//usuario = null;
			this.setUsuario(null);
			progress.close();
			return false;
		}
	    
	    if (Fechas.validarFechas(fechaUserSgteValidar, fechaBB)) { // Si la fecha de la proxima validacion es menor a la fecha del BB debe pedir la validacion
	    	progress.close();
	    	return false;
        }
	    
	    if (Fechas.validarFechas(fechaBB, fechaServidorString)) { // Si la fecha del BB es menor a la fecha del servidor, validar
        	String fechaConsultoraString = Fechas.stringToFechaAValidacion(usuario.getFechaHoraUltimaSincronizacionConsultora(), String.valueOf(usuario.getTiempoDatosConsultora()));
            /**
             * SINCRONIZACION
             */
        	if(Fechas.validarFechas(fechaConsultoraString, fechaServidorString)){//fecha de consultoras menor fecha sistema 
				progress.setTitle("Sincronizando consultoras...");
				ConsultoraDB consultoras = new ConsultoraDB();   
				if(!consultoras.getRemote()){
					progress.setTitle("La sincronización de consultoras no fue exitosa.");
				}
                consultoras = null;
            } else {
            	resultado = false;
            }
             
			String fechaSaldosString = Fechas.stringToFechaAValidacion(usuario.getFechaHoraUltimaSincronizacionSaldos(), String.valueOf(usuario.getTiempoSaldosConsultora()));
			if(Fechas.validarFechas(fechaSaldosString, fechaServidorString)){// fecha de saldos
				progress.setTitle("sincroniza saldos");
				resultado = true;
			}

			String fechaTotalesString = Fechas.stringToFechaAValidacion(usuario.getFechaHoraUltimaSincronizacionTotales(), String.valueOf(usuario.getTiempoTotales()));
			if(Fechas.validarFechas(fechaTotalesString, fechaServidorString)){ //totales
				FacturacionDB facturaciones = new FacturacionDB();
				if(!facturaciones.getRemote())
					progress.setTitle("La sincronización de facturaciones no fue exitosa.");
				IndicadorDB indicadores = new IndicadorDB();
				if(!indicadores.getRemote())
					progress.setTitle("La sincronización de indicadores no fue exitosa.");
				resultado = true;
			}
             
			String fechaOtrosString = Fechas.stringToFechaAValidacion(usuario.getFechaHoraUltimaSincronizacionOtros(), String.valueOf(usuario.getTiempoAutoValidacion()));
			if(Fechas.validarFechas(fechaOtrosString, fechaServidorString)){ //TODO: ver que se sincroniza aca
				progress.setTitle("sincroniza otros");
				resultado = true;
			}
         	progress.close();
        	return resultado;
            	
        } else {
        	progress.close();
        	return resultado;
        }
	}

    /**
     * Metodo para validar al usuario cuando no existe coincidencia con la base de datos persistente
     * @param usr Usuario
     * @param pwd Clave
     * @param recordarme indicador de recordarme
     * @param usuario Si es que hay un usuario anterior
     * @param pais codigo del pais
     * @param fuerza codigo de la fuerza de venta del usuario
     */
    public void noExisteUsr(String usr, String pwd, boolean recordarme, Usuario usuario, String pais, String fuerza) {
		progress.open();
		usuario = getUsuario();
		setCodigo(usr);
        setClave(pwd);
        setIdPais(pais);
        setIdFuerza(fuerza);
    	progress.setTitle("Validando...");
        if(validar()) {
            usuario = getUsuario();
        	usuario.setAutovalidar(recordarme);
        	progress.setTitle("Verificando versiones");
        	versionApp = Sistema.getVersion();
			versionAppUsr = usuario.getVersionApp();
			Sistema.versionApp(versionApp, versionAppUsr);
			usuario.setIdPais(pais);
			usuario.setIdEmpresa(fuerza);
			setUsuario(usuario);

    		progress.setTitle("Sincronizando...");
    		sincronizar();
    		if (usuario.isSincronizado()) {
        		progress.close();
    			Estilos.pushScreen(new MenuOpciones());
    		}

        } else {
    		progress.close();
        	Dialog.inform("Usuario o clave incorrectos.");
        }
	}

	//TODO: falta verifica si la ultima sincronizacion fue exitosa
    /**
     * Metodo para validar al usuario si es que existe en la base de datos persistente
     */
	public void existeUsr() { 
		usuario = getUsuario();
	
		progress.open();
		progress.setTitle("Validando...");
		versionApp = Sistema.getVersion();
		versionAppUsr = usuario.getVersionApp();
		progress.setTitle("Verificando versiones");
		Sistema.versionApp(versionApp, versionAppUsr);
		/**
		 * validacion de caducidades
		 */	
		usuario.setFechaHoraLocal(Fechas.dateToString("yyyyMMddHHmm"));
  	    setUsuario(usuario);

		if(usuario.isSincronizado()) {
	  	    if ( validaCaducidades() ) {
		  		progress.close();
		        Estilos.pushScreen(new MenuOpciones());
		  	} else {
		  		progress.close();
		  		Dialog.inform("Se ha vencido el plazo para autovalidación, por favor ingrese sus datos.");
		  	}
    	} else {
    		sincronizar();
    		if (usuario.isSincronizado()) {
        		progress.close();
    			Estilos.pushScreen(new MenuOpciones());
    		}
    	}
  	    
	}

    /**
     * Metodo para validar al usuario si es que existe en la base de datos persistente
     * Puede mostrar o no mostrar la pantalla de menuopciones
     */
	public void existeUsr(boolean conpantalla) { 
    	progress.setTitle("Recibiendo paises ... ");
        PaisDB paises = new PaisDB();
        paises.getRemote();
        paises = null;
		progress.setTitle("Validando ... ");
		versionApp = Sistema.getVersion();
		versionAppUsr = usuario.getVersionApp();
		progress.setTitle("Verificando versiones ... ");
		Sistema.versionApp(versionApp, versionAppUsr);
		
		sincronizar();
  	    
	}
	
}

