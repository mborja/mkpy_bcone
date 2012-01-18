package com.belcorp.ui;

import com.belcorp.dao.ConfiguracionDB;
import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Configuracion;
import com.belcorp.entidades.Usuario;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyEditField;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyStatusProgress;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

public class RecordarClave extends MainScreen implements FieldChangeListener {
    private mkpyLabelEditField txtUsuario;
    private ButtonField btnEnviar;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");   
    
    /**
     * Constructor de la clase de recordar clave
     */
    public RecordarClave() {
    	txtUsuario = new mkpyLabelEditField("Usuario: ", null, 15, EditField.NO_NEWLINE | EditField.FIELD_LEFT | EditField.HIGHLIGHT_SELECT, Color.BLACK, Color.WHITE);
        btnEnviar = new ButtonField("Enviar", ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
        btnEnviar.setChangeListener(this);
        
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/recordarclave.png"), BitmapField.FIELD_HCENTER));
        add(txtUsuario);
        add(btnEnviar);
        addMenuItem(enviar);
        add(new SeparatorField());

        Sistema.addEstadistica(Cadenas.RECORDARCLAVE);
    }

    /**
     * Opción de menú para el envío de la clave
     */
    MenuItem enviar = new MenuItem("Enviar", 110, 10) {
        public void run() {
            enviar();
        }
    };
    

    protected boolean onSavePrompt() {
           return true;
    }

	public void fieldChanged(Field field, int context) {
        if ( field == btnEnviar ) {
            enviar();
        }
	}

	/**
	 * Validación y envío de la clave
	 */
    private void enviar() {
      if ( txtUsuario.getText().trim().length() == 0 ) {
          Dialog.inform("¡ Error, ingrese un usuario.");
          return ;
      }
      progress.open();
      progress.setTitle("Enviando...");
      UsuarioDB usuarios = new UsuarioDB();
      if ( usuarios.enviaClave(txtUsuario.getText()) ) {
          Dialog.inform("Se envió con éxito");
          progress.close();
          close();
      } else {
          progress.close();
          Dialog.inform("¡ Error, no se pudo enviar la clave.");
      }
      usuarios = null;
  }

}
