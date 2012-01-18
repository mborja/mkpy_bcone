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
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.entidades.Concurso;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Item;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;

public class ConsultaPuntaje extends MainScreen  implements ListFieldCallback {
	static String[] listado;
	private ListField lstPuntaje;
	private Consultora consultora;
	private Vector jeraquia;
	private Vector lista;
	
	public ConsultaPuntaje(int origen, Consultora consultora) {
		this.consultora = consultora;
		lstPuntaje = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
		lstPuntaje.setCallback(this);
		lstPuntaje.setSearchable(true);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/detallepuntaje.png"), BitmapField.FIELD_HCENTER));
	    add(lstPuntaje);

    	int pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        Sistema.addEstadistica(Cadenas.V1_PUNTAJE);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_PUNTAJE);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_PUNTAJE);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_PUNTAJE);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else if( origen == 1 ) {
	        Sistema.addEstadistica(Cadenas.BC_PUNTAJE);
        } else if ( origen == 2 ) {
	        Sistema.addEstadistica(Cadenas.GL_PUNTAJE);
        } 
	    
	    llenaJerarquia();
    }
	    
	private void llenaJerarquia() {
		Item item;
		jeraquia = new Vector();
		int i, n;
		n = consultora.getConcursos().size();
		for ( i = 0; i < n; i++ ) {
			Concurso concurso = (Concurso) consultora.getConcursos().elementAt(i);
			item = new Item(true, concurso.getDescripcion(), "", false, i);
			item.getItems().addElement(new Item(false, concurso.getPremio(), "", false, i));
			item.getItems().addElement(new Item(false, concurso.getResultado(), concurso.getNivel(), false, i));
			item.getItems().addElement(new Item(false, concurso.getCodigo(), concurso.getPuntaje(), false, i));
			item.getItems().addElement(new Item(false, concurso.getCampanaInicio() + " - " + concurso.getCampanaFin(), "", false, i));
			
			jeraquia.addElement(item);
		}
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
		lstPuntaje.setSize(lista.size());
		lstPuntaje.invalidate();
	}
	
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstPuntaje) {
			lstPuntaje.invalidate(lstPuntaje.getSelectedIndex() + dy);
			lstPuntaje.invalidate(lstPuntaje.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
		
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstPuntaje) {
			int index = lstPuntaje.getSelectedIndex();
			Item item = (Item) get(lstPuntaje, index);
			if ( item.isCabecera() ) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				lstPuntaje.setSelectedIndex(index);
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
		g.drawText(item.getValor(), 0, y, DrawStyle.RIGHT, w);
   }
		  
	public Object get(ListField listField, int index) {
		return (Item) lista.elementAt(index);
	}

	public int getPreferredWidth(ListField listField) {
		return 0;
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		return 0;
	}
}