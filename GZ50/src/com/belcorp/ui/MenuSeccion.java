package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.entidades.Seccion;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.belcorp.dao.SeccionDB;
import com.belcorp.dao.ConsultoraDB;
import net.rim.device.api.ui.DrawStyle;

public class MenuSeccion extends MainScreen implements ListFieldCallback {
    private final static int NROLINEASMAX = 13;
    private ListField lstSecciones;
    private SeccionDB secciones = new SeccionDB();
    private ConsultoraDB consultoras = new ConsultoraDB();
    private String idClasificacion;
    private int sizePixel;
    private MainScreen menuContactos;
    
    private String cantidadTotal;

    private void seleccion() {
    	Seccion seccion = null;
		consultoras.setClasificacion(idClasificacion);
		if ( lstSecciones.getSelectedIndex() == 0 ) {
			consultoras.setIdSeccion(null);
		} else {
			seccion = get(lstSecciones.getSelectedIndex());
			consultoras.setIdSeccion(seccion.getId());
		}
		Vector filtradotmp = consultoras.getConsultoras();
		if ( filtradotmp.size() == 0 ) {
			Dialog.inform("No hay consultoras");
		} else {
	    	if(lstSecciones.getSelectedIndex() == 0){
	            Estilos.pushScreen(new ConsultaSeccion(idClasificacion, null, menuContactos, this));
	    	}else{
	            Estilos.pushScreen(new ConsultaSeccion(idClasificacion, seccion.getId(), menuContactos, this));
	    	}
		}
    }
           
    public MenuSeccion(String idClasificacion, String cantidades, MainScreen menuContactos){
		this.idClasificacion = idClasificacion;
		this.menuContactos = menuContactos;
		cantidadTotal = cantidades;
		consultoras = new ConsultoraDB();
		secciones = new SeccionDB();
		cuerpo();
    }
    
    private void cuerpo() {
		lstSecciones = new ListField(secciones.getObjetos().size() + 1, ListField.FIELD_HCENTER);
		lstSecciones.setCallback(this);
		/*
		 * SE comenta a solicitud de Sybil Dávila el día 19 de agosto 2011
		 * eliminando así la funcionalidad de que la lista se ajusta a la pantalla dependiendo de la cantidad de secciones de la GZ
		 * 
		sizePixel = ( this.getFont().getHeight() * NROLINEASMAX ) / secciones.getObjetos().size();
		lstSecciones.setRowHeight( sizePixel );
		 */
        add(new BitmapField(Bitmap.getBitmapResource(Cadenas.getClasificacionMetodologica(idClasificacion)), BitmapField.FIELD_HCENTER));
		add(lstSecciones);

		switch( Integer.parseInt(idClasificacion) ) {
			case 1:
		        Sistema.addEstadistica(Cadenas.V1_LISTASECCION);
				break;
			case 2:
		        Sistema.addEstadistica(Cadenas.V2_LISTASECCION);
				break;
			case 3:
		        Sistema.addEstadistica(Cadenas.V3_LISTASECCION);
				break;
			case 4:
		        Sistema.addEstadistica(Cadenas.V4_LISTASECCION);
				break;
			case 5:
		        //Sistema.addEstadistica(Cadenas.V5_LISTASECCION);
				break;
		}
    
    }

 	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstSecciones) {
			lstSecciones.invalidate(lstSecciones.getSelectedIndex() + dy);
			lstSecciones.invalidate(lstSecciones.getSelectedIndex());
		}
		return super.navigationMovement(dx, dy, status, time);
	}
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == lstSecciones) {
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}

   public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
	   String descripcion = "";
	   String cantidades = "";
	   if(index > 0){
		   Seccion seccion = get(index);
	        consultoras.setClasificacion(idClasificacion);
	        consultoras.setIdSeccion(seccion.getId());
	        Vector filtrado = consultoras.getConsultoras();
	        descripcion = seccion.getDescripcion();
	        cantidades = consultoras.getCantidadPorVisitar(filtrado).size() + " / " + filtrado.size();
	   }
	   if ( list.getSelectedIndex() == index ) {
            g.setColor(Color.WHITE);        
            g.setBackgroundColor(Estilos.getBGSelected());
		} else {
		    g.setColor(Estilos.getColorInterlinea(index));        
		    g.setBackgroundColor(Estilos.getBGInterlinea(index));
		    g.clear();
		}
        
	   /*
		 * SE comenta a solicitud de Sybil Dávila el día 19 de agosto 2011
		 * eliminando así la funcionalidad de que la lista se ajusta a la pantalla dependiendo de la cantidad de secciones de la GZ
	    */
        //int pxlLinea = ( sizePixel / 2 ) - ( this.getFont().getHeight() / 2 );
	   int pxlLinea = 0;
   
        if(index > 0){
            g.drawText(descripcion, 0, y + pxlLinea, 0, w);
            g.drawText(cantidades, 0, y + pxlLinea, DrawStyle.RIGHT, w);
        } else {
        	String descripcionT = "Toda la Zona";
            g.drawText(descripcionT, 0, y + pxlLinea, 0, w);
            g.drawText(cantidadTotal, 0, y + pxlLinea, DrawStyle.RIGHT, w);
        }

    }

    public Seccion get(int index) {
        return (Seccion) secciones.getObjetos().elementAt(index - 1);
    }

    public int getPreferredWidth(ListField arg0) {
        return 0;
    }

    public int indexOfList(ListField arg0, String arg1, int arg2) {
        return 0;
    }

    public Object get(ListField listField, int index) {
        return null;
    }

}
