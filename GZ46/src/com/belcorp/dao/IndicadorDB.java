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

import com.belcorp.entidades.Facturacion;
import com.belcorp.entidades.Indicador;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Fechas;
import com.belcorp.utilidades.Sistema;

public class IndicadorDB {
    private static final String metodoWeb = "BBWS16Indicadores"; //BBWS7ObtenerMeta?PIN=&IMEI=&IMSI=&IDAPP=&FFVV=FS&CodigoPais=21&NombreUsuario=1215&GMT=&Rol=
    private static String URL, DATA;
    private static PersistentObject persist;
    private static final long IDSTORE = 0x623ef9edeca6210eL; // com.belcorp.entidades.Indicador
    private static Vector objetos;
    private Usuario usuario;

    public IndicadorDB() {
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

    private void setUrl() {
    	URL = Cadenas.URLBASE + "/" + metodoWeb;
    	DATA = "PIN=" + Sistema.getPin() + "&IMEI=" + Sistema.getImei() + "&IMSI=" + Sistema.getImsi() 
			+ "&IDAPP=" + Sistema.getIdapp() 
			+ "&IdEmpresa=" + Sistema.getEmpresa() + "&IdPais=" + Sistema.getTipoPais() 
			+ "&Zonaregionpais=" + usuario.getZonaRegionPais() + "&NombreUsuario=" + usuario.getCodigo() + "&IdRol=" 
			+ Sistema.getTipoRol() + "&GMT=" + Fechas.getGMT() + "&AnhoCampanha=" + usuario.getCampana();
    }
    
    private boolean fillObjectos(NodeList node) throws Exception {
        int n = node.getLength();
        objetos = new Vector();

        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            Indicador indicador = new Indicador();
            indicador.setUbicacion(fields[1]);
            indicador.setCampana( fields[2] );
            indicador.setCantidadPedidosVenta( Double.parseDouble(fields[3]) );
            indicador.setCantidadPediosFacturado( Double.parseDouble(fields[4]) );
            indicador.setPorcentajeCantidadPedidoLogrado( Double.parseDouble(fields[5]) );
            indicador.setMontoPedidoVenta( Double.parseDouble(fields[6]) );
            indicador.setMontoPedidoFacturado( Double.parseDouble(fields[7]) );
            indicador.setPorcentajeMontoPedidoLogrado( Double.parseDouble(fields[8]) );
            indicador.setIngresosestimados( Double.parseDouble(fields[9]) );
            indicador.setIngresosreal( Double.parseDouble(fields[10]) );
            indicador.setPorcentajPrimerPedidoLogrado( Double.parseDouble(fields[11]) );
            indicador.setMontoVentaNeta( Double.parseDouble(fields[12]) );
            indicador.setCantidadConsultorasActivas( Double.parseDouble(fields[13]) );
            indicador.setPorcentajeActividad( Double.parseDouble(fields[14]) );
            indicador.setCantidadConsultorasReingreso( Double.parseDouble(fields[15]) );
            indicador.setCantidadConsultorasEgreso( Double.parseDouble(fields[16]) );
            indicador.setCapitalizacion( Double.parseDouble(fields[17]) );
            indicador.setPorcentajeRotacion( Double.parseDouble(fields[18]) );
            indicador.setPorcentajeRetencion( Double.parseDouble(fields[19]) );

            objetos.addElement(indicador);
        }
        sortIndicadores();
        persist.setContents(objetos);
        persist.commit();
        return true;
    }
    
    private void sortIndicadores() {
        int n, i, j;
        if (objetos == null) {
        	n = 0;
        } else {
            n = objetos.size();
        }
        for ( i = 0; i < n; i++ ) {
            for ( j = 0; j < n; j++ ) {
            	Indicador indI = (Indicador) objetos.elementAt(i); 
            	Indicador indJ = (Indicador) objetos.elementAt(j);
            	try {
                	if ( indI.getCampana().compareTo(indJ.getCampana()) > 0 ) { // I es menor que J
                		objetos.setElementAt(indI, j);
                		objetos.setElementAt(indJ, i);
                	}
            	} catch(Exception e) {
            		
            	}
            }
        }
    }    
    
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
    
    public Vector getObjetos() {
        return objetos;
    }
    
    public Vector getObjetosByCampana(String campana) {
    	Vector result = new Vector();
    	int n = objetos.size();
    	for(int i = 0; i < n; i++) {
    		Indicador item = (Indicador) objetos.elementAt(i);
    		if ( item.getCampana().equals(campana) ) {
    			result.addElement(item);
    		}
    	}
        return result;
    }
    
