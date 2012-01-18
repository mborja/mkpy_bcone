package com.belcorp.ui;

import com.belcorp.dao.AtraccionDB;
import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Atraccion;
import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyEditField;
import com.makipuray.ui.mkpyLabelField;
import com.makipuray.ui.mkpyStatusProgress;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.Dialog;

public class Atracciones extends MainScreen implements FieldChangeListener {
    private LabelField lblResultado;
    private EditField txtDocumento;
    private ButtonField btnValidar;
    private LabelField lblEstado;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    private Configuracion conf;
    private String estado;
    private Usuario usuario;
    
    /**
     * Constructor de la pantalla de atracciones
     */
    public Atracciones() {
    	ConfiguracionDB confs = new ConfiguracionDB();
    	conf = confs.getObjeto();
    	confs = null;
    	UsuarioDB usuarios = new UsuarioDB();
    	usuario = usuarios.getUsuario();
    	usuarios = null;
        txtDocumento = new mkpyEditField(null, "", conf.getMaxDocIdentidad(), EditField.NO_NEWLINE | EditField.FIELD_LEFT | EditField.FILTER_NUMERIC);
        lblResultado = new LabelField("", LabelField.FIELD_HCENTER);
        btnValidar = new ButtonField("Validar", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
        estado = usuario.getFechaHoraUltimaSincronizacionConsultoraFormato();
        lblEstado = new mkpyLabelField("", LabelField.USE_ALL_WIDTH | LabelField.FIELD_LEFT, Color.WHITE, Estilos.getBGModulo());

        btnValidar.setChangeListener(this);
        
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/atraccion.png"), BitmapField.FIELD_HCENTER));
        //add(new LabelField("Verificacion de Postulante", LabelField.FIELD_HCENTER | LabelField.NON_FOCUSABLE));
        add(new LabelField("Doc.identidad", LabelField.FIELD_LEFT | LabelField.NON_FOCUSABLE));
        add(txtDocumento);
        add(btnValidar);
        add(new SeparatorField());
        add(new LabelField("", LabelField.FIELD_LEFT | LabelField.NON_FOCUSABLE));
        add(lblResultado);
        addMenuItem(buscar);
        setStatus(lblEstado);

        Sistema.addEstadistica(Cadenas.ATRACCION);
    }

    /**
     * Accion sobre el menu validar
     */
    MenuItem buscar = new MenuItem("Validar", 110, 10) {
        public void run() {
            verificar();
        }
    };
    
    /**
     * Metod de verificacion de los datos y busqueda del documento de manera local y remota
     */
    private void verificar() {
        if ( txtDocumento.getText().length() < conf.getMinDocIdentidad() ) {
            Dialog.inform("¡ Error de búsqueda, ingrese como minimo " + conf.getMinDocIdentidad() + " caracteres.");
            return ;
        }
        progress.open();
        progress.setTitle("Verificando...");
    	//TODO: limpiar la informacion de Atraccion cada cierto tiempo
        AtraccionDB atracciones = new AtraccionDB();
        Atraccion atraccion = atracciones.getAtraccionByDoc(txtDocumento.getText());
        if ( atraccion == null ) {
            lblResultado.setText("No se encontro el documento de identidad");
        } else {
        	lblResultado.setText(atraccion.getResultado());
        	lblEstado.setText(Cadenas.getEspacio(estado.length()).concat(atraccion.getFechaConsulta()));
        }
        progress.close();
    }

    /**
     * Metodo para pedir confirmacion de descartar los datos ingresados en la pantalla
     */
    protected boolean onSavePrompt() {
           return true;
    }

    /**
     * Manejador de los eventos del FieldChangeListener 
     */
	public void fieldChanged(Field field, int context) {
        if ( field == btnValidar ) {
            verificar();
        }
	}    

}
