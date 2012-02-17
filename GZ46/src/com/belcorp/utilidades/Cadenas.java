package com.belcorp.utilidades;

import java.util.Vector;

import javax.microedition.global.Formatter;

import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.entidades.Configuracion;

import net.rim.device.api.i18n.MessageFormat;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.component.Dialog;

//TODO : Hacer funcion para que elija el BIS y el URLBASE de forma dinámica
public final class Cadenas {
    public static final String TOKEN = "|";
    // MB: cuando se quiere ejecutar en un equipo
    //public static final String BIS = ";DeviceSide=false;ConnectionSetup=delayed;UsePipe=true;ConnectionTimeout=120000;EncryptRequired=true;ConnectionType=mds-public";    
    // MB: cuando se quiere ejecutar en un emulador
    //public static final String BIS= ";DeviceSide=false";
    public static final String APTA = "1";
    public static final double CONVERSION = 6.6; 
    //MB : servidor de producción
    //public static final String URLBASE = "http://ws.somosbelcorp.com/ProcesoOneBlackBerry.asmx";
    //public static final String URIServer = "ws.somosbelcorp.com";
    //MB : Servidor de pruebas
    public static final String URLBASE = "http://wdev.ws.somosbelcorp.com/ProcesoOneBlackBerry.asmx";
    public static final String URIServer = "wdev.ws.somosbelcorp.com";
    public static final String urlDownload = "http://m.somosbelcorp.com";
    
    public static final String LOGIN = "1";
    public static final String RECORDARCLAVE = "2";
    public static final String MENUPRINCIPAL = "3";
    public static final String MENUNUEVAS = "56";
    public static final String ATRACCION = "4";

    public static final String V1_LISTASECCION = "5";
    public static final String V1_LISTACONSULTORA = "6";
    public static final String V1_DATOSCONSULTORA = "7";
    public static final String V1_VISITANOEXITOSA = "8";
    public static final String V1_VENTA = "9";
    public static final String V1_VENTAXMARCA = "10";
    public static final String V1_PUNTAJE = "11";
    public static final String V1_ANOTACIONES = "238";

    public static final String V2_LISTASECCION = "12";
    public static final String V2_LISTACONSULTORA = "13";
    public static final String V2_DATOSCONSULTORA = "14";
    public static final String V2_VISITANOEXITOSA = "15";
    public static final String V2_VENTA = "16";
    public static final String V2_VENTAXMARCA = "17";
    public static final String V2_PUNTAJE = "18";
    public static final String V2_POSTVENTA = "19";
    public static final String V2_ANOTACIONES = "239";

    public static final String V3_LISTASECCION = "20";
    public static final String V3_LISTACONSULTORA = "21";
    public static final String V3_DATOSCONSULTORA = "22";
    public static final String V3_VISITANOEXITOSA = "23";
    public static final String V3_VENTA = "24";
    public static final String V3_VENTAXMARCA = "25";
    public static final String V3_PUNTAJE = "26";
    public static final String V3_POSTVENTA = "27";
    public static final String V3_ANOTACIONES = "240";
    
    public static final String V4_LISTASECCION = "28";
    public static final String V4_LISTACONSULTORA = "29";
    public static final String V4_DATOSCONSULTORA = "30";
    public static final String V4_VISITANOEXITOSA = "31";
    public static final String V4_VENTA = "32";
    public static final String V4_VENTAXMARCA = "33";
    public static final String V4_PUNTAJE = "34";
    public static final String V4_POSTVENTA = "35";
    public static final String V4_ANOTACIONES = "241";

    public static final String BUSCARCONSULTORA = "36";
    public static final String BC_LISTACONSULTORA = "37";
    public static final String BC_DATOSCONSULTORA = "38";
    public static final String BC_VENTA = "39";
    public static final String BC_VENTAXMARCA = "40";
    public static final String BC_PUNTAJE = "41";
    public static final String BC_POSTVENTA = "42";
    public static final String BC_ANOTACIONES = "242";

    public static final String GENERARLISTADO = "43";
    public static final String GL_LISTACONSULTORA = "44";
    
    public static final String GL_LISTACONSULTORASECCION = "45";
    public static final String GL_LISTACONSULTORANIVEL = "46";
    public static final String GL_LISTACONSULTORAESTADO = "47";
    public static final String GL_LISTACONSULTORADEUDA = "48";
    public static final String GL_LISTACONSULTORAPEDIDO = "49";
    public static final String GL_LISTACONSULTORAANOTACIONES = "242";
    
    public static final String GL_DATOSCONSULTORA = "50";
    public static final String GL_VENTA = "39";
    public static final String GL_VENTAXMARCA = "40";
    public static final String GL_PUNTAJE = "41";
    public static final String GL_POSTVENTA = "42";
    
    public static final String FAC_CAMPANA = "51";
    public static final String FAC_CATEGORIA = "52";
    public static final String FAC_SECCION = "53";

    public static final String IND_CAMPANA = "54";
    public static final String IND_CATEGORIA = "55";

    public static final String SINCRONIZAR = "99";
    