    public static String[] mostrarSecciones(){
   	 	Vector vIndicadores = null;
        Indicador indicador;
        String specificationArray[] = null;
        try { 
       	 	vIndicadores = objetos;
           specificationArray = new String[vIndicadores.size()];
           int count = 0;
           for (count = 0; count < specificationArray.length; count++) {
           		indicador = (Indicador) vIndicadores.elementAt(count);
                specificationArray[count] = indicador.getUbicacion();
                if (specificationArray[count] == null)
                	specificationArray[count] = "";
           	}
        } catch (Exception e) {
            Dialog.alert("Ex: " + e);
        }
        return specificationArray;
   }

	  public double[] ArrayTotalesxFila() {
       double specificationArray[] = null;
        Vector vIndicadores = null;
        Indicador Indicador;
      //  String campanas[] = this.mostrarCampanas();
      //  int camp = campanas.length;
	        try { 
	        	vIndicadores = objetos;
	            specificationArray = new double[17];
	            int count = 0;
	            for (count = 0; count < vIndicadores.size(); count++) {
	            	Indicador = (Indicador) vIndicadores.elementAt(count);
	            	
	            	specificationArray[0] = specificationArray[0] + Indicador.getCantidadPedidosVenta();
	            	specificationArray[1] = specificationArray[1] + Indicador.getCantidadPediosFacturado();
	                specificationArray[2] = specificationArray[2] + Indicador.getPorcentajeCantidadPedidoLogrado();
	                specificationArray[3] = specificationArray[3] + Indicador.getMontoPedidoVenta();
	                specificationArray[4] = specificationArray[4] + Indicador.getMontoPedidoFacturado();
	                specificationArray[5] = specificationArray[5] + Indicador.getPorcentajeMontoPedidoLogrado();
	                specificationArray[6] = specificationArray[6] + Indicador.getIngresosestimados();
	                specificationArray[7] = specificationArray[7] + Indicador.getIngresosreal();
	                specificationArray[8] = specificationArray[8] + Indicador.getPorcentajPrimerPedidoLogrado();
	                specificationArray[9] = specificationArray[9] + Indicador.getMontoVentaNeta();
	                specificationArray[10] = specificationArray[10] + Indicador.getCantidadConsultorasActivas();
	                specificationArray[11] = specificationArray[11] + Indicador.getPorcentajeActividad();
	                specificationArray[12] = specificationArray[12] + Indicador.getCantidadConsultorasReingreso();
	                specificationArray[13] = specificationArray[13] + Indicador.getCantidadConsultorasEgreso();
	                specificationArray[14] = specificationArray[14] + Indicador.getCapitalizacion();
	                specificationArray[15] = specificationArray[15] + Indicador.getPorcentajeRotacion();
	                specificationArray[16] = specificationArray[16] + Indicador.getPorcentajeRetencion();
              
	                if (specificationArray[count] == 0.0 )
	                     	specificationArray[count] = 0.0;
	                }
	        } catch (Exception e) {
	            Dialog.alert("Ex: " + e);
	        }
        return specificationArray;
    }
	  
