package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.dao.EstadoConsultoraDB;
import com.belcorp.dao.NivelDB;
import com.belcorp.dao.SeccionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.EstadoConsultora;
import com.belcorp.entidades.Nivel;
import com.belcorp.entidades.Seccion;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyLabelChoiceField;
import com.makipuray.ui.mkpyStatusProgress;

public class GenerarListas extends MainScreen implements FieldChangeListener {
	private mkpyLabelChoiceField chSeccion, chNivel, chEstado, chEstadoPedido, chDeuda;
	private static final String sTrueFalse[] = {"No", "Sí", "Todos"}; 
	private static String seccion[], nivel[], estado[], sEstadoPedido[]; 
	private static SeccionDB secciones;
	private static NivelDB niveles;
	private static EstadoConsultoraDB estados;
	private ButtonField btnBuscar;
	private mkpyStatusProgress progress = new mkpyStatusProgress("");
	private Usuario usuario;
	  
	public GenerarListas(){
		UsuarioDB usuarios = new UsuarioDB();
		usuario = usuarios.getUsuario();
		usuarios = null;
		seccion = MetodosGlobales.mostrarSeccion();
		nivel = MetodosGlobales.mostrarNivel();
		estado = MetodosGlobales.mostrarEstado();
		sEstadoPedido = MetodosGlobales.getCateEstadoPedido();
	    chSeccion = new mkpyLabelChoiceField("Sección: ", seccion, 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
	    chNivel = new mkpyLabelChoiceField("Nivel: ", nivel, 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
	    chEstado = new mkpyLabelChoiceField("Estado: ", estado, 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
		chDeuda = new mkpyLabelChoiceField("Deuda: ", sTrueFalse, 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
		chEstadoPedido = new mkpyLabelChoiceField("Pedido: ", sEstadoPedido, 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
		btnBuscar = new ButtonField("Generar Listado", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
		btnBuscar.setChangeListener(this);
		
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/generarlistado.png"), BitmapField.FIELD_HCENTER));
		add(chSeccion);
		add(chNivel);
		add(chEstado);
		add(chDeuda);
		add(chEstadoPedido);
        add(btnBuscar);
        chDeuda.setSelectedIndex(sTrueFalse.length - 1);
        chEstadoPedido.setSelectedIndex(sEstadoPedido.length - 1);
        addMenuItem(mnBuscar);
        Sistema.addEstadistica(Cadenas.GENERARLISTADO);
	}

    MenuItem mnBuscar = new MenuItem ("Generar Listado", 110, 10) {
        public void run() {
        	buscar();
        }
    };
	
 	public void fieldChanged(Field field, int context) {
        if ( field == btnBuscar ) {
        	buscar();
        }
    }

    private void buscar() {
		String seleccionSeccion, seleccionNivel, seleccionEstado, seleccionDeuda, seleccionEstadoPedido;

		if(chSeccion.getSelectedIndex() == 0)
			seleccionSeccion = "";
		else{
			secciones = new SeccionDB();
			seleccionSeccion = secciones.getSeccionByNombre(seccion[chSeccion.getSelectedIndex()]);
		}
		if(chNivel.getSelectedIndex() == 0)
			seleccionNivel = "";
		else{
			niveles = new NivelDB();
			seleccionNivel = niveles.getNivelByNombre(nivel[chNivel.getSelectedIndex()]);
		}
		
		if(chEstado.getSelectedIndex() == 0)
			seleccionEstado = "";
		else{
			estados = new EstadoConsultoraDB();
			seleccionEstado = estados.getEstadoByNombre(estado[chEstado.getSelectedIndex()]);
		}
			
		if(chDeuda.getSelectedIndex() == 2)
			seleccionDeuda = "";
		else{
			seleccionDeuda = "" + chDeuda.getSelectedIndex();
		}

		if ( chEstadoPedido.getSelectedIndex() == sEstadoPedido.length - 1 ) {
			seleccionEstadoPedido = "";
		} else {
			seleccionEstadoPedido = "" + chEstadoPedido.getSelectedIndex();
		}

		
    	if ( seleccionSeccion.equals("") && seleccionNivel.equals("") && seleccionEstado.equals("") && seleccionDeuda.equals("") && seleccionEstadoPedido.equals("") ) {
    		if ( Dialog.ask(Dialog.D_YES_NO, "Desea continuar sin ingresar datos?") == Dialog.NO ) {
    			return;
    		}
    	}
		
		progress.setTitle("Buscando...");
    	progress.open();
    	progress.close();
    	try {
        	Estilos.pushScreen(new ConsultaSeccion(usuario.getCampana(), seleccionSeccion, seleccionNivel, seleccionEstado, seleccionDeuda, seleccionEstadoPedido));
    	} catch(Exception e) {
    		
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
    
    public static String[] mostrarNivel() {
    	String specificationArray[] = null;        
    	niveles = new NivelDB();
        Vector vNivel = null;
        Nivel nivel;
        try {
            vNivel = niveles.getObjetos();
            specificationArray = new String[vNivel.size() + 1];
            specificationArray[0] = "Todas";
            int i = 1;
            for (int count = 0; count < vNivel.size(); i++, count++) {
            	nivel = (Nivel) vNivel.elementAt(count);
                specificationArray[i] = nivel.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar los niveles. ");
        }
	 
        return specificationArray;
    }
    
    public static String[] mostrarEstado() {
    	String specificationArray[] = null;        
    	estados = new EstadoConsultoraDB();
        Vector vEstado = null;
        EstadoConsultora estado;
        try {
            vEstado = estados.getObjetos();
            specificationArray = new String[vEstado.size() + 1];
            specificationArray[0] = "Todas";
            int i = 1;
            for (int count = 0; count < vEstado.size(); i++, count++) {
            	estado = (EstadoConsultora) vEstado.elementAt(count);
                specificationArray[i] = estado.getDescripcion();
            }
        } catch (Exception e) {
            Dialog.alert("no se pudo descargar los estados. ");
        }
	 
        return specificationArray;
    }

	protected boolean onSavePrompt() {
        return true;
    } 

}
