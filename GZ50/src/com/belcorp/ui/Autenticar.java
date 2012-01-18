package com.belcorp.ui;

/*
 * Login.java
 *
 * © Makipuray, 2008-2011
 * Confidential and proprietary.
 */


import javax.microedition.lcdui.TextField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.belcorp.dao.PaisDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Pais;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyEditField;
import com.makipuray.ui.mkpyLabelChoiceField;
import com.makipuray.ui.mkpyPasswordEditField;

/**
 * 
 */
public class Autenticar extends MainScreen implements FieldChangeListener {
   private EditField txtUsuario;
   private PasswordEditField txtClave;
   private CheckboxField chkRecordarme;
   private ButtonField btnIngresar;
   private Usuario usuario = null; 
//   private MetodosGlobales meGlobales = new MetodosGlobales();
   private UsuarioDB usuarios = new UsuarioDB();
   private PaisDB paises = new PaisDB();
   private mkpyLabelChoiceField chPais;
   private VerticalFieldManager vField;
   private boolean isPaisVisible = false;
   private Pais pais = null;
   
   /**
    * Constructor de la pantalla de Autenticacion
    */
   public Autenticar() { 
       usuario = usuarios.getUsuario();
	   vField = new VerticalFieldManager();
//	   meGlobales.setPerfil(perfil);

       HorizontalFieldManager hField = new HorizontalFieldManager() {
   		protected void sublayout(int width, int height) {
			super.sublayout(super.getScreen().getWidth(), height);
			super.setExtent(super.getScreen().getWidth(), getField(0).getHeight() + 50);
			Field field;
			
			field = getField(0);
			layoutChild(field, width / 2, height);
			setPositionChild(field, 5, 0);
			
			field = getField(1);
			layoutChild(field, width, height);
			setPositionChild(field, (width / 2) + (width / 4) - ( field.getPreferredWidth() / 2), 0);

       	}
       };
	   
       txtUsuario = new mkpyEditField(null, "", 30, EditField.USE_ALL_WIDTH | EditField.NO_NEWLINE | EditField.FIELD_LEFT); 
       txtClave = new mkpyPasswordEditField(null, "", 30, PasswordEditField.NO_NEWLINE | PasswordEditField.FIELD_LEFT);
       chkRecordarme = new CheckboxField("Recordar mi clave", false, CheckboxField.FIELD_HCENTER);
       chkRecordarme.setChecked(true);
       btnIngresar = new ButtonField("Iniciar", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
       btnIngresar.setChangeListener(this);

       if ( usuario == null ) {
    	   chPais = new mkpyLabelChoiceField("País: ", MetodosGlobales.mostrarPaises(), 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
    	   vField.add(chPais);
    	   isPaisVisible = true;
       }
	   vField.add(new LabelField("Usuario: ", LabelField.FIELD_LEFT | LabelField.NON_FOCUSABLE));
	   vField.add(txtUsuario);
	   vField.add(new LabelField("Clave: ", LabelField.FIELD_LEFT | LabelField.NON_FOCUSABLE));
	   vField.add(txtClave);

	   hField.add(vField);
	   hField.add(new BitmapField(Bitmap.getBitmapResource("img/logo.png"), BitmapField.FIELD_HCENTER | BitmapField.FIELD_VCENTER | BitmapField.USE_ALL_WIDTH));
	   add(new BitmapField(Bitmap.getBitmapResource("img/titulos/login.png"), BitmapField.FIELD_HCENTER));
	   
	   
	   add(new LabelField("", LabelField.USE_ALL_WIDTH));
	   add(hField);
	   //add(new LabelField("", LabelField.USE_ALL_WIDTH));
	   add(chkRecordarme);
       add(btnIngresar);
       addMenuItem(iniciar);
       addMenuItem(miRecordarClave);
       addMenuItem(cambiarPais);
       
       Sistema.addEstadistica(Cadenas.LOGIN);
       
   }

   /**
    * Manejador de los eventos del FieldChangeListener 
    */
	public void fieldChanged(Field field, int context) {
        if ( field == btnIngresar ) {
            ingresar();
        }
    }

	/**
	 * Metodo para validar al usuario
	 */
    public void ingresar() {
    	if ( ! validarCamposUsrPwd() ) {
        	Dialog.inform("Ingrese Usuario o clave");
        	return;
        }
        usuario = usuarios.getUsuario();
        if( usuario == null ){//si es la primera vez que se loguea
        	int index;
        	if ( pais == null ) {
            	index = chPais.getSelectedIndex();
            	pais = (Pais) paises.getObjetos().elementAt(index);
        	}
        	usuarios.noExisteUsr(txtUsuario.getText(), txtClave.getText(), chkRecordarme.getChecked(), usuario, pais.getId(), pais.getFuerzaVenta());
        } else { // ya existe el usuario
        	if ( usuario.getCodigo().equals(txtUsuario.getText()) && usuario.getClave().equals(txtClave.getText()) ) {
        		usuario.setAutovalidar(chkRecordarme.getChecked());
        		usuarios.existeUsr();
        	} else { // cambio de usuario
            	pais = new Pais();
            	pais.setId(usuario.getIdPais());
            	pais.setFuerzaVenta(usuario.getIdEmpresa());
        		usuario = null;
        		usuarios.setUsuario(usuario);
        		ingresar();
        	}
        }
    }

    /**
     * Metodo para la validacion de los datos ingresados
     * @return si los datos son validos o no
     */
    private boolean validarCamposUsrPwd(){
	   	 if ( txtUsuario.getText().equals("") || txtClave.getText().equals("") ) {
	   		 return false;
	   	 }
	   	 return true;
   }

    /**
     * Accion sobre el menu recordar clave
     */
    MenuItem miRecordarClave = new MenuItem("Enviar clave", 110, 10) {
        public void run() {
        	Estilos.pushScreen(new RecordarClave());
        } 
    };

    /**
     * Accion sobre el menu iniciar
     */
    MenuItem iniciar = new MenuItem ("Iniciar", 110, 10) {
        public void run() {
            ingresar();
        }
    };
    
    /**
     * Accion sobre el menu cambiar pais
     */
    MenuItem cambiarPais = new MenuItem ("Cambiar país", 110, 10) {
        public void run() {
        	if ( ! isPaisVisible ) {
        		chPais = new mkpyLabelChoiceField("País: ", MetodosGlobales.mostrarPaises(), 0, EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
            	usuario = null;
            	usuarios.setUsuario(usuario);
         	   	vField.insert(chPais, 0);
         	   	isPaisVisible = true;
        	}
        }
    };
    
    /**
     * Metodo para pedir confirmacion de descartar los datos ingresados en la pantalla
     */
    protected boolean onSavePrompt() {
           return true;
    } 

} 