	  public double[][] ArrayTotalesxFilaAgrup(Vector vec) {
		  double specificationArray[][] = null;
        Vector vIndicadores = null;
        Indicador Indicador;
        String campanas[] = this.mostrarCampanas();
        int camp = campanas.length;
	        try { 
	        	vIndicadores = objetos;
	            specificationArray = new double[17][camp];
	            int count = 0, contaPorcentaje;
	            for(int i = 0;i < camp; i++){
	            	contaPorcentaje = 0;
		            for (count = 0; count < vIndicadores.size(); count++) {
		            	Indicador = (Indicador) vIndicadores.elementAt(count);
		            	if(Indicador.getCampana().equals(campanas[i])){
			            	specificationArray[0][i] = specificationArray[0][i] + Sistema.round(Indicador.getCantidadPedidosVenta());
			            	specificationArray[1][i] = specificationArray[1][i] + Sistema.round(Indicador.getCantidadPediosFacturado());
			                specificationArray[2][i] = specificationArray[2][i] + Sistema.round(Indicador.getPorcentajeCantidadPedidoLogrado());
			                specificationArray[3][i] = specificationArray[3][i] + Indicador.getMontoPedidoVenta();
			                specificationArray[4][i] = specificationArray[4][i] + Indicador.getMontoPedidoFacturado();
			                specificationArray[5][i] = specificationArray[5][i] + Sistema.round(Indicador.getPorcentajeMontoPedidoLogrado());
			                specificationArray[6][i] = specificationArray[6][i] + Sistema.round(Indicador.getIngresosestimados());
			                specificationArray[7][i] = specificationArray[7][i] + Sistema.round(Indicador.getIngresosreal());
			                specificationArray[8][i] = specificationArray[8][i] + Sistema.round(Indicador.getPorcentajPrimerPedidoLogrado());
			                specificationArray[9][i] = specificationArray[9][i] + Indicador.getMontoVentaNeta();
			                specificationArray[10][i] = specificationArray[10][i] + Sistema.round(Indicador.getCantidadConsultorasActivas());
			                specificationArray[11][i] = specificationArray[11][i] + Sistema.round(Indicador.getPorcentajeActividad());
			                specificationArray[12][i] = specificationArray[12][i] + Sistema.round(Indicador.getCantidadConsultorasReingreso());
			                specificationArray[13][i] = specificationArray[13][i] + Sistema.round(Indicador.getCantidadConsultorasEgreso());
			                specificationArray[14][i] = specificationArray[14][i] + Indicador.getCapitalizacion();
			                specificationArray[15][i] = specificationArray[15][i] + Sistema.round(Indicador.getPorcentajeRotacion());
			                specificationArray[16][i] = specificationArray[16][i] + Sistema.round(Indicador.getPorcentajeRetencion());
			                contaPorcentaje++;
//			                if (specificationArray[count][i] == 0.0 )
//			                     	specificationArray[count][i] = 0.0;   
		            	 }
		            }
            		specificationArray[2][i] = Sistema.round(specificationArray[2][i] / contaPorcentaje);
            		specificationArray[5][i] = Sistema.round(specificationArray[5][i] / contaPorcentaje);
            		specificationArray[8][i] = Sistema.round(specificationArray[8][i] / contaPorcentaje);
            		specificationArray[11][i] = Sistema.round(specificationArray[11][i] / contaPorcentaje);
            		specificationArray[15][i] = Sistema.round(specificationArray[15][i] / contaPorcentaje);
            		specificationArray[16][i] = Sistema.round(specificationArray[16][i] / contaPorcentaje);
		            
	            }
	        } catch (Exception e) {
	            Dialog.alert("Ex: " + e);
	        }
        return specificationArray;
    }
	  
	  
	  public double[][] ArrayTotalesxFilaAgrupS(Vector vec) {
		  double specificationArray[][] = null;
        Vector vIndicadores = null;
        Indicador Indicador;
        String secciones[] = mostrarSecciones();
        int secc = secciones.length;
	        try { 
	        	vIndicadores = objetos;
	            specificationArray = new double[17][secc];
	            int count = 0,contaPorcentaje;
	            for(int i=0;i<secc;i++){
	            	contaPorcentaje=0;
		            for (count = 0; count < vIndicadores.size(); count++) {
		            	Indicador = (Indicador) vIndicadores.elementAt(count);
		            	if(Indicador.getUbicacion().equals(secciones[i])){
			            	specificationArray[0][i] = specificationArray[0][i] + Indicador.getCantidadPedidosVenta();
			            	specificationArray[1][i] = specificationArray[1][i] + Indicador.getCantidadPediosFacturado();
			                specificationArray[2][i] = specificationArray[2][i] + Indicador.getPorcentajeCantidadPedidoLogrado();
			                specificationArray[3][i] = specificationArray[3][i] + Indicador.getMontoPedidoVenta();
			                specificationArray[4][i] = specificationArray[4][i] + Indicador.getMontoPedidoFacturado();
			                specificationArray[5][i] = specificationArray[5][i] + Indicador.getPorcentajeMontoPedidoLogrado();
			                specificationArray[6][i] = specificationArray[6][i] + Indicador.getIngresosestimados();
			                specificationArray[7][i] = specificationArray[7][i] + Indicador.getIngresosreal();
			                specificationArray[8][i] = specificationArray[8][i] + Indicador.getPorcentajPrimerPedidoLogrado();
			                specificationArray[9][i] = specificationArray[9][i] + Indicador.getMontoVentaNeta();
			                specificationArray[10][i] = specificationArray[10][i] + Indicador.getCantidadConsultorasActivas();
			                specificationArray[11][i] = specificationArray[11][i] + Indicador.getPorcentajeActividad();
			                specificationArray[12][i] = specificationArray[12][i] + Indicador.getCantidadConsultorasReingreso();
			                specificationArray[13][i] = specificationArray[13][i] + Indicador.getCantidadConsultorasEgreso();
			                specificationArray[14][i] = specificationArray[14][i] + Indicador.getCapitalizacion();
			                specificationArray[15][i] = specificationArray[15][i] + Indicador.getPorcentajeRotacion();
			                specificationArray[16][i] = specificationArray[16][i] + Indicador.getPorcentajeRetencion();
			                contaPorcentaje++;
//			                if (specificationArray[count][i] == 0.0 )
//			                     	specificationArray[count][i] = 0.0;   
		            	 }
	            	}
            		specificationArray[2][i] = Sistema.round(specificationArray[2][i] / contaPorcentaje);
            		specificationArray[5][i] = Sistema.round(specificationArray[5][i] / contaPorcentaje);
            		specificationArray[8][i] = Sistema.round(specificationArray[8][i] / contaPorcentaje);
            		specificationArray[11][i] = Sistema.round(specificationArray[11][i] / contaPorcentaje);
            		specificationArray[15][i] = Sistema.round(specificationArray[15][i] / contaPorcentaje);
            		specificationArray[16][i] = Sistema.round(specificationArray[16][i] / contaPorcentaje);   
	            }
	        } catch (Exception e) {
	            Dialog.alert("Ex: " + e);
	        }
        return specificationArray;
    }
	  
