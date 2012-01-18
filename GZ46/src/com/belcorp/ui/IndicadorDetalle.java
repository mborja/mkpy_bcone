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
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;
import com.belcorp.dao.IndicadorDB;
import com.belcorp.entidades.Indicador;
import com.belcorp.entidades.Item;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;

/***
 * listado solo por detalle
 * 
 * @author Franco
 * 
 */
public class IndicadorDetalle extends MainScreen implements ListFieldCallback {
	private static ListField lstIndicadores;
	static String[] sCategorias = MetodosGlobales.lsCatIndicadores();
	private Vector jerarquia;
	private Vector lista;
	private Indicador ChildIndicador;
	private String menuTitulo;
	private int contDetalleClick = 0;
	private IndicadorDB indicadores = new IndicadorDB();
	private MetodosGlobales meGlobales = new MetodosGlobales();
	private boolean child = false, opcionMenuItem = false;
	private String campana = "";

	public IndicadorDetalle() {
		add(new BitmapField(Bitmap.getBitmapResource("img/titulos/indicadoresresumen.png"), BitmapField.FIELD_HCENTER));
		lstIndicadores = new ListField(0, ListField.FIELD_HCENTER
				| ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
		lstIndicadores.setCallback(this);
		lstIndicadores.setSearchable(true);
		add(lstIndicadores);
		llenaJerarquia(true);
	}

//	private void menus(MenuItem item) {
//		removeAllMenuItems();
//		if ((!child) ) {//&& (v)
//			addMenuItem(item);
			// si no es GZ y no esta el la lista por categoria
//			if (meGlobales.getPerfil() != MetodosGlobales.perfilGZ() ) //&& (!v)
//				addMenuItem(_verDetalle);
//		}
		// si es DV y esta por detalle
//		if (meGlobales.getPerfil() == MetodosGlobales.perfilDV() && (child))
//			if (cntDetalleClick < 3) { // si es Dv solo tiene 2 detalles GR y DV
//				cntDetalleClick++;
//				addMenuItem(_verDetalle);
//			}
//	}
	
	private void llenaJerarquia(boolean opcion) {
		opcionMenuItem = opcion;
		Item item;
		jerarquia = new Vector();
		int secciones_size, categoria_size, c;
		Indicador indic;
		double totalesAgrup[][] = indicadores.ArrayTotalesxFilaAgrup(null);
		String[] sCategorias = MetodosGlobales.lsCatIndicadores();
		categoria_size = sCategorias.length;
    	String[] secciones = IndicadorDB.mostrarSecciones();
    	String[] campanas = indicadores.mostrarCampanas();
		int campana_size;
		secciones_size = secciones.length;
		campana_size = campanas.length;
		menus();
		if (opcion) {// resumen
	        Sistema.addEstadistica(Cadenas.IND_CAMPANA);
			menuTitulo = "Por categoría"; 
			opcionMenu.setText(menuTitulo);
	    	for (c = 0; c < campana_size; c++ ) {
	    		item = new Item(true, campanas[c], "", false, c);
	        	for(int k = 0; k < categoria_size; k++){
	        		switch(k){
	        		case 3:
	        		case 4:
//	        		case 6:
//	        		case 7:
	        		case 9:
//	        		case 14:
						item.getItems().addElement(new Item(false, sCategorias[k], Cadenas.getMoneda() + Sistema.round(totalesAgrup[k][c]), false, c));
	        			break;
	        		default:
						item.getItems().addElement(new Item(false, sCategorias[k], "" + Sistema.round(totalesAgrup[k][c]), false, c));
	        		}
	        	}
	        	jerarquia.addElement(item);
	    	}			
		} else { // seccion o estados
			Vector vec;
//    		if (opcionMenuItem) { // estados
//				menuTitulo = "Por sección"; 
//				opcionMenu.setText(menuTitulo);
				
//				if (child) { // si el valor es true, es una lista x detalle
					//***vec = ChildEstadoIndicador.getEstadoIndicador();
//					menus();
//				} else {
					//***vec = esIndicadorDB.getObjetos();
//				}			
//			} 		
//			else { 			
				// seccion
				Sistema.addEstadistica(Cadenas.IND_CATEGORIA);
				menuTitulo = "Por Campaña"; 
				opcionMenu.setText(menuTitulo);
				//menus();
				//removeAllMenuItems();
				if (child) { // si el valor es true, es una lista x detalle
					vec = ChildIndicador.getIndicador();
					menus();
				} else {
					//vec = indicadores.getObjetos();
					vec = indicadores.getObjetosByCampana(campana);
				}
				c = vec.size();
			    secciones_size = secciones.length;
				sCategorias = MetodosGlobales.lsCatIndicadores();
				categoria_size = sCategorias.length;
				
				int e, k;
				//double totales[] = facturaciones.ArrayTotalesxFila(vFacturacion);

				/////n = secciones.length;
				e = vec.size();
				String cat, tot;
				for (c = 0; c < categoria_size; c++ ) { //recorre las categorias
				   cat = sCategorias[c];
				   item = new Item(true, cat, "", false, c); //totales
				   double subtotal = 0;
				   for(k = 0; k < e; k++){ //recorre las facturas
					   indic = (Indicador) vec.elementAt(k);
					   double importe = importe(cat, indic);
					   //item.getItems().addElement(new Item(false, indic.getCampana() + " - " + indic.getUbicacion(), String.valueOf(Sistema.round(importe)), false, c));  //unidad
					   item.getItems().addElement(new Item(false, indic.getCampana(), String.valueOf(Sistema.round(importe)), false, c));  //unidad
					   subtotal += importe;
				   }
				   
				   switch(c){
				    case 3:
				    case 4:
				    case 6:
				    case 7:
				    case 9:
				    case 14:
				    	item.setValor(String.valueOf(Cadenas.getMoneda() + Sistema.round(subtotal)));
						   break;
				    default:
				    	item.setValor(String.valueOf(Sistema.round(subtotal)));
				   } 	  
				   
				   jerarquia.addElement(item);	
				}
					/*
				    for(int k=0; k<campana_size;k++){
				    	Nombrecampana = campanas[k];
				    	for (c = 0; c < secciones_size; c++ ) {// recorremos todas las secciones
					    	secc = secciones[c]; 
							indic = (Indicador) vec.elementAt(c);
								ubica = indic.getUbicacion();
								if(secc.equals(ubica) && indic.getCampana().equals(campanas[k])){
									item = new Item(true, Nombrecampana.concat(" - ").concat(secc) , Cadenas.getMoneda(), false, 0);//cabecera seccon con tipo de moneda
									item.getItems().addElement(new Item(false, sCategorias[0],String.valueOf(Sistema.round(indic.getCantidadPedidosVenta())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[1],String.valueOf(Sistema.round(indic.getCantidadPediosFacturado())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[2],String.valueOf(Sistema.round(indic.getPorcentajeCantidadPedidoLogrado())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[3],String.valueOf(indic.getMontoPedidoVenta()), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[4],String.valueOf(indic.getMontoPedidoFacturado()), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[5],String.valueOf(Sistema.round(indic.getPorcentajeMontoPedidoLogrado())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[6],String.valueOf(Sistema.round(indic.getIngresosestimados())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[7],String.valueOf(Sistema.round(indic.getIngresosreal())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[8],String.valueOf(Sistema.round(indic.getPorcentajPrimerPedidoLogrado())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[9],String.valueOf(indic.getMontoVentaNeta()), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[10],String.valueOf(Sistema.round(indic.getCantidadConsultorasActivas())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[11],String.valueOf(Sistema.round(indic.getPorcentajeActividad())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[12],String.valueOf(Sistema.round(indic.getCantidadConsultorasReingreso())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[13],String.valueOf(Sistema.round(indic.getCantidadConsultorasEgreso())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[14],String.valueOf(indic.getCapitalizacion()), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[15],String.valueOf(Sistema.round(indic.getPorcentajeRotacion())), ubica, false, 0));
									item.getItems().addElement(new Item(false, sCategorias[16],String.valueOf(Sistema.round(indic.getPorcentajeRetencion())), ubica, false, 0));
									jerarquia.addElement(item);	
								}
							}
				        }*/
//				 }
		}
		llenaLista();
	}
	
	 private void menus() {
			removeAllMenuItems();
			 if((!child) ){
				 addMenuItem(opcionMenu);
				
				 
				//si no es GZ y no esta en la lista por categoria
//			 	if(meGlobales.getPerfil() != MetodosGlobales.perfilGZ() ) //&& (!porCampana)
				if( Sistema.getPerfil() != Sistema.perfilGGZZ  
						&& menuTitulo != null 
						&& menuTitulo.equals("Ver detalle")) 
			 		addMenuItem(_verDetalle);
			 }
			 //si no es GZ y esta por detalle
//			if(meGlobales.getPerfil()!=meGlobales.perfilGZ() && (child))
//					 addMenuItem(bajarPerfil);
			 if (Sistema.getPerfil() == Sistema.perfilDV  && (child))
//			 if (meGlobales.getPerfil() == MetodosGlobales.perfilDV() && (child))
				 if(contDetalleClick < 3){ //si es Dv solo tiene 2 detalles GR y DV
					 contDetalleClick++;
					 addMenuItem(_verDetalle);
				 }
		}			

	 MenuItem opcionMenu = new MenuItem(menuTitulo, 110, 10) {
			public void run() {
				if( opcionMenuItem == true ) {
			    	int index = lstIndicadores.getSelectedIndex();
			    	Item item = (Item) get(lstIndicadores, index);
			    	if ( ! item.isCabecera() ) {
				    	item = (Item) jerarquia.elementAt(item.getIndexPadre());
			    	}
			    	campana = item.getDescripcion();
				}
				llenaJerarquia(! opcionMenuItem);
			}
		}; 
		
	MenuItem _verDetalle = new MenuItem("Por Sección", 110, 10) {
		public void run() {
			String ubica = obtenerUbica(lstIndicadores.getSelectedIndex());
			
			if(opcionMenuItem){
				//ChildEstadoIndicador = esIndicadorDB.getRemoteChild(ubica);
//				if (ChildEstadoIndicador != null)
//					child = true;
				llenaJerarquia(true);
			}
			else{
				ChildIndicador = indicadores.getRemoteChild(ubica);
				if (ChildIndicador != null)
					child = true;
				llenaJerarquia(true);
			}
		}
	};
	
	private double importe(String cat, Indicador indic) {
		if(cat.equals(sCategorias[0]))
			return indic.getCantidadPedidosVenta();
		if(cat.equals(sCategorias[1]))
			return indic.getCantidadPediosFacturado();
		if(cat.equals(sCategorias[2]))
			return indic.getPorcentajeCantidadPedidoLogrado();
		if(cat.equals(sCategorias[3]))
			return indic.getMontoPedidoVenta();
		if(cat.equals(sCategorias[4]))
			return indic.getMontoPedidoFacturado();
		if(cat.equals(sCategorias[5]))
			return indic.getPorcentajeMontoPedidoLogrado();
		if(cat.equals(sCategorias[6]))
			return indic.getIngresosestimados();
		if(cat.equals(sCategorias[7]))
			return indic.getIngresosreal();
		if(cat.equals(sCategorias[8]))
			return indic.getPorcentajPrimerPedidoLogrado();
		if(cat.equals(sCategorias[9]))
			return indic.getMontoVentaNeta();
		if(cat.equals(sCategorias[10]))
			return indic.getCantidadConsultorasActivas();
		if(cat.equals(sCategorias[11]))
			return indic.getPorcentajeActividad();
		if(cat.equals(sCategorias[12]))
			return indic.getCantidadConsultorasReingreso();
		if(cat.equals(sCategorias[13]))
			return indic.getCantidadConsultorasEgreso();
		if(cat.equals(sCategorias[14]))
			return indic.getCapitalizacion();
		if(cat.equals(sCategorias[15]))
			return indic.getPorcentajeRotacion();
		if(cat.equals(sCategorias[16]))
			return indic.getPorcentajeRetencion();
		return 0;
	}
	
	private String obtenerUbica(int index) {
		int n = jerarquia.size();
		int x = 0, count = 0, head = 0;
		Item item, itemChild;
		Vector child;
		for (; count < n; count++, x++) {
			item = (Item) jerarquia.elementAt(count);
			child = item.getItems();
			int m = child.size();
			head++;
			for (int i = 0; i < m; i++, x++) {
				if (x == index - 1) {
					itemChild = (Item) child.elementAt(i);
					return itemChild.getUbicacion();
				}
			}
		}
		return null;
	}

	private void llenaLista() {
		int n, m;
		n = jerarquia.size();
		lista = new Vector();
		for (int i = 0; i < n; i++) {
			Item padre = (Item) jerarquia.elementAt(i);
			lista.addElement(padre);
			if (padre.isColapsado() == false) {
				m = padre.getItems().size();
				for (int j = 0; j < m; j++) {
					Item hijo = (Item) padre.getItems().elementAt(j);
					lista.addElement(hijo);
				}
			}
		}
		lstIndicadores.setSize(lista.size());
		lstIndicadores.invalidate();
	}

//	private String monto(Indicador ind, int index) {
//		if (index == 0)
//			return String.valueOf(ind.getPedidosmeta());
//		if (index == 1)
//			return String.valueOf(ind.getPedidosfacturacion());
//		if (index == 2)
//			return String.valueOf(ind.getPedidoslogro());
//		if (index == 3)
//			return String.valueOf(ind.getMontomenta());
//		if (index == 4)
//			return String.valueOf(ind.getMontofacturacion());
//		if (index == 5)
//			return String.valueOf(ind.getMontologro());
//		if (index == 6)
//			return String.valueOf(ind.getPrimerospedidosmeta());
//		if (index == 7)
//			return String.valueOf(ind.getPrimerospedidosfacturacion());
//		if (index == 8)
//			return String.valueOf(ind.getPrimerospedidoslogro());
//		return null;
//	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if (field == lstIndicadores) {
			lstIndicadores.invalidate(lstIndicadores.getSelectedIndex() + dy);
			lstIndicadores.invalidate(lstIndicadores.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}

	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if (field == lstIndicadores) {
			int index = lstIndicadores.getSelectedIndex();
			Item item = (Item) get(lstIndicadores, index);
			if (item.isCabecera()) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				lstIndicadores.setSelectedIndex(index);
			}
			return true;
		}
		return super.navigationClick(status, time);
	}

