package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;
import com.belcorp.dao.ConsultoraDB;
import com.belcorp.entidades.Consultora;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelField;
import com.makipuray.ui.mkpyStatusProgress;

public class ConsultaSeccion extends MainScreen implements ListFieldCallback {
    private final static int NROLINEAS = 2, DELTA = 0;
    private final static String VISITADA = "VE";
    private ListField lstConsultoras, lstSeccion;
    private String idSeccion, idNivel, idEstado, deuda, estadoPedido, pasoPedido;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
       
    private ConsultoraDB consultoras;
    private static Vector consultorasCS;
    private MainScreen menuContactos;
    private MainScreen menuSeccion;
    private LabelField lblEstado;
    
    private boolean isEstablecidas = false;
    private boolean generarLista = false;
    
    private int origen = 0; // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion

    /*
     * Constructor para la llamada desde nuevas
     * 
     * 
     */
    public ConsultaSeccion(String idClasificacion, String seccion, MainScreen menuContactos, MainScreen menuSeccion) {
        this.idSeccion = seccion;        
        this.menuContactos = menuContactos;
        this.menuSeccion = menuSeccion;
        consultoras = new ConsultoraDB();
        consultoras.setClasificacion(idClasificacion);
        consultoras.setIdSeccion(seccion);
        consultoras.setVisitada(VISITADA);
        consultorasCS = new Vector();
        consultorasCS = consultoras.getConsultoras();
    	if ( consultorasCS.size() == 0 ) {
    		Dialog.inform("No se encontraron resultados.");
    		close();
    	} else {
            cuerpoSeccion(idClasificacion, seccion, false);
    	}

    	origen = 0;  // nuevas
		switch( Integer.parseInt(idClasificacion) ) {
		case 1:
	        Sistema.addEstadistica(Cadenas.V1_LISTACONSULTORA);
			break;
		case 2:
	        Sistema.addEstadistica(Cadenas.V2_LISTACONSULTORA);
			break;
		case 3:
	        Sistema.addEstadistica(Cadenas.V3_LISTACONSULTORA);
			break;
		case 4:
	        Sistema.addEstadistica(Cadenas.V4_LISTACONSULTORA);
			break;
		case 5:
	        //Sistema.addEstadistica(Cadenas.V5_LISTACONSULTORA);
			break;
		}
    
    }
    
    /*
     * 
     * Constructor para la llamada de Buscar consultora
     * 
     */
    public ConsultaSeccion(String seccion, String nombre, String DocIdent, String CodigoConsultora, boolean isEstablecidas) {
    	this.idSeccion = seccion;
        this.isEstablecidas = isEstablecidas;
    	consultoras = new ConsultoraDB();
    	consultorasCS = new Vector();
    	consultorasCS = consultoras.getRemoteBusqueda(seccion, nombre, DocIdent, CodigoConsultora);
    	if(consultorasCS == null){
    		Dialog.inform(Cadenas.getMsjDatosLocales());
        	consultoras.setIdSeccion(seccion);
        	consultoras.setIdNombre(nombre);
        	consultoras.setIdDocIdent(DocIdent);
        	consultoras.setIdCodConsultora(CodigoConsultora);
        	consultorasCS = new Vector();
        	consultorasCS = consultoras.getConsultoras();
        	//consultorasCS = consultoras.getCantidadPorVisitar(consultorasCS);
    	}
    	if ( consultorasCS.size() == 0 ) {
    		Dialog.inform("No se encontraron resultados.");
    		close();
    	} else {
        	cuerpoSeccion("0", seccion, false);
    	}

    	origen = 1;  // buscar
    	
        Sistema.addEstadistica(Cadenas.BC_LISTACONSULTORA);
        
    }
 