	  public String[] mostrarCampanas(){
		   	 Vector vIndicadores = null;
		        Indicador indicadores;
		        String specificationArray[] = null;
		        
		        try { 
		       	 vIndicadores = objetos;
		           specificationArray = new String[vIndicadores.size()];
		           int count = 0;
		           for (count = 0; count < specificationArray.length; count++) {
		        	   indicadores = (Indicador) vIndicadores.elementAt(count);
		        	   specificationArray[count] = indicadores.getCampana();  
		        	   if (specificationArray[count] == null)
		        		   specificationArray[count] = "";
		               }
			    } catch (Exception e) {
			    	Dialog.alert("Ex: " + e);
			    }  
			 return this.getCampanasFiltro(specificationArray);   
	 }
	  
	    private String[] getCampanasFiltro(String[] arr){
			Vector v = new Vector();
			for(int i=0;i<arr.length;i++){
				if(i>0){
					if(!this.isExiste(arr[i], v)){
						v.addElement(arr[i]);
					}
				}else{
					v.addElement(arr[i]);
				}
			}	
			String[] str = new String[v.size()];
			for(int i=0;i<str.length;i++){
				str[i]=(String) v.elementAt(i);
			}
	    	return str;
	    } 
		private boolean isExiste(String str,Vector v){
			for(int i=0;i<v.size();i++){
					if(v.elementAt(i).equals(str))
						return true;
			}
			return false;
		}
	  
	  public static double[] ArrayTotalesMontos() {
	       
	        double specificationArray[] = null;
	        Vector vIndicadores = null;
	        Indicador Indicador;
		        try { 
		        	vIndicadores = objetos;
		            specificationArray = new double[9];
		            int n = 0;
		            for (int count = 0; count < vIndicadores.size(); count++) {
		            	Indicador = (Indicador) vIndicadores.elementAt(count);

		            	specificationArray[n] = specificationArray[n] + Indicador.getCantidadConsultorasReingreso();
		                specificationArray[n] = specificationArray[n] + Indicador.getCantidadPediosFacturado();
		                specificationArray[n] = specificationArray[n] + Indicador.getPorcentajeCantidadPedidoLogrado();
		                n++;
		                specificationArray[n] = specificationArray[n] + Indicador.getMontoPedidoVenta();
		                specificationArray[n] = specificationArray[n] + Indicador.getMontoPedidoFacturado();
		                specificationArray[n] = specificationArray[n] + Indicador.getPorcentajeMontoPedidoLogrado();
		                n++;
		                specificationArray[n] = specificationArray[n] + Indicador.getIngresosestimados();
		                specificationArray[n] = specificationArray[n] + Indicador.getIngresosreal();
		                specificationArray[n] = specificationArray[n] + Indicador.getPorcentajPrimerPedidoLogrado();
		                n++;
		                specificationArray[n] = specificationArray[n] + Indicador.getMontoVentaNeta();
		                specificationArray[n] = specificationArray[n] + Indicador.getCantidadConsultorasActivas();
		                specificationArray[n] = specificationArray[n] + Indicador.getPorcentajeActividad();
		                n++;
		                specificationArray[n] = specificationArray[n] + Indicador.getCantidadConsultorasReingreso();
		                specificationArray[n] = specificationArray[n] + Indicador.getCantidadConsultorasEgreso();
		                specificationArray[n] = specificationArray[n] + Indicador.getCapitalizacion();
		                //n++;
		                specificationArray[n] = specificationArray[n] + Indicador.getPorcentajeRotacion();
		                specificationArray[n] = specificationArray[n] + Indicador.getPorcentajeRetencion();
		                n++;
		                if (specificationArray[count] == 0.0 )
		                     	specificationArray[count] = 0.0;
		                }
		        } catch (Exception e) {
		            Dialog.alert("Ex: " + e);
		        }
	        return specificationArray;
	  }

