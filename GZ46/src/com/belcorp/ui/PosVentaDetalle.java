package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.dao.IndicadorDB;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Indicador;
import com.belcorp.entidades.Item;
import com.belcorp.entidades.PosVenta;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;

/***
 *  listado solo por detalle 
 * @author Franco
 *
 */
public class PosVentaDetalle extends  MainScreen implements ListFieldCallback {
	private ListField lstPosVentas;
    static String[] sCategorias = MetodosGlobales.lsCatIndicadores();
    private Vector jerarquia;
	private Vector lista;  
    private Indicador facChild;
	private IndicadorDB indicadores = new IndicadorDB();
	private Consultora consultora;
	private final static int NROLINEAS = 5;

	/**
	 * Constructor de la pantalla, dependiendo de los parámetros del origen y el objeto de la consultora
	 * @param origen, si proviene de 0 = nuevas, 1 = buscar, 2 = generar lista
	 * @param consultora, objeto
	 */
     public PosVentaDetalle(int origen, Consultora consultora) {
    	 this.consultora = consultora;
 		lstPosVentas = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
 		lstPosVentas.setRowHeight( (this.getFont().getHeight() * NROLINEAS));
 	    lstPosVentas.setCallback(this);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/detallepostventa.png"), BitmapField.FIELD_HCENTER));
        lstPosVentas.setSize(consultora.getPostVentas().size());
 		add(lstPosVentas);

    	int pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        //Sistema.addEstadistica(Cadenas.V1_);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_POSTVENTA);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_POSTVENTA);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_POSTVENTA);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else if( origen == 1 ) {
	        Sistema.addEstadistica(Cadenas.BC_POSTVENTA);
        } else if ( origen == 2 ) {
	        Sistema.addEstadistica(Cadenas.GL_POSTVENTA);
        } 
 		
 		//llenaJerarquia(true);
     }
     
//	private void llenaJerarquia(boolean opcion) {
//    	 Item item;
// 		jerarquia = new Vector();
// 		int c ;
// 		c = consultora.getPostVentas().size();
//   		for( int j = 0; j < c; j++){
//   			PosVenta posVenta = (PosVenta) consultora.getPostVentas().elementAt(j);
//   			item = new Item(true, posVenta.getCodigo(), Cadenas.getMoneda(), false, j);
//   			item.getItems().addElement(new Item(false, posVenta.getOperacion(), "", false, j));
//   			item.getItems().addElement(new Item(false, "Código de Venta", "" + posVenta.getUnidad(), false, j));
//   			item.getItems().addElement(new Item(false, posVenta.getResultado(), "" + posVenta.getMotivo(), false, j));
//   			item.getItems().addElement(new Item(false, posVenta.getCampanaOrigen() + " - " + posVenta.getCampanaProceso(), "", false, j));
//
//   			jerarquia.addElement(item);
//   		}
//    	llenaLista();
//   }
// 	
// 	private void llenaLista() {
//		int n, m;
//		n = jerarquia.size();
//		lista = new Vector();
//		for(int i = 0; i < n; i++) {
//			Item padre = (Item) jerarquia.elementAt(i);
//			lista.addElement(padre);
//			if ( padre.isColapsado() == false ) {
//				m = padre.getItems().size();
//				for (int j = 0; j < m; j++) {
//					Item hijo = (Item) padre.getItems().elementAt(j);
//					lista.addElement(hijo);
//				}
//			}
//		}
//		lstPosVentas.setSize(lista.size());
//		lstPosVentas.invalidate();
//	}
 	 	
 	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstPosVentas) {
			lstPosVentas.invalidate(lstPosVentas.getSelectedIndex() + dy);
			lstPosVentas.invalidate(lstPosVentas.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
		
//	protected boolean navigationClick(int status, int time) {
//		Field field = this.getFieldWithFocus();
//		if(field == lstPosVentas) {
//			int index = lstPosVentas.getSelectedIndex();
//			Item item = (Item) get(lstPosVentas, index);
//			if ( item.isCabecera() ) {
//				item.setColapsado(!item.isColapsado());
//				llenaLista();
//				lstPosVentas.setSelectedIndex(index);
//				return true;
//			}
//		}
//		return super.navigationClick(status, time);
//	}
	
			
    public void drawListRow(ListField listField, Graphics g, int index, int y, int w) {
    	String sColapsado = "";
    	PosVenta item = (PosVenta) get(listField, index);
		if ( listField.getSelectedIndex() == index ) {
            g.setColor(Color.WHITE);        
            g.setBackgroundColor(Estilos.getBGSelected());
		} else {
			//if ( item.isCabecera() ) {
	            //g.setColor(Color.WHITE);        
	            //g.setBackgroundColor(Estilos.getBGModulo());
	            //g.setColor(Estilos.getColorInterlinea(0));        
	            //g.setBackgroundColor(Estilos.getBGInterlinea(0));
			//} else {
	            //g.setColor(Estilos.getColorInterlinea(index - 1));        
	            //g.setBackgroundColor(Estilos.getBGInterlinea(index - 1));
	            //g.setColor(Estilos.getColorInterlinea(1));        
	            //g.setBackgroundColor(Estilos.getBGInterlinea(1));
	            //g.clear(0 + 1, y + 1, w - 2, this.getFont().getHeight() - 2);
			//}
            g.setColor(Estilos.getColorInterlinea(index - 1));        
            g.setBackgroundColor(Estilos.getBGInterlinea(index - 1));
	        g.clear();
		}
		//if ( item.isCabecera() ) {
			//if ( item.isColapsado() ) {
				//sColapsado = Characters.BLACK_RIGHT_POINTING_TRIANGLE + " ";
			//} else {
				//sColapsado = Characters.BLACK_DOWN_POINTING_TRIANGLE + " ";
			//}
		//} else {
			//sColapsado = Characters.SPACE + "";
		//}
		g.drawText(item.getCodigo(), 0, y, DrawStyle.LEFT, w);
		g.drawText(item.getCampanaOrigen() + " - " + item.getCampanaProceso(), 0, y + ( this.getFont().getHeight() * 1), DrawStyle.LEFT, w);
		g.drawText(item.getProducto(), 0, y + ( this.getFont().getHeight() * 2), DrawStyle.LEFT, w);
        g.drawText(item.getUnidad(), 0, y + ( this.getFont().getHeight() * 2), DrawStyle.RIGHT, w);
        g.drawText(item.getResultado(), 0, y + ( this.getFont().getHeight() * 3), DrawStyle.LEFT, w);
        g.drawText(item.getMotivo(), 0, y + ( this.getFont().getHeight() * 3), DrawStyle.RIGHT, w);
        g.drawText(item.getOperacion(), 0, y + ( this.getFont().getHeight() * 4), DrawStyle.LEFT, w);
   }

	public Object get(ListField listField, int index) {
		return consultora.getPostVentas().elementAt(index);
		//return (Item) lista.elementAt(index);
	}

    public int getPreferredWidth(ListField arg0) {
         return 0;
    }

    public int indexOfList(ListField arg0, String arg1, int arg2) {
        return 0;
    }

    
}