    /*
     * Constructor para la llamada de generar lista
     * 
     */
    public ConsultaSeccion(String campana, String seccion, String nivel, String estado, String deud, String estPedido) {
    	idSeccion = seccion;
    	idNivel = nivel;
    	idEstado = estado;
    	deuda = deud;
    	estadoPedido = estPedido;
    	consultoras = new ConsultoraDB();
    	consultorasCS = new Vector();
    	consultorasCS = consultoras.getRemoteBusqueda(campana, seccion, idNivel, idEstado, deuda, estadoPedido);
    	if(consultorasCS == null){
    		Dialog.inform(Cadenas.getMsjDatosLocales());
        	consultoras.setIdSeccion(seccion);
        	consultoras.setIdNivel(idNivel);
        	consultoras.setIdEstado(idEstado);
        	consultoras.setIdDeuda(deuda);
        	consultoras.setEstadoPedido(estadoPedido);
        	consultorasCS = new Vector();
        	consultorasCS = consultoras.getConsultorasGenerar();
        	//consultorasCS = consultoras.getCantidadPorVisitar(consultorasCS);
    	}
    	if ( consultorasCS.size() == 0 ) {
    		Dialog.inform("No se encontraron resultados.");
    		close();
    	} else {
    		if ( estPedido.equals("1") || estPedido.equals("2") || estPedido.equals("") ) {
            	cuerpoSeccion("0", null, true);
    		} else {
            	cuerpoSeccion("0", null, false);
    		}
    	}
    	origen = 2;  // generar lista y facturar
    	if ( seccion.equals("") && idNivel.equals("") && idEstado.equals("") && deuda.equals("") && estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORA);
    	} else if ( !seccion.equals("") && idNivel.equals("") && idEstado.equals("") && deuda.equals("") && estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORASECCION);
    	} else if ( seccion.equals("") && !idNivel.equals("") && idEstado.equals("") && deuda.equals("") && estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORANIVEL);
    	} else if ( seccion.equals("") && idNivel.equals("") && !idEstado.equals("") && deuda.equals("") && estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORAESTADO);
    	} else if ( seccion.equals("") && idNivel.equals("") && idEstado.equals("") && !deuda.equals("") && estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORADEUDA);
    	} else if ( seccion.equals("") && idNivel.equals("") && idEstado.equals("") && deuda.equals("") && !estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORAPEDIDO);
    	} else if ( !seccion.equals("") || !idNivel.equals("") || !idEstado.equals("") || !deuda.equals("") || !estadoPedido.equals("") ) {
            Sistema.addEstadistica(Cadenas.GL_LISTACONSULTORA);
    	}
    }

    /*
     * 
     * 
     */
