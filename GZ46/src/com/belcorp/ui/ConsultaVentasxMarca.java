package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Item;
import com.belcorp.entidades.PedidosxMarca;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;

public class ConsultaVentasxMarca extends MainScreen implements ListFieldCallback {
	private ListField lstVentasxMarca;
	private Consultora consultora;
	private boolean porCampana;
	private final static String CAMPANA = "Por Campaña"; 
	private final static String MARCA = "Por Marca"; 
	
	private Vector jeraquia;
	private Vector lista;
      
	public ConsultaVentasxMarca(int origen, Consultora consultora) {
		this.consultora = consultora;
		lstVentasxMarca = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
		lstVentasxMarca.setCallback(this);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/ventaxmarca.png"), BitmapField.FIELD_HCENTER));
	    add(lstVentasxMarca);
	    addMenuItem(opcionMenu);

    	int pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        Sistema.addEstadistica(Cadenas.V1_VENTAXMARCA);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_VENTAXMARCA);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_VENTAXMARCA);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_VENTAXMARCA);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else if( origen == 1 ) {
	        Sistema.addEstadistica(Cadenas.BC_VENTAXMARCA);
        } else if ( origen == 2 ) {
	        Sistema.addEstadistica(Cadenas.GL_VENTAXMARCA);
        } 
	    
	    llenaJerarquia(true);
	}
	
	private void llenaJerarquia(boolean opcion) {
		Item item;
		porCampana = opcion;
		jeraquia = new Vector();
		int i, n;
		n = consultora.getPedidosxMarca().size();
		if ( opcion == true ) {
	    	opcionMenu.setText(MARCA);
			for ( i = 0; i < n; i++ ) {
				PedidosxMarca pedido = (PedidosxMarca) consultora.getPedidosxMarca().elementAt(i);
				item = new Item(true, pedido.getCampanaFormato(), "", Cadenas.getMoneda(), false, i);
				item.getItems().addElement(new Item(false, "Esika", Sistema.round(pedido.getPorcentajeVentaEsika()) + "%", 
						 "" + pedido.getVentaEsika(), false, i));
				item.getItems().addElement(new Item(false, "LBel", Sistema.round(pedido.getPorcentajeVentaEbel()) + "%", 
						"" + pedido.getVentaEbel(), false, i));
				item.getItems().addElement(new Item(false, "Cyzone", Sistema.round(pedido.getPorcentajeVentaCyzone()) + "%", 
						"" + pedido.getVentaCyzone(), false, i));
				item.getItems().addElement(new Item(false, "Otros", Sistema.round(pedido.getPorcentajeOtros()) + "%", 
						"" + pedido.getVentaOtros(), false, i));
				jeraquia.addElement(item);
			}
		} else {
	    	opcionMenu.setText(CAMPANA);
			double total = 0;
			item = new Item(true, "Esika", "", "" + total, false, 0);
			for ( i = 0; i < n; i++ ) {
				PedidosxMarca pedidosxMarca = (PedidosxMarca) consultora.getPedidosxMarca().elementAt(i);
				item.getItems().addElement(new Item(false, pedidosxMarca.getCampanaFormato(), Sistema.round(pedidosxMarca.getPorcentajeVentaEsika()) + "%", "" + pedidosxMarca.getVentaEsika(), false, 0));
				total += pedidosxMarca.getVentaEsika();
			}
			total = Sistema.round(total);
			item.setUbicacion(Cadenas.getMoneda() + total);
			jeraquia.addElement(item);
			total = 0;
			item = new Item(true, "LBel", "", "" + total, false, 0);
			for ( i = 0; i < n; i++ ) {
				PedidosxMarca pedidosxMarca = (PedidosxMarca) consultora.getPedidosxMarca().elementAt(i);
				item.getItems().addElement(new Item(false, pedidosxMarca.getCampanaFormato(), Sistema.round(pedidosxMarca.getPorcentajeVentaEbel()) + "%", "" + pedidosxMarca.getVentaEbel(), false, 0));
				total += pedidosxMarca.getVentaEbel();
			}
			total = Sistema.round(total);
			item.setUbicacion(Cadenas.getMoneda() + total);
			jeraquia.addElement(item);
			total = 0;
			item = new Item(true, "Cyzone", "", "" + total, false, 0);
				for ( i = 0; i < n; i++ ) {
				PedidosxMarca pedidosxMarca = (PedidosxMarca) consultora.getPedidosxMarca().elementAt(i);
				item.getItems().addElement(new Item(false, pedidosxMarca.getCampanaFormato(), Sistema.round(pedidosxMarca.getPorcentajeVentaCyzone()) + "%", "" + pedidosxMarca.getVentaCyzone(), false, 0));
				total += pedidosxMarca.getVentaCyzone();
			}
			total = Sistema.round(total);
			item.setUbicacion(Cadenas.getMoneda() + total);
			jeraquia.addElement(item);
			total = 0;
			item = new Item(true, "Otros", "", "" + total, false, 0);
			for ( i = 0; i < n; i++ ) {
				PedidosxMarca pedidosxMarca = (PedidosxMarca) consultora.getPedidosxMarca().elementAt(i);
				item.getItems().addElement(new Item(false, pedidosxMarca.getCampanaFormato(), Sistema.round(pedidosxMarca.getPorcentajeOtros()) + "%", "" + pedidosxMarca.getVentaOtros(), false, 0));
				total += pedidosxMarca.getVentaOtros();
			}
			total = Sistema.round(total);
			item.setUbicacion(Cadenas.getMoneda() + total);
			jeraquia.addElement(item);
		}
		llenaLista();
	}


	MenuItem opcionMenu = new MenuItem(MARCA, 110, 10) {
		public void run() {
			llenaJerarquia(! porCampana);
		}
	}; 
	
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
		lstVentasxMarca.setSize(lista.size());
	    lstVentasxMarca.invalidate();
	}
	
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstVentasxMarca) {
			lstVentasxMarca.invalidate(lstVentasxMarca.getSelectedIndex() + dy);
			lstVentasxMarca.invalidate(lstVentasxMarca.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
		
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstVentasxMarca) {
			int index = lstVentasxMarca.getSelectedIndex();
			Item item = (Item) get(lstVentasxMarca, index);
			if ( item.isCabecera() ) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				lstVentasxMarca.setSelectedIndex(index);
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
		g.drawText(item.getValor(), 0, y, DrawStyle.HCENTER, w);
		g.drawText(item.getUbicacion(), 0, y, DrawStyle.RIGHT, w);
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
