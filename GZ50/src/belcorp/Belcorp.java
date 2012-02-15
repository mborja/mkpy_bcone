package belcorp;

/*
 * Belcorp.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.belcorp.dao.PaisDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Usuario;
import com.belcorp.ui.Autenticar;
import com.belcorp.ui.MenuOpciones;
import com.belcorp.ui.MetodosGlobales;
import com.belcorp.utilidades.Sincronia;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;

/**
 * @author Fencalada  
**/

class Belcorp extends UiApplication{
    private static Belcorp myApp;
    private static String version; 
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    
    public static void main(String[] args) {
        setVersion(ApplicationDescriptor.currentApplicationDescriptor().getVersion());
        myApp = new Belcorp();
        myApp.enterEventDispatcher();
    }

    Belcorp() {
        try {
            UiApplication.getUiApplication().invokeLater( new Runnable() {
            	public void run () {
            		/*
        	        * TODO: secuencia de pasos iniciales
        	        * veriricar si hay usuario getusuario()
        	        * 
        	        * si hay usuario, verificar si tiene la marca de autovalidar y si puede autovalidar ==> pasar por (*)
        	        * si no llamar a paises y mensajes y mostrar el login 
        	        * si el validar es exitos cargar los datos faltantes
        	        * 
        	        * (*) validar la fecha del sistem con la del servidor web (que viene con el usuario) 
        	        * si las fechas del svr es menor pasa sino se debe llamar a sincronizar la info
        	        * valida vencimiento de las fechas de los datos de consultoras y saldos
        	        * (fechas de caducidad, saldos y totales deben ser menor a la fecha actual,
        	        * de lo contrario a la que no cumpla se le sincroniza)
        	        * 
        	        * 
        	        * como referncia puedes usar los diagramas de secuencia del docuemnto de dise–o GZ de dropbox
        	        * 
        	        * SI el estado de sincronizadao esta en FAlSE debe de sincronizar forzasamente
        	        */
            		UsuarioDB usuarios = new UsuarioDB();
        	        Usuario usuario = usuarios.getUsuario();
        	        
	        		//Si el usuario existe
	        		if(usuario != null && usuario.getCodigo() != null) { 
	        	        String versionApp = Sistema.getVersion();
	        			String versionImsi = Sistema.getImsi();
	        			String versionAppUsr = usuario.getVersionApp();
	        			String versionImsiUsr = usuario.getImsi();
	          	      	//si las versiones del IMSI son iguales
	        	        if(versionImsi.equals(versionImsiUsr)) {
	        	        	// si la version del app son iguales
	        	        	Sistema.versionApp(versionApp, versionAppUsr);
            	            if(usuario.getPuedeAutoValidar() && usuario.getAutovalidar()) {
            	            	if ( usuarios.validaCaducidades() ) {
            	                    pushScreen(new MenuOpciones());
            	                    
            	            	} else {
                            		pushScreen(new Autenticar() );                            
                    	        }
            	            	   
	        	        	} else {
                        		pushScreen(new Autenticar() );                            
        	            	}
            	            // si el IMSI no son iguales
		                } else {
		                	pushScreen(new Autenticar() );
		                }
        	        }
	        		else {  // si no existe usuario se muestra la opcion de login pero antes se tiene que cargar los mensajes
	        			progress.open();
	        	    	progress.setTitle("Recibiendo paises...");
	        	        PaisDB paises = new PaisDB();
	        	        paises.getRemote();
	        	        progress.close();
	        	        paises = null;
	        			
                		pushScreen(new Autenticar() );                            
        	        }
            	}
           });
            
        } catch(Exception e) {
        }
     }

        public static void setVersion(String version) {
                Belcorp.version = version;
        }

        public static String getVersion() {
                return version;
        }

} 