    public static String formatCurrency(String value) {
        int i, n;
        long parteEntera;
        double decimal, total;
        String sDecimal, sEntero, resultado = "";

        total = Double.parseDouble(value);
        int idx = value.indexOf('.');
        if ( idx >= 0 ) {
                parteEntera = Long.parseLong( value.substring(0, idx) );
        } else {
                parteEntera = Long.parseLong( value );
        }
        decimal = total - parteEntera;

        if ( decimal == 0 ) {
                sDecimal = ".00";
        } else {
                String tmp = String.valueOf(decimal) + "000";
                sDecimal = getDecimal() + tmp.substring(3);
        }
        sDecimal = sDecimal.substring(0, 3);
        sEntero = String.valueOf(parteEntera);
        n = sEntero.length() - 1;
        for ( i = n; i >= 0; i--) {
                if ( (i % 3 == 0 ) && (i > 0) && (i < n) ) { // adicionar simbolo de miles
                                resultado = sEntero.charAt(i) + getMiles() + resultado;   
                        //Dialog.inform(i + " " + sEntero.charAt(i) + " " + resultado);
                } else { //
                        resultado = sEntero.charAt(i) + resultado; 
                }
        }
        resultado = resultado + sDecimal;
        Dialog.inform(resultado);
        
        //CoverageInfo.is
        
        return resultado;
    }
    
    public static String getMoneda() { 
        ConfiguracionDB confs = new ConfiguracionDB();
        Configuracion conf = confs.getObjeto();
        confs = null;
        return conf.getSimboloMoneda();
    }
    
    public static String getDecimal() { 
        ConfiguracionDB confs = new ConfiguracionDB();
        Configuracion conf = confs.getObjeto();
        confs = null;
        return conf.getSimboloDecimal();
    }

    public static String getMiles()  { 
        ConfiguracionDB confs = new ConfiguracionDB();
        Configuracion conf = confs.getObjeto();
        confs = null;
        return conf.getSimboloMiles();
    }

    //TODO: jalar valores de Mensajes
    public static String getClasificacionMetodologica(String id) {
        String resulado = "img/titulos/establecida.png";
        if ( id.equals("1") ) {
            resulado = "img/titulos/nuevasvisitameta.png";
        }
        if ( id.equals("2") ) {
            resulado = "img/titulos/visitacompanamiento2.png";
        }
        if ( id.equals("3") ) {
            resulado = "img/titulos/visitacompanamiento3.png";
        }
        if ( id.equals("4") ) {
            resulado = "img/titulos/visitafelicitaciones.png";
        }
        if ( id.equals("5") ) {
            resulado = "img/titulos/establecida.png";
        }
        return resulado;
    }
    
    public static String[] splitSimple(String strCadena, String strSeparador) {
        int indexOfEnd = strCadena.length();
        String[] strCampos = null;
        Vector lista = new Vector();
        while (indexOfEnd > 0) {
           indexOfEnd = strCadena.indexOf(strSeparador);
           if (indexOfEnd >= 0) {
              lista.addElement(strCadena.substring(0, indexOfEnd));
              indexOfEnd += strSeparador.length();
              strCadena = strCadena.substring(indexOfEnd);
           } else {
              String cadena = strCadena.substring(0);
              if (cadena.length() > 0) {
                 lista.addElement(cadena);
              }
           }
        }
        strCampos = new String[lista.size()];
        lista.copyInto(strCampos);
        lista = null;
        return strCampos;
     }
    
    public static String getMsjDatosLocales() { 
        return "Debido a dificultades con la conexión," +
                   "se mostrará la siguiente información local.";
    }
    
    public static String getMsjTresCaracteres() { 
        return  "el nombre debe tener mínimo 3 caracteres para la búsqueda";
    }
    
    public static String getEspacio(int lenght){
        int pantallaPixeles = Display.getWidth();
        int totalCaracteres = (int)(pantallaPixeles / CONVERSION + 0.5D);
        int cantCaracteresFalta = totalCaracteres - lenght;
        String espacio = "";
        for(int i = 0; i <= cantCaracteresFalta; i++){
                espacio += " ";
        }
        return espacio;
    }
    
    public static String getBIS() {
    	if ( DeviceInfo.isSimulator() ) {
    		return ";DeviceSide=false";
    	} else {
    		return ";DeviceSide=false;ConnectionSetup=delayed;UsePipe=true;ConnectionTimeout=120000;EncryptRequired=true;ConnectionType=mds-public";
    	}
    }
    
    public static String getFormatDecimalNumberString(double numero,int decimales){
    	String numeroEnTexto = String.valueOf(numero);
    	String parteEntera="0";
    	String parteDecimal="00";
    	String resultado="";
    	String[] textoSplit = splitSimple(numeroEnTexto,".");
    	if(textoSplit.length==2){
    		parteEntera=textoSplit[0];
    		parteDecimal=textoSplit[1].concat("00");
    	}else if(textoSplit.length==1){
    		parteEntera=textoSplit[0];
    		parteDecimal="00";    		
    	}
        //MBL:Si se desea darle formato de miles a la parte entera.
        //parteEntera = formatMiles( Integer.parseInt(parteEntera));
        parteDecimal = parteDecimal.substring(0,2);
    	
    	resultado = parteEntera.concat(getDecimal()).concat(parteDecimal);
    	
    	return resultado;
    }
    
    public static String formatMiles(int entero){
    	String parteEntera = String.valueOf(entero);
        String enteroConMiles="";
        int n = parteEntera.length();
        for (int i = n; i > 0; i--) {
            if ( (i % 3 == 0 ) && (i > 0) && (i < n) ) { // adicionar simbolo de miles
            	enteroConMiles = parteEntera.charAt(i-1) + getMiles() + enteroConMiles;   
            } else { //
            	enteroConMiles = parteEntera.charAt(i-1) + enteroConMiles; 
            }
        }
        return enteroConMiles;
    }
    
}
