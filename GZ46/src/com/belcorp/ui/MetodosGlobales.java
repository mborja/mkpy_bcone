package com.belcorp.ui;

import java.util.Vector;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.dao.EstadoConsultoraDB;
import com.belcorp.dao.NivelDB;
import com.belcorp.dao.PaisDB;
import com.belcorp.dao.SeccionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.EstadoConsultora;
import com.belcorp.entidades.Nivel;
import com.belcorp.entidades.Pais;
import com.belcorp.entidades.Seccion;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;

public class MetodosGlobales {
    static Usuario usuario = new Usuario();
//    private static int perfil ; // DV = 1, GR = 2, GZ = 3
    private UsuarioDB usuarios = new UsuarioDB();
	public final static String[] opciones = {"Consultoras", " Atracción", " Nuevas", " Buscar", "Pedidos", " Facturación", " Indicadores"};

    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    
    public static void salirAplicativo() {
        System.exit(0);
    }

    public static Screen pantallaActiva() {
        return UiApplication.getUiApplication().getActiveScreen();
    }

    public static void pantallaAnterior() {
        UiApplication.getUiApplication().popScreen(pantallaActiva());
    }

    public static int BuscarSeccionxNombre(String seccion) {
        String specificationArray[] = null;
        int seccionId = -1; 
        SeccionDB seccionDB = new SeccionDB();
                        
        Vector vSeccion = null;
        Seccion sSeccion;
        try {
            vSeccion = seccionDB.getObjetos();
            specificationArray = new String[vSeccion.size()];
            int count = 0;
            for (count = 0; count < specificationArray.length; count++) {
                sSeccion = (Seccion) vSeccion.elementAt(count);
                if(seccion.equals(sSeccion.getDescripcion()))
                    seccionId = Integer.parseInt(sSeccion.getId());
                if (specificationArray[count] == null)
                    specificationArray[count] = "";
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo buscar seccion por  nombre. ");
        }
        return seccionId;
    }
    
    public static String[] mostrarSeccion() {
        String sSeccion[] = null;
        
        SeccionDB secciones;    
    	secciones = new SeccionDB();
        Vector vSeccion = null;
        Seccion seccion;
        try {
            vSeccion = secciones.getObjetos();
            sSeccion = new String[vSeccion.size() + 1];
            sSeccion[0] = "Todas";
            int i = 1;
            for (int count = 0; count < vSeccion.size(); i++, count++) {
            	seccion = (Seccion) vSeccion.elementAt(count);
                sSeccion[i] = seccion.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar las secciones. ");
        }
    	 
        return sSeccion;
    }
    
    public static String[] mostrarPaises() {
        String sPaises[] = null;
        
        PaisDB paises = new PaisDB();
        Vector vPaises = null;
        Pais pais;
        try {
        	vPaises = paises.getObjetos();
            sPaises = new String[vPaises.size()];
            int i = 0;
            for (int count = 0; count < vPaises.size(); i++, count++) {
            	pais = (Pais) vPaises.elementAt(count);
                sPaises[i] = pais.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar las secciones. ");
        }
        return sPaises;
    }

    public static String[] mostrarNivel() {
        String nivel[] = null;
      
        NivelDB niveles;    
        niveles = new NivelDB();
        Vector vNivel = null;
        Nivel nivel2;
        try {
            vNivel = niveles.getObjetos();
            nivel = new String[vNivel.size() + 1];
            nivel[0] = "Todos";
            int i = 1;
            for (int count = 0; count < vNivel.size();i++, count++) {
            	nivel2 = (Nivel) vNivel.elementAt(count);
                nivel[i] = nivel2.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar los niveles. ");
        }
        return nivel;
    }
    
    public static String[] mostrarEstado() {
        String estado[] = null;
      
        EstadoConsultoraDB estadoConsultoraDB;    
        estadoConsultoraDB = new EstadoConsultoraDB();
        Vector vEstado = null;
        EstadoConsultora estadoConsultora;
        try {
            vEstado = estadoConsultoraDB.getObjetos();
            estado = new String[vEstado.size() + 1];
            estado[0] = "Todos";
            int i = 1;
            for (int count = 0; count < vEstado.size();i++, count++) {
            	estadoConsultora = (EstadoConsultora) vEstado.elementAt(count);
            	estado[i] = estadoConsultora.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar los estados. ");
        }
        return estado;
    }
    
    public static String[] lsCatFacturacion(){
         String[] sCategorias = {
			"Pedidos", "PEG", "Primer Pedido", "Enviado", "Observacion",
			"Rechazado", "Facturado", "Monto", "Activas sin Pedidos"}; 
         return sCategorias;
    }

    public static String[] getCateEstadoPedido(){
        String[] sEstadoPedido = {"Enviado", "Observado", "Rechazado", "Facturado", "Sin pedido", "Todos"}; 
        return sEstadoPedido;
    }    
    
	  public static String[] lsCatIndicadores(){
		  String[] sCategorias = {
		  		"Cant. pedidos por venta",
		  		"Cant. pedidos facturado",
		  		"% Cant. pedidos logrados",
		  		"Monto pedido venta",
		  		"Monto pedido facturado",
		  		"% Monto pedido logrado",
		  		"Ingresos estimados",
		  		"Ingresos reales",
		  		"% Primer pedido logrado",
		  		"Monto venta neta",
		  		"Cantidad consultoras activas",
		  		"% Actividad",
		  		"Cantidad consultoras reingreso",
		  		"Cantidad consultoras egreso",
		  		"Capitalización",
		  		"% Rotación",
		  		"% Retención"}; 
		  return sCategorias;
	  }
	  
	  private static boolean isSubModulo(Configuracion conf, int subModulo) {
    	  int n = conf.getSubModulo().size();
    	  for ( int i = 0; i < n; i++) {
    		  String submodulo = (String) conf.getSubModulo().elementAt(i);
    		  int index = Integer.parseInt(submodulo);
    		  if ( index == subModulo ) return true;
    	  }
    	  return false;
	  }
      
/**
 *
1 login
2 menu
3 atraccion
4 nuevas
5 buscar
6 facturacion
7 indicadores

6|1|2|4|5|6|7|

 * @return
 */
	  public static String[] lsMenuOpciones(){
    	  String[] opc;
    	  ConfiguracionDB confs = new ConfiguracionDB();
    	  Configuracion conf = confs.getObjeto();
    	  int n = conf.getSubModulo().size();
    	  int index = 0;
    	  for ( int i = 0; i < n; i++ ) {
    		  String submodulo = (String) conf.getSubModulo().elementAt(i);
    		  int valor = Integer.parseInt(submodulo);
    		  if ( valor > 7 ) {
    			  n = 0;
    			  break;
    		  }
    	  }
    	  if ( n > 0 ) {
        	  opc = new String[n];
        	  opc[index++] = opciones[0]; // consultora
        	  if ( isSubModulo(conf, 3) ) opc[index++] = opciones[1]; // atraccion
        	  if ( isSubModulo(conf, 4) ) opc[index++] = opciones[2]; // nuevas
        	  if ( isSubModulo(conf, 5) ) opc[index++] = opciones[3]; // buscar
        	  opc[index++] = opciones[4]; // pedido
        	  if ( isSubModulo(conf, 6) ) opc[index++] = opciones[5]; // fact
        	  if ( isSubModulo(conf, 7) ) opc[index++] = opciones[6]; // indic
    	  } else {
    		  // .
        	  if ( Sistema.getPerfil() == Sistema.perfilGGZZ ) {
        		  opc = new String[7];
        	  } else {
        		  opc = new String[5];
        	  }
        	  opc[index++] = opciones[0]; // consultora
        	  if ( Sistema.getPerfil() == Sistema.perfilGGZZ ) {
            	  opc[index++] = opciones[1]; // atraccion
            	  opc[index++] = opciones[2]; // nuevas
        	  }
        	  opc[index++] = opciones[3]; // buscar
        	  opc[index++] = opciones[4]; // pedido
        	  opc[index++] = opciones[5]; // fact
        	  opc[index++] = opciones[6]; // indic
    	  }
    	  
          return opc;
      }
      
//      public static  int perfilDV(){
//    	  return 1;
//      }
//
//      public static  int perfilGR(){
//    	  return 2;
//      }
//      
//      public static int perfilGZ(){
//    	  return 3;
//      }
      
      public static String[] menuOpcionesxPerfil(){
		  return lsMenuOpciones();
      }

//	public void setPerfil(int perf) {
//		this.perfil = perf;
//	}

	public static int getPerfil() {
		return Sistema.getPerfil();
	}

	public static String establecidasTitulo() {
		int perfil = getPerfil();
		int gz = Sistema.perfilGGZZ;
		if(perfil == gz){
			return "Establecidas";
		}
		else{ 
			return "Mis Contactos";
		}
	}
		

}
