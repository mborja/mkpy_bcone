package com.belcorp.ui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.container.MainScreen;
import com.belcorp.entidades.Consultora;
import com.belcorp.dao.ConsultoraDB;
import com.belcorp.dao.MotivoNoVistaDB;
import com.belcorp.entidades.MotivoNoVisita;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.mkpyImageEditField;
import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyStatusProgress;

public class NoExitoConsultora  extends MainScreen implements ListFieldCallback{
    private mkpyImageEditField editTelef1, editTelef2, editTelef3, editEmail; 
    private ConsultoraDB consultoras;
    private Consultora consultora;
    private Vector _listData = new Vector();
    private ListField _checkList;
    private MotivoNoVistaDB motivos = new MotivoNoVistaDB();
    private mkpyStatusProgress progress = new mkpyStatusProgress(""); 
    private MainScreen menuContactos;
    private MainScreen menuSeccion;
    private MainScreen consultaSeccion;
    private MainScreen datosConsultora;
    private static final String NO_VISITADA = "NVE";
    private int pantalla;
    private int origen;  // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
    
    public NoExitoConsultora(int orig, Consultora cons, MainScreen menuContactos, MainScreen menuSeccion, MainScreen consultaSeccion, MainScreen datosConsultora){
        consultora = cons;
        this.menuContactos = menuContactos;
        this.menuSeccion = menuSeccion;
        this.consultaSeccion = consultaSeccion;
        this.datosConsultora = datosConsultora;
        this.origen = orig;
        pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());
        
