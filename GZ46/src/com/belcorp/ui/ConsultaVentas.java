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

import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Item;
import com.belcorp.entidades.PedidosVenta;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;

public class ConsultaVentas extends MainScreen implements ListFieldCallback {
	private ListField lstVentas;
	private Vector jeraquia;
	private Vector lista;
	private Consultora consultora;
	private boolean porCampana; // true es por campana, false = por categoria
	private final static String CAMPANA = "Por Campaña"; 
	private final static String CATEGORIA = "Por Categoría"; 
    private int origen;  // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
    private Configuracion conf;

	public ConsultaVentas(int origen, Consultora consultora) {
    	ConfiguracionDB confs = new ConfiguracionDB();
    	conf = confs.getObjeto();
    	confs = null;

		this.consultora = consultora;
		lstVentas = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
	    lstVentas.setCallback(this);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/venta.png"), BitmapField.FIELD_HCENTER));
		add(lstVentas);
    	addMenuItem(opcionMenu);
    	int pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        Sistema.addEstadistica(Cadenas.V1_VENTA);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_VENTA);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_VENTA);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_VENTA);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else if( origen == 1 ) {
	        Sistema.addEstadistica(Cadenas.BC_VENTA);
        } else if ( origen == 2 ) {
	        Sistema.addEstadistica(Cadenas.GL_VENTA);
        } 
    	
    	llenaJerarquia(true);
	}
	
	/**
	 * opcion == true campañas, false == categorias
	 * @param opcion
	 */
	private void llenaJerarquia(boolean opcion) {
		Item item;
		porCampana = opcion;
		jeraquia = new Vector();
		int i, n;
		Vector pedidos = consultora.getPedidos();
		n = pedidos.size();
		if ( opcion == true ) {
	    	opcionMenu.setText(CATEGORIA);
			for ( i = 0; i < n; i++ ) {
				PedidosVenta pedido = (PedidosVenta) pedidos.elementAt(i);
				item = new Item(true, pedido.getCampanaFormato(), Cadenas.getMoneda(), false, i);
				item.getItems().addElement(new Item(false, "Venta", "" + pedido.getVenta(), false, i));
				item.getItems().addElement(new Item(false, "Ganancia", "" +pedido.getGanancia(), false, i));
				if ( conf.getMostrarAsistencia().equals("1") ) {
					item.getItems().addElement(new Item(false, "Asistencia", "" + pedido.getAsistencia(), false, i));
				}
				jeraquia.addElement(item);
			}
		    lstVentas.setSize( ( n * 3 ) + n, 0);
		} else {
	    	opcionMenu.setText(CAMPANA);
			double total = 0;
			//INICIO: PARA LA VENTA
			item = new Item(true, "Venta", "", false, 0);
			for ( i = 0; i < n; i++ ) {
				PedidosVenta pedido = (PedidosVenta) pedidos.elementAt(i);
				item.getItems().addElement(new Item(false, pedido.getCampanaFormato(), "" + pedido.getVenta(), false, 0));
				total += pedido.getVenta();
			}
			total = Sistema.round(total);
			item.setValor(Cadenas.getMoneda() + total);
			jeraquia.addElement(item);
			//FIN: PARA LA VENTA
			total = 0;
			//INICIO: PARA LA GANANCIA
			item = new Item(true, "Ganancia", "", false, 1);
			for ( i = 0; i < n; i++ ) {
				PedidosVenta pedido = (PedidosVenta) pedidos.elementAt(i);
				item.getItems().addElement(new Item(false, pedido.getCampanaFormato(), "" + pedido.getGanancia(), false, 1));
				total += pedido.getGanancia();
			}
			total = Sistema.round(total);
			item.setValor(Cadenas.getMoneda() + total);
			jeraquia.addElement(item);
			//FIN: PARA LA GANANCIA
			if ( conf.getMostrarAsistencia().equals("1") ) {
				//INICIO: PARA LA ASISTENCIA
				item = new Item(true, "Asistencia", "", false, 2);
				for ( i = 0; i < n; i++ ) {
					PedidosVenta pedido = (PedidosVenta) pedidos.elementAt(i);
					item.getItems().addElement(new Item(false, pedido.getCampanaFormato(), "" + pedido.getAsistencia(), false, 2));
				}
				jeraquia.addElement(item);
				//FIN: PARA LA ASISTENCIA
			}
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
	    lstVentas.setSize(lista.size());
		lstVentas.invalidate();
	}
		    
	MenuItem opcionMenu = new MenuItem(CATEGORIA, 110, 10) {
		public void run() {
			llenaJerarquia(! porCampana);
		}
	};

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstVentas) {
			lstVentas.invalidate(lstVentas.getSelectedIndex() + dy);
			lstVentas.invalidate(lstVentas.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
		
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstVentas) {
			int index = lstVentas.getSelectedIndex();
			Item item = (Item) get(lstVentas, index);
			if ( item.isCabecera() ) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				lstVentas.setSelectedIndex(index);
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