//    public ConsultaSeccion() {
//    	 cuerpoSeccion("0", null, false);
//    }
    
    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
    	if ( list == lstSeccion ) {
            g.setColor(Color.WHITE);
            g.setBackgroundColor(Estilos.getBGModulo());
            g.clear();
            g.drawText("Seccion " + idSeccion, 0, y, DrawStyle.HCENTER, w);
    	}
    	if ( list == lstConsultoras ) {
            Consultora consultora = get(index);
    		if ( list.getSelectedIndex() == index ) {
                g.setColor(Color.WHITE);        
                g.setBackgroundColor(Estilos.getBGSelected());
    		} else {
                g.setColor(Estilos.getColorInterlinea(index));        
                g.setBackgroundColor(Estilos.getBGInterlinea(index));
                g.clear();
    		}
            g.drawText(consultora.getNombre(), 0, y, DrawStyle.LEFT, w);
            g.drawText(consultora.getCodigo(), 0, y + this.getFont().getHeight() + DELTA, DrawStyle.LEFT, w);
            g.drawText(Cadenas.getMoneda().concat(String.valueOf(consultora.getSaldo())), 0, y + this.getFont().getHeight() + DELTA, DrawStyle.RIGHT, w);
            if ( generarLista ) {
                g.drawText(consultora.getOperacion(), 0, y + ( this.getFont().getHeight() * 2) + DELTA, DrawStyle.LEFT, w);
            }
    	}
    }

    public Consultora get(int index) {    	
        return (Consultora) consultorasCS.elementAt(index) ;
    }

    public int getPreferredWidth(ListField arg0) {
        return 0;
    }

    public int indexOfList(ListField list, String prefix, int start) {
        return _findByPrefix(prefix);
    }

    public Object get(ListField listField, int index) {
        return null;
    }

    public static int _findByPrefix(String prefix) {
        int i, n;
        String _Name = "";
        n = consultorasCS.size();
        for ( i = 0; i < n; i++) {
            Consultora oConsultora = (Consultora)consultorasCS.elementAt(i);
            _Name = oConsultora.getNombre().toUpperCase();
            if ( _Name.startsWith(prefix.toUpperCase()) ) {
                return i;
            }
        }
        return -1;  
    }        

    private void seleccion() {
        Consultora consultoraFiltro = (Consultora) consultorasCS.elementAt(lstConsultoras.getSelectedIndex());
        Consultora consultora = null;
        String codId = consultoraFiltro.getId();
        if ( codId.trim().equals("") ) {
        	codId = consultoraFiltro.getCodigo();
        }
//TODO:verificar que funciona bien sin esta linea        
        consultora = consultoraBusquedaLocal(codId); 
        if(consultora == null) {
    		progress.open();
    		progress.setTitle("Buscando...");
        	consultora = consultoras.getRemoteConsultora(codId);
    		progress.close();
        } else {
        	consultora.setSaldo(consultoraFiltro.getSaldo());
        	consultoras.commit();
        }
        if ( consultora == null) {
        	Dialog.inform("Consultora retirada");
        } else {
            Estilos.pushScreen(new DatosConsultora(origen, consultora, menuContactos, menuSeccion, this, isEstablecidas));
        }
    }
    
	private Consultora consultoraBusquedaLocal(String conId) {
		Consultora consultora;
		//consultoras = new ConsultoraDB();
		Vector vConsultora;
		vConsultora =  consultoras.getObjetos();
        int i, n;
        n = vConsultora.size();
        for (i = 0; i < n; i++) {
            consultora = (Consultora) vConsultora.elementAt(i);
            if(conId.equals(consultora.getId())){
               return consultora;
            }
        }
		return null;
	}

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == lstConsultoras) {
            lstConsultoras.invalidate(lstConsultoras.getSelectedIndex() + dy);
            lstConsultoras.invalidate(lstConsultoras.getSelectedIndex());
            if ( lstConsultoras.getSelectedIndex() + dy < 0 ) {
                lblEstado.setText((lstConsultoras.getSelectedIndex() + 1) + " / " + lstConsultoras.getSize());
            } else if( lstConsultoras.getSelectedIndex() + dy > lstConsultoras.getSize() - 1 ) {
                lblEstado.setText((lstConsultoras.getSelectedIndex() + 1) + " / " + lstConsultoras.getSize());
            } else {
                lblEstado.setText((lstConsultoras.getSelectedIndex() + dy + 1) + " / " + lstConsultoras.getSize());
            }
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstConsultoras) {
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}
	
    public void cuerpoSeccion(String idClasificacion, String seccion, boolean generaLista) {
    	long estilos;
    	generarLista = generaLista;
    	if(seccion == "-1") 
    		idSeccion = "Todas";
    	if ( idClasificacion.equals("0") ) {
            add(new BitmapField(Bitmap.getBitmapResource("img/titulos/resultadosbusqueda.png"), BitmapField.FIELD_HCENTER));
    	} else {
            add(new BitmapField(Bitmap.getBitmapResource(Cadenas.getClasificacionMetodologica(idClasificacion)), BitmapField.FIELD_HCENTER));
    	}
	    if(consultorasCS.size() == 0)
	    	estilos = ListField.NON_FOCUSABLE;
	    else
	    	estilos = ListField.FIELD_HCENTER;
	    lstConsultoras = new ListField(consultorasCS.size(), estilos);
	    if ( generaLista ) {
		    lstConsultoras.setRowHeight( (this.getFont().getHeight() * (NROLINEAS + 1)) + DELTA );
	    } else {
		    lstConsultoras.setRowHeight( (this.getFont().getHeight() * NROLINEAS) + DELTA );
	    }

	    if(seccion != null) { 
	    	lstSeccion = new ListField(1, Field.NON_FOCUSABLE | LabelField.FIELD_HCENTER | LabelField.USE_ALL_WIDTH);
	    	lstSeccion.setCallback(this);
	    	add(lstSeccion);
	    }
	    lstConsultoras.setCallback(this);
	    lstConsultoras.setSearchable(true);
        lblEstado = new mkpyLabelField("1 / " + lstConsultoras.getSize(), LabelField.USE_ALL_WIDTH | LabelField.FIELD_RIGHT, Color.WHITE, Estilos.getBGModulo());
	    add(lstConsultoras);
	    setStatus(lblEstado);
    
    }
}
