package com.belcorp.ui;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.belcorp.dao.PaisDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.belcorp.utilidades.Sincronia;
import com.makipuray.ui.mkpyLabelField;

public class MenuOpciones extends MainScreen implements ListFieldCallback {        
    private ListField menu;
    boolean cont = false;
    private String[] opciones ;
    //private Sincronia sincUsuario;
    private static Timer timer = null; 
    
//    private MetodosGlobales meGlobales = new MetodosGlobales();
    private Usuario usuario;

    public boolean onClose() {
    	if(Dialog.ask(Dialog.D_YES_NO, "Desea salir del aplicativo?") != Dialog.NO)
    		System.exit(0);
    	return true;
    };
    
    private void seleccion() {
    	if( opciones[menu.getSelectedIndex()].equals(MetodosGlobales.opciones[1]) ) { // Atracciones
    		Estilos.pushScreen(new Atracciones());
    	} else if( opciones[menu.getSelectedIndex()].equals(MetodosGlobales.opciones[2]) ) { // Nuevas
        	Estilos.pushScreen(new MenuContactos());
    	} else if( opciones[menu.getSelectedIndex()].equals(MetodosGlobales.opciones[3]) ) { // Buscar
        	Estilos.pushScreen(new Establecidas(MetodosGlobales.establecidasTitulo()));
    	} else if( opciones[menu.getSelectedIndex()].equals(MetodosGlobales.opciones[5]) ) { // Facturacion
        	Estilos.pushScreen(new FacturaResumen());
    	} else if( opciones[menu.getSelectedIndex()].equals(MetodosGlobales.opciones[6]) ) { // Indicadores
        	Estilos.pushScreen(new IndicadorDetalle());
    	}
    }
       
    public MenuOpciones() {
        VerticalFieldManager vField = new VerticalFieldManager();
        UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
//		meGlobales.setPerfil(perfil);
        
        HorizontalFieldManager hField = new HorizontalFieldManager() {
    		protected void sublayout(int width, int height) {
    			super.sublayout(super.getScreen().getWidth(), height);
    			super.setExtent(super.getScreen().getWidth(), getField(1).getHeight());
    			Field field;

    			field = getField(0);
                layoutChild(field, width / 2, height);
    			//setPositionChild(field, (width / 4) - ( field.getPreferredWidth() / 2), 0);
    			setPositionChild(field, 5, 0);

    			field = getField(1);
                layoutChild(field, width, height);
    			setPositionChild(field, (width / 2) + (width / 4) - ( field.getPreferredWidth() / 2), 0);

        	}        	
        };
        
		vField.add(new LabelField("", LabelField.FIELD_LEFT));
		vField.add(new mkpyLabelField(usuario.getNombre(), LabelField.ELLIPSIS | LabelField.FIELD_LEFT, Color.BLACK, Color.WHITE, true));
		vField.add(new LabelField(usuario.getZonaRegionPais(), LabelField.FIELD_LEFT));
		vField.add(new LabelField(usuario.getCampanaFormato(), LabelField.FIELD_LEFT));
		
		hField.add(vField);
		hField.add(new BitmapField(Bitmap.getBitmapResource("img/logo.png"), BitmapField.FIELD_LEFT | BitmapField.FIELD_VCENTER));

	   	opciones = MetodosGlobales.menuOpcionesxPerfil();
        menu = new ListField(opciones.length, ListField.FIELD_HCENTER);
        menu.setCallback(this);

        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/menuprincipal.png"), BitmapField.FIELD_HCENTER));
        add(hField);
        add(menu);
        addMenuItem(mnSincronizar);
        addMenuItem(mnAcerca);
        addMenuItem(mnCerrarSesion);

        Sistema.addEstadistica(Cadenas.MENUPRINCIPAL);
        
        if(usuario != null ){
	        //MBL : Timer de sincronización
        	long tiempo = usuario.getTiempoDatosConsultora()*60*60000; //mbl: En horas
            timer = new Timer();  
            timer.schedule(new Sicronizacion(), tiempo, tiempo);   
        }
        
    }

    MenuItem mnSincronizar = new MenuItem ("Sincronizar", 110, 10) {
        public void run() {
        	try{
        		synchronized (UiApplication.getEventLock()) 
                {  
                    Sincronia sincUsuario = new Sincronia();
        			sincUsuario.Exec();
        			sincUsuario = null;
                }
        	}catch (Exception ex){
        		
        	}
        }
    };
    
    /*MBL 15/11/2011 : Opción para liberar al usuario grabado*/
    MenuItem mnCerrarSesion = new MenuItem ("Cerrar Sesión", 110, 10) {
        public void run() {
        	UsuarioDB usuarios = new UsuarioDB();
        	usuarios.Clear();
        	System.exit(0);
        }
    };
    
    MenuItem mnAcerca = new MenuItem ("Acerca de ...", 110, 10) {
        public void run() {
        	Dialog.inform("Belcorp One Ver. " + Sistema.getVersion());
        }
    };

    protected boolean navigationMovement(int dx, int dy, int status, int time) {
        Field field = this.getFieldWithFocus();
        if(field == menu) {
                menu.invalidate(menu.getSelectedIndex() + dy);
                menu.invalidate(menu.getSelectedIndex());
        }
        return super.navigationMovement(dx, dy, status, time);
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == menu) {
			seleccion();
			return true;
		}
		return super.navigationClick(status, time);
	}

    public void drawListRow(ListField list, Graphics g, int index, int y, int w) {
        if ( list.getSelectedIndex() == index ) {
        } else {
            g.setColor(Color.WHITE);
            if(get(index).equals("Consultoras") || get(index).equals("Pedidos")){
                g.setBackgroundColor(Estilos.getBGModulo());
            } else  {
                g.setBackgroundColor(Estilos.getBGSubModulo());
            }
            g.clear();
        }
        g.drawText(get(index), 0, y, 0, w);
    }

    public String get(int index) {
        return opciones[index];
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
    
    public class Sicronizacion extends TimerTask
    {         
    	public Sicronizacion() { 
    		
    	}  
  
        public void run() 
        { 
        	try{
        		synchronized (UiApplication.getEventLock()) 
                {  
        			Sincronia sincUsuario = new Sincronia();
        			Finalizador finejecucion =  new Finalizador(sincUsuario,120000); 
        			finejecucion.start();
        			sincUsuario.Exec();
        			sincUsuario = null;
                }
        	}catch (Exception ex){

        	}
        }
   }
    
    protected class Finalizador extends Thread{
    	Finalizador(Thread tarea,int tiempomaximo){
    		try {
				sleep(tiempomaximo);
				tarea.interrupt();
			} catch (InterruptedException e) {
				
			}
    		
    	}
    	
    }
    
}
