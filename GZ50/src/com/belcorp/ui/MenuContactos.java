package com.belcorp.ui;

import java.util.Vector;
import com.belcorp.dao.ConsultoraDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Usuario;

import net.rim.device.api.system.Bitmap;
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

import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelField;

public class MenuContactos extends MainScreen implements ListFieldCallback {
    private final static int NROLINEASMAX = 11;
    private static ListField menu1;
    private final static String[] sData1 = {" Visita de meta", " Visita de acompañamiento 2", " Visita de acompañamiento 3", " Visita felicitaciones"};
    private ConsultoraDB consultoras;
    private int sizePixel;
    private LabelField lblEstado;
    String cantidades = "";
    String estado;
    
    public MenuContactos(){
    	UsuarioDB usuarios = new UsuarioDB();
    	Usuario usuario = usuarios.getUsuario();
        consultoras = new ConsultoraDB();
        estado = usuario.getFechaHoraUltimaSincronizacionConsultoraFormato();
        lblEstado = new mkpyLabelField(Cadenas.getEspacio(estado.length()).concat(estado), LabelField.USE_ALL_WIDTH | LabelField.FIELD_LEFT, Color.WHITE, Estilos.getBGModulo());
		sizePixel = ( this.getFont().getHeight() * NROLINEASMAX ) / sData1.length;
        menu1 = new ListField(sData1.length, ListField.FIELD_HCENTER);
		menu1.setRowHeight( sizePixel );
        menu1.setCallback(this);
                
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/nuevas.png"), BitmapField.FIELD_HCENTER));
        add(menu1);
        
        this.setStatus(lblEstado);
        Sistema.addEstadistica(Cadenas.BUSCARCONSULTORA);

    }
    
    private void seleccion() {
        int index = menu1.getSelectedIndex();
        if ( index >= 0 ) { 
            String idClasificacion;
            idClasificacion = String.valueOf(index + 1);

            consultoras.setClasificacion(String.valueOf(idClasificacion));
            Vector filtrado = consultoras.getConsultoras();
            String cant = consultoras.getCantidadPorVisitar(filtrado).size() + " / " + filtrado.size();
            if ( filtrado.size() > 0 ) {
                Estilos.pushScreen(new MenuSeccion(idClasificacion, cant, this));
            } else {
            	Dialog.inform("No hay consultoras");
            }
        } 
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == menu1) {
            menu1.invalidate(menu1.getSelectedIndex() + dy);
            menu1.invalidate(menu1.getSelectedIndex());
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == menu1) {
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
        if ( list.getSelectedIndex() == index ) {
        } else {
            g.setColor(Color.WHITE);
            g.setBackgroundColor(Estilos.getBGSubModulo());
            g.clear();
        }
        if ( list == menu1 ) {
            consultoras.setClasificacion(String.valueOf(index + 1));
            Vector filtrado = consultoras.getConsultoras();
            cantidades = consultoras.getCantidadPorVisitar(filtrado).size() + " / " + filtrado.size();
        }
        String label = (String) get(list, index);
        int pxlLinea = ( sizePixel / 2 ) - ( this.getFont().getHeight() / 2 );        
        g.drawText(label, 0, y + pxlLinea, 0, w);
        g.drawText(cantidades, 0, y + pxlLinea, DrawStyle.RIGHT, w);
    }
    
    public String get(int index) {
        return null;
    }

    public int getPreferredWidth(ListField arg0) {
        return 0;
    }

    public int indexOfList(ListField arg0, String arg1, int arg2) {
        return 0;
    }

    public Object get(ListField listField, int index) {
    	String result = "";
    	if ( listField == menu1 ) {
    		result = sData1[index];
    	} 
    	return result;
    }
}
