
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
import net.rim.device.api.util.Arrays;

import com.belcorp.dao.FacturacionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Facturacion;
import com.belcorp.entidades.Item;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;

/***
 *  listado por seccion y categoria
 *  detalle de las listas solo para las secciones
 * @author Franco
 *
 */
public class FacturacionDetalle extends  MainScreen  implements ListFieldCallback  {        
    private static ListField lstFacturaciones;
    static String[] sCategorias = MetodosGlobales.lsCatFacturacion();
 	private boolean porCampana;
	private final static String menuSeccion = "Por Sección"; 
	private final static String menuCategoria = "Por Categoría"; 
	private boolean child = false;
	private Vector jerarquia;
	private Vector lista;
	private FacturacionDB facturaciones = new FacturacionDB();
	private Facturacion facChild;    
	private MetodosGlobales meGlobales = new MetodosGlobales();
	private String campana;
	private mkpyStatusProgress progress = new mkpyStatusProgress("");
    
	public FacturacionDetalle(String camp) {
		campana = camp;
		lstFacturaciones = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
		lstFacturaciones.setCallback(this);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/facturacionresumen.png"), BitmapField.FIELD_HCENTER));
	    add(lstFacturaciones);
	    llenaJerarquia(true);
	}

	private void llenaJerarquia(boolean opcion) {
	    Item item;
	    menus();
		porCampana = opcion;
    	jerarquia = new Vector();

    	Facturacion facturacion;
    	Vector vFacturacion = null;
    	if(child){ // si el valor es true, es una lista x detalle
    		vFacturacion = facChild.getFacturacion();
    	    menus();// se cambian los items del menu
    	} else {
    		vFacturacion = facturaciones.getObjetosByCampana(campana);
    	}
    	String[] sCategorias = MetodosGlobales.lsCatFacturacion();
    	int i, n, m, c;
    	
		n = vFacturacion.size(); //tamaño del vector de objetos
    	n = sCategorias.length;
    
		if ( opcion ){ // x seccion
	        Sistema.addEstadistica(Cadenas.FAC_SECCION);
	    	opcionMenu.setText(menuCategoria);
			for ( i = 0; i < vFacturacion.size(); i++ ) {// recorremos todas las facturas
				facturacion = (Facturacion) vFacturacion.elementAt(i);
				item = new Item(true, campana.concat(" - ").concat(facturacion.getUbicacion()), "", false, i);//cabecera seccon con tipo de moneda
				item.getItems().addElement(new Item(false, sCategorias[0], String.valueOf(Sistema.round(facturacion.getPedidos())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[1], String.valueOf(Sistema.round(facturacion.getPeg())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[2], String.valueOf(Sistema.round(facturacion.getPrimerpedido())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[3], String.valueOf(Sistema.round(facturacion.getEnviado())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[4], String.valueOf(Sistema.round(facturacion.getObservado())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[5], String.valueOf(Sistema.round(facturacion.getRechazado())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[6], String.valueOf(Sistema.round(facturacion.getFacturado())), false, i));
				item.getItems().addElement(new Item(false, sCategorias[7], Cadenas.getMoneda()+String.valueOf(facturacion.getMonto()), false, i));
				item.getItems().addElement(new Item(false, sCategorias[8], String.valueOf(Sistema.round(facturacion.getActivassinpedido())), false, i));
				jerarquia.addElement(item);	
			}
		}else { // x categoria
	        Sistema.addEstadistica(Cadenas.FAC_CATEGORIA);
			int e, k;
			opcionMenu.setText(menuSeccion);
			m = sCategorias.length;
			e = vFacturacion.size();
			String cat;
			for (c = 0; c < m; c++ ) { //recorre las categorias
			   cat = sCategorias[c];
			   item = new Item(true, cat, "", false, c); //totales
			   double subtotal = 0;
			   for(k = 0; k < e; k++){ //recorre las facturas
				   facturacion = (Facturacion) vFacturacion.elementAt(k);
				   double importe = importe(cat, facturacion);
	               item.getItems().addElement(new Item(false, facturacion.getUbicacion(), String.valueOf(Sistema.round(importe)), false, c));  //unidad
				   subtotal += importe;
			   }
			   if(c == 7)
				   item.setValor(Cadenas.getMoneda().concat("" + Sistema.round(subtotal)));
			   else
				   item.setValor("" + Sistema.round(subtotal));
			   jerarquia.addElement(item);	
			}
		}
		llenaLista();	
	}

	MenuItem opcionResumen = new MenuItem("Por Campaña", 110, 10) {
		public void run() {
			close();
			//llenaJerarquia(! porCampana);
		}
	}; 
	
	MenuItem opcionMenu = new MenuItem(menuCategoria, 110, 10) {
		public void run() {
			llenaJerarquia(! porCampana);
		}
	}; 
			
//	MenuItem _verDetalle = new MenuItem("Detalles", 110, 10) {
//		public void run() {
//			String ubica = obtenerUbica(lstFacturaciones.getSelectedIndex());
//			facChild = facturaciones.getRemoteChild(ubica);
//			if(facChild != null)
//				child = true;
//			llenaJerarquia(porCampana);
//		}
//	}; 
//			
//	private String obtenerUbica(int index) {
//		int n = jerarquia.size();
//		int x = 0, count = 0, head = 0;
//		Item item, itemChild;
//		Vector child;
//        for (; count < n; count++,x++) {
//            item = (Item) jerarquia.elementAt(count);
//            child = item.getItems();
//            int m = child.size();
//            head ++;
//            for(int i= 0; i < m; i++, x++){
//            	if(x == index - 1){
//            		itemChild = (Item) child.elementAt(i);
//            		return itemChild.getUbicacion();
//            	}
//            }
//        }
//        return null;
//	}
		
	private void llenaLista() {
		int n, m;
		n = jerarquia.size();
		lista = new Vector();
		for(int i = 0; i < n; i++) {
			Item padre = (Item) jerarquia.elementAt(i);
			lista.addElement(padre);
			if ( padre.isColapsado() == false ) {
				m = padre.getItems().size();
				for (int j = 0; j < m; j++) {
					Item hijo = (Item) padre.getItems().elementAt(j);
					lista.addElement(hijo);
				}
			}
		}
		lstFacturaciones.setSize(lista.size());
		lstFacturaciones.invalidate();
	}
 
	private double importe(String cat, Facturacion facturacion) {
		if(cat.equals(sCategorias[0]))
			return facturacion.getPedidos();
		if(cat.equals(sCategorias[1]))
			return facturacion.getPeg();
		if(cat.equals(sCategorias[2]))
			return facturacion.getPrimerpedido();
		if(cat.equals(sCategorias[3]))
			return facturacion.getEnviado();
		if(cat.equals(sCategorias[4]))
			return facturacion.getObservado();
		if(cat.equals(sCategorias[5]))
			return facturacion.getRechazado();
		if(cat.equals(sCategorias[6]))
			return facturacion.getFacturado();
		if(cat.equals(sCategorias[7]))
			return facturacion.getMonto();
		if(cat.equals(sCategorias[8]))
			return facturacion.getActivassinpedido();
		return 0;
	}
		  
	 private void menus() {
		 removeAllMenuItems();
		 if((!child) ){
			 addMenuItem(opcionMenu);
			//si no es GZ y no esta en la lista por categoria
//		 	if(meGlobales.getPerfil() != MetodosGlobales.perfilGZ() && (!porCampana))
//		 		addMenuItem(_verDetalle);
		 }
		 //si no es GZ y esta por detalle
//			if(meGlobales.getPerfil()!=meGlobales.perfilGZ() && (child))
//					 addMenuItem(bajarPerfil);
//		 if (meGlobales.getPerfil() == MetodosGlobales.perfilDV() && (child)) {
//			 if(contDetalleClick < 3){ //si es Dv solo tiene 2 detalles GR y DV
//				 contDetalleClick++;
//				 addMenuItem(_verDetalle);
//			 }
//		 }
		 addMenuItem(opcionResumen);
	}			

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstFacturaciones) {
			lstFacturaciones.invalidate(lstFacturaciones.getSelectedIndex() + dy);
			lstFacturaciones.invalidate(lstFacturaciones.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
		
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstFacturaciones) {
			int index = lstFacturaciones.getSelectedIndex();
			Item item = (Item) get(lstFacturaciones, index);
			if ( item.isCabecera() ) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				lstFacturaciones.setSelectedIndex(index);
			} else {
				if ( porCampana ) {
					if ( !item.getValor().equals("0") ) {
						getCategoria(item.getDescripcion(), ((Item) jerarquia.elementAt(item.getIndexPadre())).getDescripcion());
					}
				}
			}
			return true;
		}
		return super.navigationClick(status, time);
	}
	
	private void getCategoria(String categoria, String campana_seccion) {
    	int estado = -1;
    	String camp = campana_seccion.substring(4, 8) + campana_seccion.substring(1, 3);
    	String seccion = campana_seccion.substring(11);
		switch(Arrays.getIndex(sCategorias, categoria)) {
			case 3:
				estado = 0;
				break;
			case 4:
				estado = 1;
				break;
			case 5:
				estado = 2;
				break;
			case 6:
				estado = 3;
				break;
			case 8:
				estado = 4;
				break;
		}
		if ( estado >= 0 ) {
	    	try {
	    		if ( seccion.equals("N/S") ) seccion = "";
	    		progress.setTitle("Buscando... ");
	        	progress.open();
	        	progress.close();
		    	Estilos.pushScreen(new ConsultaSeccion(camp, seccion, "", "", "", "" + estado));
	    	} catch(Exception e) {
	    		
	    	}
		}
	}
	
    public void drawListRow(ListField listField, Graphics g, int index, int y, int w) {
     	String sColapsado = "";
		Item item = (Item) get(listField, index);
		if ( listField.getSelectedIndex() == index ) {
            g.setColor(Color.WHITE);        
            g.setBackgroundColor(Estilos.getBGSelected());
            //g.clear();
		} else {
			if ( item.isCabecera() ) {
	            //g.setColor(Color.WHITE);        
	            //g.setBackgroundColor(Estilos.getBGModulo());
	            g.setColor(Estilos.getColorInterlinea(0));        
	            g.setBackgroundColor(Estilos.getBGInterlinea(0));
			} else {
	            g.setColor(Estilos.getColorInterlinea(1));        
	            g.setBackgroundColor(Estilos.getBGInterlinea(1));
	            //g.setColor(Estilos.getColorInterlinea(index - 1));        
	            //g.setBackgroundColor(Estilos.getBGInterlinea(index - 1));
	            //g.clear(0 + 1, y + 1, w - 2, this.getFont().getHeight() - 2);
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

    public int getPreferredWidth(ListField arg0) {
         return 0;
    }

    public int indexOfList(ListField arg0, String arg1, int arg2) {
        return 0;
    }
 
 }