        _checkList = new ListField();
        llenaMotivos();
        _checkList.setCallback(this);
        add(new BitmapField(Bitmap.getBitmapResource("img/titulos/nuevasvisitanoexitosa.png"), BitmapField.FIELD_HCENTER));
        //INICIO: datos consultora
        add(new LabelField(consultora.getNombre(), LabelField.ELLIPSIS));        
        add(new mkpyLabelLabelField("Cod.Consultora: ", consultora.getCodigo(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
        add(new mkpyLabelLabelField("Doc.Identidad: ", consultora.getDocIdentidad(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));

        editTelef1 = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/telefono1.png"), consultora.getTelefono1(), 15, EditField.FILTER_PHONE | EditField.NO_NEWLINE, Color.BLACK, Color.WHITE);
        editTelef2 = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/telefono2.png"), consultora.getTelefono2(), 15, EditField.FILTER_PHONE | EditField.NO_NEWLINE, Color.BLACK, Color.WHITE);
        editTelef3 = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/telefono3.png"), consultora.getTelefono3(), 15, EditField.FILTER_PHONE | EditField.NO_NEWLINE, Color.BLACK, Color.WHITE);
        editEmail = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/correo.png"), consultora.getEmail(), 100, EditField.NO_NEWLINE | EditField.FILTER_EMAIL, Color.BLACK, Color.WHITE);
        
        add(editTelef1);
        add(editTelef2);
        add(editTelef3);
        add(editEmail);
        
        add(new mkpyLabelLabelField("Campaña de Ingreso: ", consultora.getCampanaIngreso(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
        add(new SeparatorField());
        add(new mkpyLabelLabelField("Motivos: ", "", LabelField.NON_FOCUSABLE, Color.BLACK, Color.WHITE));
        //FIN: datos consultora

        add(_checkList);
        //if(!consultora.getVisitada().equals(NO_VISITADA))
		if( pantalla <= 4) {
        	addMenuItem(registrar);
		}
        addMenuItem(actualizar);

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        Sistema.addEstadistica(Cadenas.V1_VISITANOEXITOSA);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_VISITANOEXITOSA);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_VISITANOEXITOSA);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_VISITANOEXITOSA);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else if( origen == 1 ) {
	        //Sistema.addEstadistica(Cadenas.BC_VISITANOEXITOSA);
        } else if ( origen == 2 ) {
	        //Sistema.addEstadistica(Cadenas.GL_VISITANOEXITOSA);
        } 
    }

    private void llenaMotivos() {
    	_listData = new Vector();
        int elementLength = motivos.getObjetos().size();
        for(int count = 0; count < elementLength; ++count) {
            MotivoNoVisita nomotivo = (MotivoNoVisita) motivos.getObjetos().elementAt(count);
            _listData.addElement(new ChecklistData(nomotivo.getDescripcion(), false));
            _checkList.insert(count);
        }
    }
    
	protected boolean navigationClick(int status, int time) {
		Field field = this.getFieldWithFocus();
		if(field == _checkList) {
			for ( int i = 0; i < _listData.size(); i++ ) {
	            ChecklistData data = (ChecklistData)_listData.elementAt(i);
	            if ( data.isChecked() )
	            	data.toggleChecked();
			}
            int index = _checkList.getSelectedIndex();
            ChecklistData data = (ChecklistData)_listData.elementAt(index);
            data.toggleChecked();
            _listData.setElementAt(data, index);
            _checkList.invalidate();
			return true;
		}
		return super.navigationClick(status, time);
	}
	
    MenuItem actualizar = new MenuItem("Actualizar datos", 110, 10) {
        public void run() {
            actualizar();
        }
    };
    
    private boolean changeData(int seccion) {
    	boolean result = false;
    	switch(seccion) {
	    	case 0:
	        	if ( consultora.getTelefono1().equals(editTelef1.getText()) && 
	        			consultora.getTelefono2().equals(editTelef2.getText()) &&
	        			consultora.getTelefono3().equals(editTelef3.getText()) && 
	        			consultora.getEmail().equals(editEmail.getText()) ) {
	        		result = false;
	        	} else {
	        		result = true;
	        	}
	        	break;
	    	case 1:
	        	//MotivoNoVisita motivo = (MotivoNoVisita) motivos.getObjetos().elementAt(_checkList.getSelectedIndex());
	    		//Dialog.alert(consultora.getIdMotivosNoVisita());
	    		int index = getChecked();
	    		if ( index >= 0 ) {
	        		result = true;
	    		} else {
	        		result = false;
	    		}
	    		
	        	break;
    	}
    	return result;
    }
    
    private int getChecked() {
    	int n = _listData.size();
    	for (int i = 0; i < n; i++) {
    		ChecklistData item = (ChecklistData) _listData.elementAt(i);
    		if ( item.isChecked() ) {
    			return i; 
    		}
    	}
    	return -1;
    }
    
    private void actualizar() {
    	boolean modifica = false, modMotivo = false;
 	   
    	modifica = changeData(0); 	
    	//modMotivo = changeData(1);
    
    	//if ( modifica || modMotivo ) {
    	if ( modifica ) {
    		if ( Dialog.ask(Dialog.D_YES_NO, "¿Está seguro de actualizar los datos?") == Dialog.YES ){
			   
	    		progress.open();
	    		progress.setTitle("Actualizando datos");
	    		    		
		        consultoras = new ConsultoraDB();
		        boolean resultado = true;
		        

		    	if ( modifica ) {
		    		consultora.setTelefono1(editTelef1.getText());
		    		consultora.setTelefono2(editTelef2.getText());
		    		consultora.setTelefono3(editTelef3.getText());
		    		consultora.setEmail(editEmail.getText());		    		
		    		resultado = resultado && consultoras.sendUpdate(consultora, 0);
		    	} 

//		    	if ( modMotivo ) {	   	    	
//		   	    	consultora.setMotivosNoVisita(String.valueOf(_checkList.getSelectedIndex()));
//		    		resultado = resultado && consultoras.sendUpdate(consultora, 1);
//		    	}               
		    	if ( resultado ) {
		            Dialog.inform("Los datos se actualizaron con éxito");
		    	} else {
		            Dialog.inform("Los datos NO se actualizaron con éxito");
		    	}
		    	progress.close();
    		}
    	} else {
    		Dialog.alert("No se han modificado los datos de la consultora.");
    		return;
    	}
    }
            
    MenuItem registrar = new MenuItem("Visita no exitosa", 110, 10) {
        public void run() {
            visitaNoExito();
        }

        private void visitaNoExito() {
        	boolean modMotivo = changeData(1);
        	if ( modMotivo ) {
            	actualizar();
            	if ( Dialog.ask(Dialog.D_YES_NO, "¿Está seguro de registrar la visita no exitosa?") == Dialog.YES ){
        	    	progress.open();
        	    	progress.setTitle("Registrando la vista no exitosa");
        	        consultoras = new ConsultoraDB();

    	    		int index = getChecked();
    	            MotivoNoVisita nomotivo = (MotivoNoVisita) motivos.getObjetos().elementAt(index);
                	consultora.setIdMotivosNoVisita( nomotivo.getId() );
        	        
        	        if ( consultoras.sendUpdate(consultora, 5) ) {
        	            Dialog.inform("La visita no exitosa se registró con éxito");
        	            close();
        	        	//NoExitoConsultora.this.close();
        	        	//datosConsultora.close();
        	        	//Estilos.pushScreen(new DatosConsultora(consultora, menuContactos, menuSeccion, consultaSeccion, false));
        	        } else {
        	            Dialog.inform("La visita NO se registró con éxito");
        	        }
        	        progress.close();
        		}
        	} else {
        		Dialog.alert("Debe seleccionar un motivo de no visita.");
        	}
        }
    };            

    public void drawListRow(ListField list, Graphics graphics, int index, int y, int w) {
        ChecklistData currentRow = (ChecklistData)this.get(list, index);
        StringBuffer rowString = new StringBuffer();

        if (currentRow.isChecked()) {
            rowString.append(Characters.BALLOT_BOX_WITH_CHECK);
        } else {
            rowString.append(Characters.BALLOT_BOX);
        }
        
        rowString.append(Characters.SPACE);
        rowString.append(Characters.SPACE);
        rowString.append(currentRow.getStringVal());
        
        graphics.drawText(rowString.toString(), 0, y, 0, w);
    }
            
    public Object get(ListField list, int index) {
        return _listData.elementAt(index);
    }
            
    public int indexOfList(ListField list, String p, int s) {
        //return listElements.getSelectedIndex();
        return _listData.indexOf(p, s);
    }
            
    public int getPreferredWidth(ListField list)  { 
        return this.getHeight();
    }
            
    private class ChecklistData{
        private String _stringVal;
        private boolean _checked;
                
//        ChecklistData() {
//            _stringVal = "";
//            _checked = false;
//        }
                
        ChecklistData(String stringVal, boolean checked) {
            _stringVal = stringVal;
            _checked = checked;
        }
                
        //Get/set methods.
        private String getStringVal() {
            return _stringVal;
        }
        
        private boolean isChecked() {
            return _checked;
        }
        
//        private void setStringVal(String stringVal) {
//            _stringVal = stringVal;
//        }
//        
//        private void setChecked(boolean checked) {
//            _checked = checked;
//        }
        
        //Toggle the checked status.
        private void toggleChecked() {
            _checked = !_checked;
        }
    }
    
    protected boolean onSavePrompt() {
    	boolean modifica = false, modMotivo = false;
    	modifica = changeData(0);
    	//modMotivo = changeData(1);
    	//if ( modifica || modMotivo  ) {
    	if ( modifica ) {
    		if ( Dialog.ask(Dialog.D_YES_NO, "Descartar los cambios?") == Dialog.YES ) 
    			return true;
    		else
    			return false;
    	} else {
	       return true;
    	}
    } 

}