//	MenuItem _Estado = new MenuItem("Por estados", 110, 10) {
//		public void run() {
//			opcionMenuItem = true;
//			llenaJerarquia(true);
//		}
//	};
//
//	MenuItem _Secciones = new MenuItem("Por seccion", 110, 10) {
//		public void run() {
//			opcionMenuItem = false;
//			llenaJerarquia(true);
//		}
//	};

	public void drawListRow(ListField listField, Graphics g, int index, int y,
			int w) {
		String sColapsado = "";
		Item item = (Item) get(listField, index);
		if (listField.getSelectedIndex() == index) {
			g.setColor(Color.WHITE);
			g.setBackgroundColor(Estilos.getBGSelected());
		} else {
			if (item.isCabecera()) {
	            g.setColor(Estilos.getColorInterlinea(0));        
	            g.setBackgroundColor(Estilos.getBGInterlinea(0));
			} else {
	            g.setColor(Estilos.getColorInterlinea(1));        
	            g.setBackgroundColor(Estilos.getBGInterlinea(1));
			}
			g.clear();
		}
		if (item.isCabecera()) {
			if (item.isColapsado()) {
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

	public int getPreferredWidth(ListField arg0) {
		return 0;
	}

	public int indexOfList(ListField arg0, String arg1, int arg2) {
		return 0;
	}

}