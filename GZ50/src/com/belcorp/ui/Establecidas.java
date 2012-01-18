	package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.MainScreen;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;

import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.dao.SeccionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.Seccion;
import com.belcorp.entidades.Usuario;
import com.makipuray.ui.mkpyLabelChoiceField;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyStatusProgress;

public class Establecidas extends MainScreen implements FieldChangeListener {
	private mkpyLabelEditField editNombre, editDocIdent, editCodigoConsultora;
	private mkpyLabelChoiceField chSeccion;
	private static String sSeccion[] = null; 
	private static SeccionDB secciones;
	private ButtonField btnBuscar;
	private mkpyStatusProgress progress = new mkpyStatusProgress("");
	private Configuracion conf;
 	UsuarioDB usuarios = new UsuarioDB();

     public Establecidas(String titulo){
    	ConfiguracionDB confs = new ConfiguracionDB();
    	conf = confs.getObjeto();
    	confs = null;
		sSeccion = mostrarSeccion();
		chSeccion = new mkpyLabelChoiceField("Sección: ", sSeccion, 0, ObjectChoiceField.FIELD_LEFT, Color.BLACK, Color.WHITE);
		editNombre = new mkpyLabelEditField("Nombre: ", null, 50, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Color.BLACK, Color.WHITE); 
		editDocIdent = new mkpyLabelEditField("Doc.identidad : ", null, conf.getMaxDocIdentidad(), EditField.NO_NEWLINE | EditField.FILTER_NUMERIC, Color.BLACK, Color.WHITE);
		editCodigoConsultora = new mkpyLabelEditField("Código Consultora: ", null, conf.getMinBuscaCodigo(), EditField.NO_NEWLINE | EditField.FIELD_LEFT | EditField.FILTER_NUMERIC, Color.BLACK, Color.WHITE);

		btnBuscar = new ButtonField("Buscar", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
		btnBuscar.setChangeListener(this);

		add(new BitmapField(Bitmap.getBitmapResource("img/titulos/buscarconsultora.png"), BitmapField.FIELD_HCENTER));
        add(editNombre);
        if ( conf.getMostrarDocIdentidad().equals("1") ) {
            add(editDocIdent);
        }
        add(editCodigoConsultora);
        add(chSeccion);
        add(btnBuscar);
        addMenuItem(_buscar);
        addMenuItem(_generarLista);
        Sistema.addEstadistica(Cadenas.BUSCARCONSULTORA);
    }

 	public void fieldChanged(Field field, int context) {
        if ( field == btnBuscar ) {
        	buscar();
        }
    }
 	
    public static String[] mostrarSeccion() {
    	String specificationArray[] = null;        
    	secciones = new SeccionDB();
        Vector vSeccion = null;
        Seccion seccion;
        try {
            vSeccion = secciones.getObjetos();
            specificationArray = new String[vSeccion.size() + 1];
            specificationArray[0] = "Todas";
            int i = 1;
            for (int count = 0; count < vSeccion.size(); i++, count++) {
            	seccion = (Seccion) vSeccion.elementAt(count);
                specificationArray[i] = seccion.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar las secciones. ");
        }
	 
        return specificationArray;
    }

	private void buscar() {
		String seleccion;
		if(chSeccion.getSelectedIndex() == 0)
			seleccion = "";
		else{
			secciones = new SeccionDB();
			seleccion = secciones.getSeccionByNombre(sSeccion[chSeccion.getSelectedIndex()]);
		}
		consultaSeccion(seleccion);	
	}
    
    private void consultaSeccion(String seleccion) {
    	int n = 0;
    	String nombre, docIdent, codConsultora;

    	nombre = editNombre.getText();
    	n = nombre.length();
    	if( n > 0 ) {
    		if(  n < conf.getMinBuscaNombre() ) {
        		Dialog.alert("Debe ingresar al menos " + conf.getMinBuscaNombre() + " caracteres en el nombre.");
        		return;
    		}
    	}
    	docIdent = editDocIdent.getText();
    	n = docIdent.length();
    	if( n > 0 ) {
    		if(  n < conf.getMinDocIdentidad() ) {
        		Dialog.alert("Debe ingresar al menos " + conf.getMinDocIdentidad() + " caracteres en el documento.");
        		return;
    		}
    	}
    	
    	codConsultora =  editCodigoConsultora.getText();
    	n = codConsultora.length();
    	if( n > 0 ) {
    		if(  n < conf.getMinBuscaCodigo() ) {
        		Dialog.alert("Debe ingresar al menos " + conf.getMinBuscaCodigo() + " caracteres en el código de la consultora."); 
        		return;
    		}
    	}
    	
    	if ( nombre.length() == 0 && docIdent.length() == 0 && codConsultora.length() == 0 && chSeccion.getSelectedIndex() == 0 ) {
    		if ( Dialog.ask(Dialog.D_YES_NO, "Desea continuar sin ingresar datos?") == Dialog.NO ) {
    			return;
    		}
    	}
    	
		progress.setTitle("Buscando...");
    	progress.open();
    	progress.close();
    	try {
        	Estilos.pushScreen(new ConsultaSeccion( seleccion, nombre, docIdent, codConsultora, true));
    	} catch(Exception e) {
    		
    	}
	}

	MenuItem _buscar = new MenuItem("Buscar", 110, 10) {
        public void run() {
            buscar();
        }
    };
    
	MenuItem _generarLista = new MenuItem("Generar Listado", 110, 10) {
        public void run() {
            generarLista();
        }
    };
    
    private void generarLista() {
    	UiApplication.getUiApplication().pushScreen(new GenerarListas());
    }

    protected boolean onSavePrompt() {
        return true;
    } 
}