	  public Indicador getRemoteChild(String ubica) {
		
        HttpConnection httpConn = null;
        InputStream is = null;
        Vector child = null;
        Indicador indicador = null ;
        try {
        	
            httpConn = (HttpConnection) Connector.open(URL + Cadenas.getBIS());
            httpConn.setRequestMethod(HttpConnection.GET);
            if ( httpConn.getResponseCode() == HttpConnection.HTTP_OK ) {
                is = httpConn.openInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse( is );
                Element rootElement = document.getDocumentElement();
                rootElement.normalize();
                child = bajarNivelDetalle(rootElement.getChildNodes(), ubica);
                for(int n = 0; n < objetos.size(); n++){
                	indicador = ((Indicador)objetos.elementAt(n));
                	if(indicador.getUbicacion().equalsIgnoreCase(ubica)){
                		indicador.setIndicador(child);
                		return indicador;
                	}
                }    
                is.close();
                is = null;
            } 
            httpConn.close();
            httpConn = null;
        } catch(Exception ex) {
            try { is.close(); } catch(Exception e) { }
            is = null;
            try { httpConn.close(); } catch(Exception e) { }
            httpConn = null;
          
        }
		return indicador; 
	}

	private Vector bajarNivelDetalle(NodeList node, String ubica) {
        int n = node.getLength();
        Vector obj = null;
        for (int i = 1; i < n; i = i + 2) {
            Node contactNode = node.item(i);
            String registro = contactNode.getChildNodes().item(0).getNodeValue();
            String[] fields = Cadenas.splitSimple(registro, Cadenas.TOKEN);
            if(ubica.equals(fields[0])){
            	obj = new Vector();
	            Indicador indicador = new Indicador();
	            indicador.setUbicacion(fields[0]);
	            indicador.setCampana( fields[1] );
	            indicador.setCantidadPedidosVenta( Double.parseDouble(fields[2]) );
	            indicador.setCantidadPediosFacturado( Double.parseDouble(fields[3]) );
	            indicador.setPorcentajeCantidadPedidoLogrado( Double.parseDouble(fields[4]) );
	            indicador.setMontoPedidoVenta( Double.parseDouble(fields[5]) );
	            indicador.setMontoPedidoFacturado( Double.parseDouble(fields[6]) );
	            indicador.setPorcentajeMontoPedidoLogrado( Double.parseDouble(fields[7]) );
	            indicador.setIngresosestimados( Double.parseDouble(fields[8]) );
	            indicador.setIngresosreal( Double.parseDouble(fields[9]) );
	            indicador.setPorcentajPrimerPedidoLogrado( Double.parseDouble(fields[10]) );
	            indicador.setMontoVentaNeta( Double.parseDouble(fields[11]) );
	            indicador.setCantidadConsultorasActivas( Double.parseDouble(fields[12]) );
	            indicador.setPorcentajeActividad( Double.parseDouble(fields[13]) );
	            indicador.setCantidadConsultorasReingreso( Double.parseDouble(fields[14]) );
	            indicador.setCantidadConsultorasEgreso( Double.parseDouble(fields[15]) );
	            indicador.setCapitalizacion( Double.parseDouble(fields[16]) );
	            indicador.setPorcentajeRotacion( Double.parseDouble(fields[17]) );
	            indicador.setPorcentajeRetencion( Double.parseDouble(fields[18]) );
	            obj.addElement(indicador);
            }
        }
        return obj;
	}

}
