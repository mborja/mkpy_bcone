package com.belcorp.utilidades;

import com.belcorp.dao.EstadisticaDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Estadistica;
import com.belcorp.entidades.Usuario;
import com.belcorp.ui.MetodosGlobales;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.SIMCardException;
import net.rim.device.api.system.SIMCardInfo;
import net.rim.device.api.ui.component.Dialog;

public final class Sistema {
	private final static String idApp = "0x88e3e33db93a1e6cL"; // pe.com.belcorp.one
    private final static int tipoPerfil = 1; // 1 = GZ, 2 = GR, 3 =  DV
    public static final int perfilGGZZ = 1;
    public static final int perfilGGRR = 2;
    public static final int perfilDV = 3;

    public static int getPerfil() {
    	return tipoPerfil;
    }

    /**
     * Adiciona un a la estadística de la opción de menú
     * @param opcion valor id del número de opción
     */
    public static void addEstadistica(String opcion) {
    	EstadisticaDB est = new EstadisticaDB();
    	Estadistica item = new Estadistica();
    	item.setOpcion(opcion);
    	item.setCantidad(1);
    	item.setFechaHora(Fechas.dateToString());
    	est.addEstadistica(item);
    	est = null;
    }

    /**
     * Obtiene el ID del la empresa del usuario validado
     * @return String el id de la empresa del usuario validado
     */
    public static String getEmpresa() {
    	UsuarioDB usuarios = new UsuarioDB();
    	Usuario usuario = usuarios.getUsuario();
    	usuarios = null;
    	return usuario.getIdEmpresa();
    }

    /**
     * Obtiene el tipo de pais del usuario validad
     * @return String el tipo de pais del usuario validad
     */
    public static String getTipoPais() {
    	UsuarioDB usuarios = new UsuarioDB();
    	Usuario usuario = usuarios.getUsuario();
    	usuarios = null;
    	return usuario.getIdPais();
    }

    /**
     * Obtiene el tipo de rol dependiendo del tipo de fuerza
     * @param fuerza, es el tipo de fuerza de venta (FL, FE)
     * @return int el id del tipo de rol
     */
    public static int getTipoRol(String fuerza) {
    	int retorno = 0;
    	if ( fuerza.equals("FS") ) {
    		if ( tipoPerfil == 1 ) {
    			retorno = 3;
    		} else if ( tipoPerfil == 2 ) {
    			retorno = 11;
    		} else if ( tipoPerfil == 3 ) {
    			retorno = 14;
    		}
    	} else if ( fuerza.equals("FL") ) {
    		if ( tipoPerfil == 1 ) {
    			retorno = 5;
    		} else if ( tipoPerfil == 2 ) {
    			retorno = 8;
    		} else if ( tipoPerfil == 3 ) {
    			retorno = 14;
    		}
    	}
    	return retorno;
    }
    
    /**
     * Otiene el tipo de rol del usuario validado
     * @return int el id del tipo de rol
     */
    public static int getTipoRol() {
    	UsuarioDB usuarios = new UsuarioDB();
    	Usuario usuario = usuarios.getUsuario();
    	usuarios = null;
    	int retorno = 0;
    	if ( usuario != null ) {
        	if ( usuario.getIdEmpresa().equals("FS") ) {
        		if ( tipoPerfil == 1 ) {
        			retorno = 3;
        		} else if ( tipoPerfil == 2 ) {
        			retorno = 11;
        		} else if ( tipoPerfil == 3 ) {
        			retorno = 14;
        		}
        	} else if ( usuario.getIdEmpresa().equals("FL") ) {
        		if ( tipoPerfil == 1 ) {
        			retorno = 5;
        		} else if ( tipoPerfil == 2 ) {
        			retorno = 8;
        		} else if ( tipoPerfil == 3 ) {
        			retorno = 14;
        		}
        	}
    	}
    	return retorno;
    }

    /**
     * Obtiene el estado de la red celular
     * @return boolean si es que hay cobertura o no
     */
    public static boolean isCoverage() {
		try {
	    	if ( GPRSInfo.getGPRSState() == GPRSInfo.GPRS_STATE_STANDBY || GPRSInfo.getGPRSState() == GPRSInfo.GPRS_STATE_READY ) {
	    		return true;
	    	}
	    	return false;
		} catch (Exception e) {
			return true;
		}
    }
    
    /**
     * Obtiene el número del chip del la línea celular
     * @return String el número del chip
     */
    public static String getImsi() {
		try {
			return GPRSInfo.imeiToString(SIMCardInfo.getIMSI(), false );
		} catch (Exception e) {
			return "ND";
		}
	}

    /**
     * Retorna el número redondeado  
     * @param n
     * @return
     */
	public static long round(double n){
	    return (long)(n + 0.5D);
	}
	
	public static String round(double n, int d){
		String num = String.valueOf(n);
		int index = num.indexOf(".");
	    return num.substring(0, index + d);
	}
	
	/**
	 * Obtiene el IMEI número de serie del equipo celular
	 * @return String el número de serie del equipo móvil
	 */
	public static String getImei() {
		try {
			return GPRSInfo.imeiToString(GPRSInfo.getIMEI(), false);	
		} catch (Exception e) {
			return "ND";
		}
	}
	
	/**
	 * Obtiene el número de PIN del BlackBerry
	 * @return int el número PIN del BlackBerry
	 */
	public static int getPin() {
		return DeviceInfo.getDeviceId();
	}
	
	/**
	 * Obtiene el número de versión de la aplicación indicado en el JAD
	 * @return
	 */
	public static String getVersion(){
	    String version = ApplicationDescriptor.currentApplicationDescriptor().getVersion();
		return version;
	}

	/**
	 * Obtiene un valor del JAD dependiendo de la clave
	 * @param Name la clave indicada en el archivo JAD
	 * @return String el valor indicado en el archivo JAD para la Clave en @Name
	 */
	public static String getJADProperty(String Name){
	    CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
	    CodeModuleGroup myGroup = null;
	    String moduleName = ApplicationDescriptor.currentApplicationDescriptor().getModuleName();
	    for (int i = 0; i < allGroups.length; i++) {
		    if (allGroups[i].containsModule(moduleName)) {
			    myGroup = allGroups[i];
			    break;
		    }
	    }
	 
	    // Get the property
	    String prop = myGroup.getProperty(Name);
	    return prop;
	}

	/**
	 * Obtiene el valor ID de la aplicación
	 * @return String el id de la aplicación
	 */
	public static String getIdapp() {
		return idApp;
	}

	/**
	 * Compara las versiones de la aplicación con la indicada en el servidor
	 * @param versionApp
	 * @param versionAppUsr
	 */
	public static void versionApp(String versionApp, String versionAppUsr) {
    	if(!versionApp.equals(versionAppUsr)){ 
    		String msj = "Existe una nueva actualización del aplicativo "+
    		"necesaria para continuar ¿Desea descargarla en este momento?";	
    		int ask = Dialog.ask(Dialog.D_YES_NO, msj, Dialog.NO);
    		if( ask == Dialog.YES ){
    			Browser.getDefaultSession().displayPage(Cadenas.urlDownload);
//    		} else { // if ( ask == -1 )
    			//MetodosGlobales.salirAplicativo();
    		}
			//return false;
            MetodosGlobales.salirAplicativo();
//    	} else {
//    		return true;
    	}
	}

}
