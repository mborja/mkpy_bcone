package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;
import com.belcorp.dao.FacturacionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Item;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyStatusProgress;


/***
 *  listado solo por el detalle de facturacion
 * @author Franco
 *
 */

public class FacturaResumen extends  MainScreen  implements ListFieldCallback  {        
    private static ListField _listaConsulResumen;
	private Vector jerarquia;
	private Vector lista;
	private mkpyStatusProgress progress = new mkpyStatusProgress("");

	public FacturaResumen() {
		_listaConsulResumen = new ListField(0, ListField.FIELD_HCENTER | ListField.USE_ALL_WIDTH | ListField.USE_ALL_HEIGHT);
		_listaConsulResumen.setCallback(this);
		_listaConsulResumen.setSearchable(true);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/facturacionresumen.png"), BitmapField.FIELD_HCENTER));
	    add(_listaConsulResumen);
	    addMenuItem(_Secciones);
	    llenaJerarquia();
        Sistema.addEstadistica(Cadenas.FAC_CAMPANA);

	}

    private void llenaJerarquia() {
		Item item;
    	FacturacionDB facturaciones = new FacturacionDB();
		jerarquia = new Vector();
		int i, n, m, c;
    	String[] campanas = facturaciones.mostrarCampanas(facturaciones.getObjetos());
    	
		m = campanas.length;
    	double totales[][] = facturaciones.arrayTotalesxFilaAgrup(null);//se llena con objetos 
    	String[] sCategorias = MetodosGlobales.lsCatFacturacion();
    	n = sCategorias.length;
    	for (c = 0; c < m; c++ ) {
    		item = new Item(true, campanas[c], "", false, c);
        	for( i = 0; i < n; i++){
        		if(i == 7)
        			item.getItems().addElement(new Item(false, sCategorias[i], Cadenas.getMoneda().concat(String.valueOf(Sistema.round(totales[i][c]))), false, c));
        		else
        			item.getItems().addElement(new Item(false, sCategorias[i], String.valueOf(Sistema.round(totales[i][c])), false, c));
        	}
        	jerarquia.addElement(item);
    	}
    	llenaLista();
	}
      
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
		_listaConsulResumen.setSize(lista.size());
		_listaConsulResumen.invalidate();
	}
	  
    protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == _listaConsulResumen) {
			_listaConsulResumen.invalidate(_listaConsulResumen.getSelectedIndex() + dy);
			_listaConsulResumen.invalidate(_listaConsulResumen.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
		
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == _listaConsulResumen) {
			int index = _listaConsulResumen.getSelectedIndex();
			Item item = (Item) get(_listaConsulResumen, index);
			if ( item.isCabecera() ) {
				item.setColapsado(!item.isColapsado());
				llenaLista();
				_listaConsulResumen.setSelectedIndex(index);
				return true;
			} else {
				item = (Item) jerarquia.elementAt(item.getIndexPadre());
				int op = getCategoria(index);
				if ( op >= 0 ) {
					int cant = Integer.parseInt( ((Item) get(_listaConsulResumen, index)).getValor() );
					if ( cant > 0 ) {
				    	String campana = item.getDescripcion().substring(4) + item.getDescripcion().substring(1, 3);
						progress.setTitle("Buscando... ");
				    	progress.open();
				    	progress.close();
				    	try {
					    	Estilos.pushScreen(new ConsultaSeccion(campana, "", "", "", "", "" + op));
				    	} catch(Exception e) {
				    		
				    	}
					}
				}
				return true;
			}
		}
		return super.navigationClick(status, time);
	}
	
	private int getCategoria(int index) {
		int i, j, n = 0, m = 0, avance = 0;
		j = _listaConsulResumen.getSize();
		for ( i = 0; i < j; i++) {
			Item item = (Item) get(_listaConsulResumen, i);
			if ( item.isCabecera() ) {
				if ( item.isColapsado() ) m++;
				else n++;
				avance = ((9 * n) + (1 * m));
				if ( avance > index ) {
					break;
				}
			}
		}
		int elemento = avance - (9 - index);
		int estado = -1;
		switch(elemento) {
		case 4:
			estado = 0;
			break;
		case 5:
			estado = 1;
			break;
		case 6:
			estado = 2;
			break;
		case 7:
			estado = 3;
			break;
		case 9:
			estado = 4;
			break;
		}
		return estado;
	}
    
	private MenuItem _Secciones = new MenuItem("Por Sección", 110, 10) {
	    public void run() {
	    	int index = _listaConsulResumen.getSelectedIndex();
	    	Item item = (Item) get(_listaConsulResumen, index);
	    	if ( ! item.isCabecera() ) {
		    	item = (Item) jerarquia.elementAt(item.getIndexPadre());
	    	}
			UiApplication.getUiApplication().pushScreen(new FacturacionDetalle(item.getDescripcion()));
	    }
	};

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

    public int getPreferredWidth(ListField arg0) {
         return 0;
    }

    public int indexOfList(ListField arg0, String arg1, int arg2) {
        return 0;
    }
}
