package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.HistorialAnotacion;
import com.belcorp.entidades.Item;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelField;

public class HistorialAnotaciones extends MainScreen implements ListFieldCallback {
	private ListField lstAnotaciones;
	private Vector jeraquia;
	private Vector lista;
	private Consultora consultora;
    private int origen;  // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
    private Configuracion conf;
	private final static int NROLINEAS = 2;
    private LabelField lblEstado;

	public HistorialAnotaciones(int orig, Consultora consultora) {
    	ConfiguracionDB confs = new ConfiguracionDB();
    	conf = confs.getObjeto();
    	confs = null;

		this.consultora = consultora;
		lstAnotaciones = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
		lstAnotaciones.setCallback(this);
		lstAnotaciones.setRowHeight( (this.getFont().getHeight() * NROLINEAS));

		try {
	        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/historialanotaciones.png"), BitmapField.FIELD_HCENTER));
		} catch(Exception e) {
			Dialog.inform("" + e.getMessage());
		}
		add(lstAnotaciones);
    	int pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        Sistema.addEstadistica(Cadenas.V1_ANOTACIONES);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_ANOTACIONES);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_ANOTACIONES);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_ANOTACIONES);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else {
        	Sistema.addEstadistica(Cadenas.BC_ANOTACIONES);
        } 
    	
    	llenaJerarquia();

        lblEstado = new mkpyLabelField("0 / " + (lstAnotaciones.getSize() - 1), LabelField.USE_ALL_WIDTH | LabelField.FIELD_RIGHT, Color.WHITE, Estilos.getBGModulo());
	    setStatus(lblEstado);
    	
	}
	
	private void llenaJerarquia() {
		jeraquia = new Vector();
		int i, n;
		Vector anotaciones = consultora.getHistorialAnotaciones();
		Item item = new Item(true, consultora.getNombre(), "", false, 0);
		n = anotaciones.size();
		for ( i = 0; i < n; i++ ) {
			HistorialAnotacion anotacion = (HistorialAnotacion) anotaciones.elementAt(i);
			item.getItems().addElement(new Item(false, anotacion.getFechaHora(), anotacion.getAnotacion(), false, 0));
		}
		jeraquia.addElement(item);
    	llenaLista();
	}
	
	private void llenaLista() {
		int n, m;
		n = jeraquia.size();
		lista = new Vector();
		for(int i = 0; i < n; i++) {
			Item padre = (Item) jeraquia.elementAt(i);
			lista.addElement(padre);
			if ( padre.isColapsado() == false ) {
				m = padre.getItems().size();
				for (int j = 0; j < m; j++) {
					Item hijo = (Item) padre.getItems().elementAt(j);
					lista.addElement(hijo);
				}
			}
		}
		lstAnotaciones.setSize(lista.size());
		lstAnotaciones.invalidate();
	}
		    
//	MenuItem opcionMenu = new MenuItem(CATEGORIA, 110, 10) {
//		public void run() {
//			llenaJerarquia(! porCampana);
//		}
//	};

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstAnotaciones) {
			lstAnotaciones.invalidate(lstAnotaciones.getSelectedIndex() + dy);
			lstAnotaciones.invalidate(lstAnotaciones.getSelectedIndex());

            if ( lstAnotaciones.getSelectedIndex() + dy < 0 ) {
                lblEstado.setText((lstAnotaciones.getSelectedIndex()) + " / " + (lstAnotaciones.getSize() - 1));
            } else if( lstAnotaciones.getSelectedIndex() + dy > lstAnotaciones.getSize() - 1 ) {
                lblEstado.setText((lstAnotaciones.getSelectedIndex()) + " / " + (lstAnotaciones.getSize() - 1));
            } else {
                lblEstado.setText((lstAnotaciones.getSelectedIndex() + dy) + " / " + (lstAnotaciones.getSize() - 1));
            }
		
		}
		return super.navigationMovement(dx, dy, status, time);
	}

	private void seleccion() {
		int index = lstAnotaciones.getSelectedIndex();
		if ( index > 0 ) {
			Item item = (Item) get(lstAnotaciones, index);
			Dialog.inform(item.getValor());
		}
	}
	
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstAnotaciones) {
			int index = lstAnotaciones.getSelectedIndex();
			Item item = (Item) get(lstAnotaciones, index);
			if ( item.isCabecera() ) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				lstAnotaciones.setSelectedIndex(index);
				return true;
			} else {
				seleccion();
				return true;
			}
		}
		return super.navigationClick(status, time);
	}
	
	public void drawListRow(ListField listField, Graphics g, int index, int y, int w) {
		String sColapsado = "";
		Item item = (Item) get(listField, index);
		if ( listField.getSelectedIndex() == index ) {
            g.setColor(Color.WHITE);        
            g.setBackgroundColor(Estilos.getBGSelected());
		} else {
			if ( item.isCabecera() ) {
	            g.setColor(Estilos.getColorInterlinea(0));        
	            g.setBackgroundColor(Estilos.getBGInterlinea(0));
			} else {
	            g.setColor(Estilos.getColorInterlinea(1));        
	            g.setBackgroundColor(Estilos.getBGInterlinea(1));
			}
	        g.clear();
		}
		if ( item.isCabecera() ) {
			if ( item.isColapsado() ) {
				sColapsado = Characters.BLACK_RIGHT_POINTING_TRIANGLE + " ";
			} else {
				sColapsado = Characters.BLACK_DOWN_POINTING_TRIANGLE + " ";
			}
		} else {
			sColapsado = Characters.SPACE + "";
		}
		g.drawText(sColapsado + item.getDescripcion(), 0, y, DrawStyle.LEFT, w);
		g.drawText(item.getValor(), 0, y + ( this.getFont().getHeight() * 1), DrawStyle.LEFT | DrawStyle.ELLIPSIS, w);
   }

	public String get(int index) {
	  return "";
	}

	public int getPreferredWidth(ListField listField) {
		return 0;
	}
	
	public int indexOfList(ListField listField, String prefix, int start) {
		return 0;
	}

	public Object get(ListField listField, int index) {
		return (Item) lista.elementAt(index);
	}
	
}
