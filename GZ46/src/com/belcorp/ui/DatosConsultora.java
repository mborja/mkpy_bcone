package com.belcorp.ui;

import javax.microedition.global.Formatter;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.invoke.PhoneArguments;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ChoiceField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

import com.belcorp.dao.ConsultoraDB;
import com.belcorp.dao.EstadoConsultoraDB;
import com.belcorp.dao.MetaDB;
import com.belcorp.dao.NivelDB;
import com.belcorp.entidades.Consultora;
import com.belcorp.entidades.Meta;
import com.belcorp.utilidades.Cadenas;
import com.belcorp.utilidades.Estilos;
import com.belcorp.utilidades.Sistema;
import com.makipuray.ui.HyperlinkField;
import com.makipuray.ui.mkpyImageEditField;
import com.makipuray.ui.mkpyImageLabelField;
import com.makipuray.ui.mkpyLabelEditField;
import com.makipuray.ui.mkpyLabelField;
import com.makipuray.ui.mkpyLabelLabelField;
import com.makipuray.ui.mkpyObjectChoiceField;
import com.makipuray.ui.mkpyStatusProgress;

public class DatosConsultora extends MainScreen implements FieldChangeListener {
    private ChoiceField chTipoMeta;
    private mkpyLabelLabelField lblRangoMeta;
    private mkpyLabelEditField editMontoMeta, editMeta, editDupla;
    private mkpyImageEditField editTelef1, editTelef2, editTelef3, editEmail, editAnotacion;
    private HyperlinkField hypVenta, hypVentaxMarca, hypPuntaje, hypCDR, hypAnotaciones;
    private MetaDB metas = new MetaDB();
    private ConsultoraDB consultoras;
    private Consultora consultora;
    private String  verVenta = "Ver Venta", 
					verVentaxMarca = "Ver Venta x Marca",
					verPuntaje = "Ver Puntaje",
					verPosVenta = "Ver CDR",
					verAnotaciones = "Historial de anotaciones";
    private int pantalla = 0;
    private MainScreen menuContactos;
    private MainScreen menuSeccion;
    private MainScreen consultaSeccion;
    private mkpyStatusProgress progress = new mkpyStatusProgress("");  
    private boolean isEstablecidas = false, metaCompleta = true;
    private EstadoConsultoraDB estados = new EstadoConsultoraDB();
    private NivelDB niveles = new NivelDB();
    private int origen;  // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion

    public DatosConsultora(int orig, Consultora consultora, MainScreen menuContactos, MainScreen menuSeccion, MainScreen consultaSeccion, boolean isEstablecidas){
		this.consultora = consultora;
		this.menuContactos = menuContactos;
		this.menuSeccion = menuSeccion;
		this.consultaSeccion = consultaSeccion;
		this.isEstablecidas = isEstablecidas;
		int pantalla = Integer.parseInt(consultora.getClasificacionMetodologica());
		this.origen = orig;
		datosConsultora(pantalla);
   }
    
    private void datosConsultora(int screen) {
    	Formatter formato =  new Formatter();
    	pantalla = screen;
        //INICIO: datos consultora
        add(new BitmapField(Bitmap.getBitmapResource(Cadenas.getClasificacionMetodologica(consultora.getClasificacionMetodologica())), BitmapField.FIELD_HCENTER));
        add( new mkpyLabelField(consultora.getNombre(), LabelField.USE_ALL_WIDTH | LabelField.FOCUSABLE | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, true) );
        add( new mkpyImageLabelField(Bitmap.getBitmapResource("img/consultora/direccion.png"), consultora.getDireccion(), LabelField.NON_FOCUSABLE | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, true) );
        add( new mkpyLabelLabelField("Cod.Consultora: ", consultora.getCodigo(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) );
        add( new mkpyLabelLabelField("Doc.Identidad: ", consultora.getDocIdentidad(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) );
        editTelef1 = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/telefono1.png"), consultora.getTelefono1(), 30, EditField.FILTER_PHONE | EditField.NO_NEWLINE, Color.BLACK, Color.WHITE); //
        add(editTelef1);
        editTelef2 = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/telefono2.png"), consultora.getTelefono2(), 30, EditField.FILTER_PHONE | EditField.NO_NEWLINE, Color.BLACK, Color.WHITE);
        add(editTelef2);
        editTelef3 = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/telefono3.png"), consultora.getTelefono3(), 30, EditField.FILTER_PHONE | EditField.NO_NEWLINE, Color.BLACK, Color.WHITE);
        add(editTelef3);
        editEmail = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/correo.png"), consultora.getEmail(), 100, EditField.NO_NEWLINE | EditField.FILTER_EMAIL, Color.BLACK, Color.WHITE);
        add(editEmail);
        add( new mkpyLabelLabelField("Campaña de Ingreso: ", consultora.getCampanaIngreso(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) );
        add( new mkpyLabelLabelField("¿Puede pasar pedido?: ", consultora.getAutorizadoPasarPedido(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) ); //se cambio .getPasoPedido() ... parece que es un error ... depende del criterio del usuario
        add( new mkpyLabelLabelField("Sección: ", consultora.getIdSeccion(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) );
        add( new mkpyLabelLabelField("¿Asistió a la conf. Triunfadoras?: ", consultora.getAsisteCompartamos(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) );
        add( new mkpyLabelLabelField("Saldo: ", Cadenas.getMoneda() + consultora.getSaldo(), EditField.NON_FOCUSABLE, Color.BLACK, Color.WHITE) );        

        hypVenta = new HyperlinkField(verVenta);
        hypVentaxMarca = new HyperlinkField(verVentaxMarca);
        hypPuntaje = new HyperlinkField(verPuntaje);
        hypCDR = new HyperlinkField(verPosVenta);
        hypAnotaciones = new HyperlinkField(verAnotaciones);
        
        //FIN: datos consultora
       
        if(pantalla == 1 ){
            editTelef1.setEditable(false);
            editTelef2.setEditable(false);
            editTelef3.setEditable(false);
            editEmail.setEditable(false);
            
            add(new SeparatorField());
            add( new mkpyLabelField("1) Felicítala por su primer pedido de ".concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getMontoUltimoPedido())).concat(" y su primera ganancia de ").concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getGananciaUltimaCampana())), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );           
            add(hypVenta);
            add(hypVentaxMarca);
            hypVenta.setBackColor(Estilos.getBGInterlinea(1));
            hypVentaxMarca.setBackColor(Estilos.getBGInterlinea(1));
            add( new mkpyLabelField("2) Explícale las ganancias de catálogos y revista Gana!", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(2), Estilos.getBGInterlinea(2)) );
            add( new mkpyLabelField("3) Explícale el programa de nuevas y los concursos vigentes.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(3), Estilos.getBGInterlinea(3)) );
            add(hypPuntaje);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(3));
            add( new mkpyLabelField("4) Explícale el Belcorp Noticias y Detalle de Factura.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(4), Estilos.getBGInterlinea(4)) );
            add( new mkpyLabelField("5) Guíala a fijar una meta y regístrala. Usa la cartilla de metas.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5)) );
            chTipoMeta = new mkpyObjectChoiceField("Tipo meta: ", mostrarMetas(), 0, ObjectChoiceField.USE_ALL_WIDTH, Estilos.getBGInterlinea(5));
            int indx;
            if(consultora.getIdMeta().equals("")) {
            	indx = 0;
            } else {
            	indx = getIndexIdMeta(consultora.getIdMeta());
            }
            add(chTipoMeta);
            add( new mkpyLabelField("Descripción de la meta", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5)) );
            editMeta = new mkpyLabelEditField("", consultora.getDescripcionMeta(), 60, EditField.NO_NEWLINE | EditField.EDITABLE, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5));
            add(editMeta);
            editMontoMeta = new mkpyLabelEditField("Monto Meta: ".concat(Cadenas.getMoneda()), String.valueOf(consultora.getMontoMeta()), 25, EditField.NO_NEWLINE | EditField.FILTER_REAL_NUMERIC | EditField.FIELD_LEFT, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5));
            add(editMontoMeta);
            lblRangoMeta = new mkpyLabelLabelField("", "", LabelField.NON_FOCUSABLE, Color.GRAY, Estilos.getBGInterlinea(5)); 
            chTipoMeta.setChangeListener(this);
            chTipoMeta.setSelectedIndex(indx);
            add(lblRangoMeta);
            if ( consultora.getMontoMeta() > 0 ) {
            	chTipoMeta.setEditable(false);
            	editMeta.setEditable(false);
            	editMontoMeta.setEditable(false);
            }
            
            if (consultora.getDatosDupla().equals(" ") ) { 
                add( new mkpyLabelField("6) Coméntale de Dupla CyZone y de www.SomosBelcorp.com.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(6), Estilos.getBGInterlinea(6)) );
            } else { 
            	add( new mkpyLabelField("6) Tiene dupla ".concat(consultora.getDatosDupla()), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(6), Estilos.getBGInterlinea(6)) );
            }

            add( new mkpyLabelField("7) Invítala a la conferencia Triunfadoras.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(7), Estilos.getBGInterlinea(7)) );
            add( new mkpyLabelField("8) Pídele 3 referidos.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(8), Estilos.getBGInterlinea(8)) );

            editAnotacion = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/anotaciones.png"), consultora.getAnotaciones(), 125, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Estilos.getColorInterlinea(9), Estilos.getBGInterlinea(9));
            add(editAnotacion);
            add(hypAnotaciones);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(9));
        } else if(pantalla == 2){
        	add(hypCDR);
            if ( consultora.getDatosDupla().equals(" ") ) { 
                editDupla = new mkpyLabelEditField("Dupla CyZone: ", "", 100, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
                add(editDupla);
            } else { 
                add( new mkpyLabelLabelField("Dupla CyZone: ", consultora.getDatosDupla(), LabelField.NON_FOCUSABLE | LabelField.FIELD_LEFT | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, true));
            }
            add(new SeparatorField());

            add( new mkpyLabelField("1) Seguimiento de meta :", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
            chTipoMeta = new mkpyObjectChoiceField("Tipo meta: ", mostrarMetas(), 0,  ObjectChoiceField.USE_ALL_WIDTH, Estilos.getBGInterlinea(1));
            add(chTipoMeta);
            
            add( new mkpyLabelField("Descripción de la meta",EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
            
            editMeta = new mkpyLabelEditField("", consultora.getDescripcionMeta(), 60, EditField.EDITABLE, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1));
            editMontoMeta = new mkpyLabelEditField("Monto meta: ".concat(Cadenas.getMoneda()), String.valueOf( consultora.getMontoMeta() ), 25, EditField.FILTER_NUMERIC | EditField.FIELD_LEFT, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1));
            add(editMeta);
            add(editMontoMeta);
            lblRangoMeta = new mkpyLabelLabelField("", "", LabelField.NON_FOCUSABLE, Color.GRAY, Estilos.getBGInterlinea(5)); 
            chTipoMeta.setChangeListener(this);
            chTipoMeta.setSelectedIndex ( getIndexIdMeta(consultora.getIdMeta()) );
            add(lblRangoMeta);
            if ( consultora.getMontoMeta() > 0 ) {
            	chTipoMeta.setEditable(false);
            	editMeta.setEditable(false);
            	editMontoMeta.setEditable(false);
            }
            
            //TODO: monto acumulado de ganancia
            add( new mkpyLabelField("Tiene ".concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getGananciaUltimaCampana())).concat(" acumulado de ganancia."), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
            if ( consultora.getMontoMeta() > 0 ) { // TIENE META
                //TODO: monto acumulado de ganancia
                add( new mkpyLabelField("Le falta ".concat(Cadenas.getMoneda()).concat(formato.formatNumber((consultora.getMontoMeta() - consultora.getGananciaUltimaCampana()),2)).concat(" para llegar a su meta."),EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
            }
            add(hypVenta);
            add(hypVentaxMarca);
            hypVenta.setBackColor(Estilos.getBGInterlinea(1));
            hypVentaxMarca.setBackColor(Estilos.getBGInterlinea(1));

            add( new mkpyLabelField("2) Seguimiento a pedidos de la próxima campaña.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(2), Estilos.getBGInterlinea(2)) );
            add( new mkpyLabelField("3) Explícale las ganancias de catálogos y revista Gana!.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(3), Estilos.getBGInterlinea(3)) );
            add( new mkpyLabelField("4) Explícale el programa de nuevas  y los concursos vigentes.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(4), Estilos.getBGInterlinea(4)) );
            add(hypPuntaje);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(4));

            add( new mkpyLabelField("5) Invítala a la conferencia Triunfadoras.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5)) );
            add( new mkpyLabelField("6) Pídele 3 referidos.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(6), Estilos.getBGInterlinea(6)) );

            editAnotacion = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/anotaciones.png"), consultora.getAnotaciones(), 125, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Estilos.getColorInterlinea(7), Estilos.getBGInterlinea(7));
            add(editAnotacion);
            add(hypAnotaciones);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(7));
        } else if(pantalla == 3) {
        	add(hypCDR);
            if ( consultora.getDatosDupla().equals(" ") ) { 
                editDupla = new mkpyLabelEditField("Dupla CyZone: ", "", 100, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
                add(editDupla);
            } else { 
                add( new mkpyLabelLabelField("Dupla CyZone: ", consultora.getDatosDupla(), LabelField.NON_FOCUSABLE | LabelField.FIELD_LEFT | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, true));
            }
            add(new SeparatorField());
            
            add( new mkpyLabelField("1) Seguimiento de meta :", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
        	add( new mkpyLabelField("Tiene ".concat(Cadenas.getMoneda()).concat(formato.formatNumber(consultora.getGananciaUltimaCampana(),2)).concat(" acumulado de ganancia."), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
            if ( consultora.getMontoMeta() > 0 ) { // TIENE META
	            //TODO: monto acumulado de ganancia
                add( new mkpyLabelField("Le falta ".concat(Cadenas.getMoneda()).concat(formato.formatNumber((consultora.getMontoMeta() - consultora.getGananciaUltimaCampana()),2)).concat(" para llegar a su meta."),EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)));
            //} else {
	            chTipoMeta = new mkpyObjectChoiceField("Tipo meta: ", mostrarMetas(), 0, ObjectChoiceField.USE_ALL_WIDTH, Estilos.getBGInterlinea(1));
	            add( new mkpyLabelField("Descripción de la meta ", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
	            editMeta = new mkpyLabelEditField("", consultora.getDescripcionMeta(), 60, EditField.EDITABLE, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1));
	            editMontoMeta = new mkpyLabelEditField("Monto meta: ".concat(Cadenas.getMoneda()), String.valueOf( consultora.getMontoMeta() ), 25, EditField.FILTER_NUMERIC | EditField.FIELD_LEFT, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1));
	            lblRangoMeta = new mkpyLabelLabelField("", "", LabelField.NON_FOCUSABLE, Color.GRAY, Estilos.getBGInterlinea(5)); 
	            chTipoMeta.setChangeListener(this);
	            chTipoMeta.setSelectedIndex ( getIndexIdMeta(consultora.getIdMeta()) );
	            add(lblRangoMeta);
            }
            add(hypVenta);
            hypVenta.setBackColor(Estilos.getBGInterlinea(1));
            add(hypVentaxMarca);
            hypVentaxMarca.setBackColor(Estilos.getBGInterlinea(1));

            add( new mkpyLabelField("2) Seguimiento a pedidos de la próxima campaña.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(2), Estilos.getBGInterlinea(2)) );
            add( new mkpyLabelField("3) Explícale las ganancias de catálogos y revista Gana!.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(3), Estilos.getBGInterlinea(3)) );
            add( new mkpyLabelField("4) Explícale el programa de nuevas  y los concursos vigentes.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(4), Estilos.getBGInterlinea(4)) );
            add(hypPuntaje);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(4));
            add( new mkpyLabelField("5) Invítala a la conferencia Triunfadoras.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5)) );
            add( new mkpyLabelField("6) Pídele 3 referidos.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(6), Estilos.getBGInterlinea(6)) );
            editAnotacion = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/anotaciones.png"), consultora.getAnotaciones(), 125, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Estilos.getColorInterlinea(7), Estilos.getBGInterlinea(7));
            add(editAnotacion);
            add(hypAnotaciones);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(7));
        } else if(pantalla == 4) {
        	add(hypCDR);
            if ( consultora.getDatosDupla().equals(" ") ) { 
                editDupla = new mkpyLabelEditField("Dupla CyZone: ", "", 100, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
                add(editDupla);
            } else { 
                add( new mkpyLabelLabelField("Dupla CyZone: ", consultora.getDatosDupla(), LabelField.NON_FOCUSABLE | LabelField.FIELD_LEFT | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, true));
            }
            add(new SeparatorField());
            add( new mkpyLabelField("1) Segumiento de meta:", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );

            if ( consultora.getMontoMeta() > 0 ) { // TIENE META
                add( new mkpyLabelField("Tiene ".concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getGananciaUltimaCampana())).concat(" acumulado de ganancia."), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
                //TODO: monto acumulado de ganancia
                chTipoMeta = new mkpyObjectChoiceField("Tipo meta: ", mostrarMetas(), 0, ObjectChoiceField.NON_FOCUSABLE | ObjectChoiceField.USE_ALL_WIDTH, Estilos.getBGInterlinea(1));
                add(chTipoMeta);
                add( new mkpyLabelField("Descripción de la meta", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
                editMeta = new mkpyLabelEditField("", consultora.getDescripcionMeta(), 60, EditField.NON_FOCUSABLE, Color.BLACK, Estilos.getBGInterlinea(1));
                add(editMeta);
                editMontoMeta = new mkpyLabelEditField("Monto meta: ".concat(Cadenas.getMoneda()), String.valueOf( consultora.getMontoMeta() ), 25, EditField.FILTER_NUMERIC | EditField.FIELD_LEFT | EditField.NON_FOCUSABLE, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1));                
                add(editMontoMeta);
                lblRangoMeta = new mkpyLabelLabelField("", "", LabelField.NON_FOCUSABLE, Color.GRAY, Estilos.getBGInterlinea(5)); 
                chTipoMeta.setChangeListener(this);
                chTipoMeta.setSelectedIndex ( getIndexIdMeta(consultora.getIdMeta()) );
                add(lblRangoMeta);
                if ( consultora.getGananciaUltimaCampana() >= consultora.getMontoMeta() ) { // CUMPLIO META
                    add( new mkpyLabelField("Felicítala por lograr los 4 primeros pedidos consecutivos y por haber alcanzado el monto ".concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getGananciaUltimaCampana())).concat(" de su meta. Pregúntale si adquirió la menta. Anímala a seguir superándose."), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
                } else { // NO CUMPLIO META
                    add( new mkpyLabelField("Felicítala por lograr los 4 primeros pedidos consecutivos y motávala para que siga adelante para lograr su meta, le faltó ".concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getMontoMeta() - consultora.getGananciaUltimaCampana())).concat(" para llegar a su meta."), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
                }
                if ( consultora.getMontoMeta() > 0 ) {
                	chTipoMeta.setEditable(false);
                	editMeta.setEditable(false);
                	editMontoMeta.setEditable(false);
                }
            } else {
                //TODO: monto acumulado de ganancia
                add( new mkpyLabelField("Felicítala por lograr los 4 primeros pedidos consecutivos y motívala a seguir superándose y lograr sus metas.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
                add( new mkpyLabelField("Tiene ".concat(Cadenas.getMoneda()).concat(String.valueOf(consultora.getGananciaUltimaCampana())).concat(" acumulado de ganancia."), EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)) );
            }
            add( new mkpyLabelField("2) Seguimiento a pedidos de la próxima campaña.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(2), Estilos.getBGInterlinea(2)) );
            add( new mkpyLabelField("3) Explícale el plan de superación.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(3), Estilos.getBGInterlinea(3)) );
            add( new mkpyLabelField("4) Explícale las ganancias de catálogos y revista Gana!.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(4), Estilos.getBGInterlinea(4)) );
            add( new mkpyLabelField("5) Revisa los concursos.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(5), Estilos.getBGInterlinea(5)) );
            add(hypPuntaje);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(5));

            add( new mkpyLabelField("6) Invítala a la conferencia Triunfadoras.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(6), Estilos.getBGInterlinea(6)) );
            if ( consultora.getMontoMeta() > 0 ) { // TIENE META
                if ( consultora.getGananciaUltimaCampana() >= consultora.getMontoMeta() ) { // CUMPLIO META
                	//
                } else {
                    add( new mkpyLabelField("7) Pídele 3 referidos.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(7), Estilos.getBGInterlinea(7)) );
                }
            }
            editAnotacion = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/anotaciones.png"), consultora.getAnotaciones(), 125, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Estilos.getColorInterlinea(7), Estilos.getBGInterlinea(7));
            add(editAnotacion);
            add(hypAnotaciones);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(7));

        } else {
        	add(hypCDR);
            if ( consultora.getDatosDupla().equals(" ") ) { 
                editDupla = new mkpyLabelEditField("Dupla CyZone: ", "", 100, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Color.BLACK, Color.WHITE);
                add(editDupla);
            } else { 
                add( new mkpyLabelLabelField("Dupla CyZone: ", consultora.getDatosDupla(), LabelField.NON_FOCUSABLE | LabelField.FIELD_LEFT | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, true));
            }

            add( new mkpyLabelLabelField("Estado: ", estados.getDescripcionByCodigo(consultora.getEstadoConsultora()), LabelField.NON_FOCUSABLE | LabelField.FIELD_LEFT | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, false));
            add( new mkpyLabelLabelField("Nivel: ", niveles.getDescripcionByCodigo(consultora.getIdNivel()), LabelField.NON_FOCUSABLE | LabelField.FIELD_LEFT | LabelField.ELLIPSIS, Color.BLACK, Color.WHITE, false));
            add(new SeparatorField());

            add(new mkpyLabelField("1) Revisa sus últimas ventas y ganancias.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(1), Estilos.getBGInterlinea(1)));
            add(hypVenta);
            hypVenta.setBackColor(Estilos.getBGInterlinea(1));
            add(new mkpyLabelField("2) Revisa y guía de acuerdo a su Venta por Marca.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(2), Estilos.getBGInterlinea(2)));
            add(hypVentaxMarca);
            hypVentaxMarca.setBackColor(Estilos.getBGInterlinea(2));
            add(new mkpyLabelField("3) Explícale los concursos vigentes.", EditField.NON_FOCUSABLE | EditField.USE_ALL_WIDTH, Estilos.getColorInterlinea(3), Estilos.getBGInterlinea(3)));
            add(hypPuntaje);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(3));
            editAnotacion = new mkpyImageEditField(Bitmap.getBitmapResource("img/consultora/anotaciones.png"), consultora.getAnotaciones(), 125, EditField.NO_NEWLINE | EditField.FIELD_LEFT, Estilos.getColorInterlinea(4), Estilos.getBGInterlinea(4));
            add(editAnotacion);
            add(hypAnotaciones);
            hypPuntaje.setBackColor(Estilos.getBGInterlinea(4));
        }

        // 0 == nuevas, 1 = buscar, 2 = generar lista y facturacion
        if ( origen == 0 ) {
    		switch( pantalla ) {
    		case 1:
    	        Sistema.addEstadistica(Cadenas.V1_DATOSCONSULTORA);
    			break;
    		case 2:
    	        Sistema.addEstadistica(Cadenas.V2_DATOSCONSULTORA);
    			break;
    		case 3:
    	        Sistema.addEstadistica(Cadenas.V3_DATOSCONSULTORA);
    			break;
    		case 4:
    	        Sistema.addEstadistica(Cadenas.V4_DATOSCONSULTORA);
    			break;
    		case 5:
    	        //Sistema.addEstadistica(Cadenas.V5_DATOSCONSULTORA;
    			break;
    		}
        } else if( origen == 1 ) {
	        Sistema.addEstadistica(Cadenas.BC_DATOSCONSULTORA);
        } else if ( origen == 2 ) {
	        Sistema.addEstadistica(Cadenas.GL_DATOSCONSULTORA);
        } 
    }

	public void fieldChanged(Field field, int context) {
		if ( chTipoMeta != null ) {
	        if ( field == chTipoMeta ) {
	        	if ( context == 2 ) {
		        	if ( chTipoMeta.getSelectedIndex() == 0 ) {
			            lblRangoMeta.setValue("");
		        	} else {
			        	Meta meta = (Meta) metas.getObjetos().elementAt(chTipoMeta.getSelectedIndex() - 1);    			        	
			            lblRangoMeta.setValue("MIN: " + Cadenas.getMoneda() + meta.getMinimo() + " MAX: " + Cadenas.getMoneda() + meta.getMaximo());
		        	}
	        	} else {
		        	if ( chTipoMeta.getSelectedIndex() > 0 ) {
			        	Meta meta = (Meta) metas.getObjetos().elementAt(chTipoMeta.getSelectedIndex() - 1);    			        	
			            lblRangoMeta.setValue("MIN: " + Cadenas.getMoneda() + meta.getMinimo() + " MAX: " + Cadenas.getMoneda() + meta.getMaximo());
		        	}
	        	}
	        }
		}
	}
    
	protected boolean navigationClick(int status, int time) {

		Field field = this.getFieldWithFocus();
		if(field == hypVenta) {
			consultaVentas();
			return true;
		}
		if(field == hypVentaxMarca) {
			consultaVentasxMarca();
			return true;
		}
		if(field == hypPuntaje) {
			consultaPuntaje();
			return true;
		}
		if(field == hypCDR) {
			consultaPosVenta();
			return true;
		}
		if(field == hypAnotaciones) {
			verAnotaciones();
			return true;
		}
		
		return super.navigationClick(status, time);
	}
	
    protected boolean onSavePrompt() {
    	boolean modifica = false, modMeta = false, modAnotacion = false;
    	modifica = changeData(0, 0);
    	modMeta = changeData(1, 0);
    	modAnotacion = changeData(2, 0);
    	if ( modifica || modMeta || modAnotacion ) {
    		if ( Dialog.ask(Dialog.D_YES_NO, "Descartar los cambios?") == Dialog.YES ) 
    			return true;
    		else
    			return false;
    	} else {
	       return true;
    	}
    }    

    MenuItem actualizar = new MenuItem("Actualizar datos", 110, 10) {
        public void run() {
        	updateConsultora(0);
        }
    };

    MenuItem miVenta = new MenuItem(verVenta, 110, 10) {
        public void run() {
        	consultaVentas();      
        }

    };
    MenuItem miVentaMarca = new MenuItem(verVentaxMarca, 110, 10) {
        public void run() {
        	consultaVentasxMarca();
        }
    };

    MenuItem miDetallePuntaje = new MenuItem(verPuntaje, 110, 10) {
        public void run() {
        	consultaPuntaje();
        }
    };
    
    MenuItem miPosVenta = new MenuItem(verPosVenta, 110, 10) {
        public void run() {
        	consultaPosVenta();
        }

    };

    MenuItem misAnotaciones = new MenuItem(verAnotaciones, 110, 10) {
        public void run() {
        	verAnotaciones();
        }

    };

    private boolean changeData(int seccion, int origen) {
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
	        	metaCompleta = true;
	    		String idMeta;
	    		String montoMeta;
	    		String descMeta;
	    		if ( chTipoMeta != null ) {
		    		if(chTipoMeta.getSelectedIndex() == 0){
		    			idMeta = "0";	
		    		}else{
		    			Meta meta = (Meta) metas.getObjetos().elementAt(chTipoMeta.getSelectedIndex() - 1);
		    			idMeta = meta.getId();
		    		}
	    		} else {
	    			idMeta = consultora.getIdMeta();
	    		}
	    		if ( editMontoMeta != null ) {
		    		if(editMontoMeta.getText().trim().equals("")){
		    			montoMeta = "0.00";
		    		}else{
		    			montoMeta = editMontoMeta.getText();
		    		}
	    		} else {
	    			montoMeta = String.valueOf(consultora.getMontoMeta());
	    		}
	    		if ( editMeta == null ) {
	    			descMeta = consultora.getDescripcionMeta();
	    		} else {
	    			descMeta = editMeta.getText();
	    		}

	        	if ( consultora.getIdMeta().equals(idMeta) && 
	        			consultora.getDescripcionMeta().equals(descMeta) && 
	        			consultora.getMontoMeta() == Double.parseDouble(montoMeta) ) {
	        		result = false;
	        	} else {
	        		if ( chTipoMeta.getSelectedIndex() == 0 || Double.parseDouble(montoMeta) == 0 || descMeta.trim().equals("") ) {
	        			if ( origen == 1 ) {
		        			Dialog.inform("Debe ingresar el tipo, descripcion y el monto de la meta");
	        			}
	        			metaCompleta = false;
		        		result = false;
	        		} else {
	        			//INICIO: ADD v1.4
	        			double montoMetaTmp = Double.parseDouble(montoMeta); 
		    			Meta meta = (Meta) metas.getObjetos().elementAt(chTipoMeta.getSelectedIndex() - 1);
		    			if ( montoMetaTmp >= meta.getMinimo() && montoMetaTmp <= meta.getMaximo() ) {
			        		result = true;
		    			} else {
		        			if ( origen == 1 ) {
			        			Dialog.inform("Ingrese monto de meta: MIN: " + Cadenas.getMoneda() + meta.getMinimo() + " MAX: " + Cadenas.getMoneda() + meta.getMaximo());
		        			}
		        			metaCompleta = false;
			        		result = false;
		    			}
	        			//FIN: ADD v1.4
	        		}
	        	}
	        	break;
	    	case 2:
	        	if ( consultora.getAnotaciones().equals(editAnotacion.getText()) ) {
	        		result = false;
	        	} else {
	        		result = true;
	        	}
	        	break;
    	}
    	return result;
    }
    
    private void updateConsultora(int desde) {
		boolean preguntar = true;
    	boolean modifica = false, modMeta = false, modAnotacion = false;
   
    	modifica = changeData(0, 1); 	
    	modMeta = changeData(1, 1);
    	modAnotacion = changeData(2, 1);
	    	
    	if ( modifica || modMeta || modAnotacion ) {
    		if ( desde == 0 ) {
    			if ( metaCompleta ) {
        			if ( Dialog.ask(Dialog.D_YES_NO, "¿Está seguro de actualizar los datos?") == Dialog.YES ) {
        				preguntar = true;
        			} else {
        				preguntar = false;
        			}
    			} else {
    				preguntar = false;    				
    			}
    		} else {
    			if ( metaCompleta ) {
    				preguntar = true;
    			} else {
    				preguntar = false;    				
    			}
    		}
    		if ( preguntar  ) {
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

		    	if ( modMeta ) {
		    		if(chTipoMeta.getSelectedIndex() > 0){			    
			        	Meta meta = (Meta) metas.getObjetos().elementAt(chTipoMeta.getSelectedIndex() - 1);    			        	
			    		consultora.setIdMeta(meta.getId());
		    		}else{
		    			consultora.setIdMeta("");
		    		}
		    		consultora.setDescripcionMeta(editMeta.getText());
		    		String montoMeta;
		    		
		    		if(editMontoMeta.getText().trim().equals("")){
		    			montoMeta = "0.00";
		    		}	
		    		else{
		    			montoMeta = editMontoMeta.getText();
		    		}	
		   	    	consultora.setMontoMeta(Double.parseDouble(montoMeta));
		    		resultado = resultado && consultoras.sendUpdate(consultora, 1);
		    	}        
		    	if ( modAnotacion ) {
		    		consultora.setAnotaciones(editAnotacion.getText());
		    		resultado = resultado && consultoras.sendUpdate(consultora, 2);
		    	}        
		    	if ( resultado ) {
		    		if ( desde == 0 ) {
			            Dialog.inform("Los datos se actualizaron con éxito");
		    		}
		    	} else {
		            Dialog.inform("Los datos NO se actualizaron con éxito");
		    	}
		    	if (consultora.getMontoMeta() > 0 ) {
		    		try{
		            	chTipoMeta.setEditable(false);
		            	editMeta.setEditable(false);
		            	editMontoMeta.setEditable(false);
		    		}catch(Exception ex){
		    			//Dialog.inform(ex.getMessage());
		    		}
		    	}
		    	progress.close();
    		}
    	} else {
    		if ( desde == 0 ) {
    			if ( metaCompleta ) {
            		Dialog.alert("No se han modificado los datos de la consultora.");
    			}
    		}
    	}
    }

	private void verAnotaciones() {
		if ( consultora.getHistorialAnotaciones().size() == 0 ) {
			Dialog.inform("La consultora no tiene anotaciones");
		} else {
			Estilos.pushScreen(new HistorialAnotaciones(origen, consultora));
		}
	}
    
	private void consultaVentas() {
		if ( consultora.getPedidos().size() == 0 ) {
			Dialog.inform("La consultora no tiene ventas");
		} else {
			Estilos.pushScreen(new ConsultaVentas(origen, consultora));
		}
	}
	
	private void consultaVentasxMarca() {
		if ( consultora.getPedidosxMarca().size() == 0 ) {
			Dialog.inform("La consultora no tiene ventas");
		} else {
			Estilos.pushScreen(new ConsultaVentasxMarca(origen, consultora));
		}
	}
	
	private void consultaPuntaje() {
		if ( consultora.getConcursos().size() == 0 ) {
			Dialog.inform("La consultora no tiene puntajes");
		} else {
			Estilos.pushScreen(new ConsultaPuntaje(origen, consultora));
		}
	}

	private void consultaPosVenta() {
		if ( consultora.getPostVentas().size() == 0 ) {
			Dialog.inform("La consultora no tiene CDR");
		} else {
			Estilos.pushScreen(new PosVentaDetalle(origen, consultora));
		}
	}

    MenuItem registrar = new MenuItem("Registrar visita", 110, 10) {
        public void run() {
            registrar();
        }
    };

    private void registrar() {
		if ( Dialog.ask(Dialog.D_YES_NO, "¿Está seguro de registrar la visita?") == Dialog.YES ){
			updateConsultora(1);
	    	if ( ! metaCompleta ) {
	    		return;
	    	}
	    	progress.open();
	    	progress.setTitle("Registrando la vista");
	        consultoras = new ConsultoraDB();
	        if ( consultoras.sendUpdate(consultora, 4) ) {
	            Dialog.inform("La visita se registró con éxito");
	        	this.close();
	        	consultaSeccion.close();
	        	menuSeccion.close();
	        	menuContactos.close();
	        	Estilos.pushScreen(new MenuContactos());
	        } else {
	            Dialog.inform("La visita NO se registró con éxito");
	        }
	        progress.close();
		}
    }
            
    MenuItem visitaNoExitosa = new MenuItem("Visita no exitosa", 110, 10) {
        public void run() {
            visitaNoExito();
        }
    };
  
    private void visitaNoExito() {
    	Estilos.pushScreen(new NoExitoConsultora(origen, consultora, menuContactos, menuSeccion, consultaSeccion, this));
    }
    
    public String[] mostrarMetas() {
        metas = new MetaDB();
        int n = metas.getObjetos().size();
        String[] metasDesc = new String[n + 1];
        metasDesc[0] = "Seleccione";
        for(int i = 0; i < n; i++) {
            Meta meta = (Meta) metas.getObjetos().elementAt(i);
            metasDesc[i + 1] = meta.getDescripcion();
        }
        return metasDesc;
    }     
    
    public int getIndexIdMeta(String idMeta) {
        metas = new MetaDB();
        int n = metas.getObjetos().size();
        for(int i = 0; i < n; i++) {
            Meta meta = (Meta) metas.getObjetos().elementAt(i);
            if ( meta.getId().equals(idMeta) ) {
            	return i + 1;
            }
        }
        return 0;
    }     

    private void llamar(String nro) {
        Invoke.invokeApplication( Invoke.APP_TYPE_PHONE, new PhoneArguments( PhoneArguments.ARG_CALL, nro ) );
    }

    private void escribir(String correo) {
        Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES , new MessageArguments(MessageArguments.ARG_NEW, correo, "", ""));
    }
    
    public void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		Field field = this.getFieldWithFocus();
		// add these items to the full menu
		if ( field == editTelef1 ) {
			if ( !editTelef1.getText().trim().equals("") ) {
		        menu.add(new MenuItem("Llamar ".concat(consultora.getTelefono1()), 110, 10) {
	        			public void run() {
	    				llamar(consultora.getTelefono1());
	    			}
	    		});        		
		        menu.add(MenuItem.separator(110));
			}
		}
		if ( field == editTelef2 ) {
			if ( !editTelef2.getText().trim().equals("") ) {
		        menu.add(new MenuItem("Llamar " + consultora.getTelefono2(), 110, 10) {
        			public void run() {
    				llamar(consultora.getTelefono2());
	    			}
	    		});        		
		        menu.add(MenuItem.separator(110));
			}
		}
		if ( field == editTelef3 ) {
			if ( !editTelef3.getText().trim().equals("") ) {
		        menu.add(new MenuItem("Llamar ".concat(consultora.getTelefono3()), 110, 10) {
        			public void run() {
    				llamar(consultora.getTelefono3());
	    			}
	    		});        		
		        menu.add(MenuItem.separator(110));
			}
		}
		if ( field == editEmail ) {
			if ( !editEmail.getText().trim().equals("") ) {
		        menu.add(new MenuItem("Escribir ".concat(consultora.getEmail()), 110, 10) {
        			public void run() {
    				escribir(consultora.getEmail());
	    			}
	    		});        		
		        menu.add(MenuItem.separator(110));
			}
		}
		
		if( !consultora.getVisitada().equals("VE") && !isEstablecidas){
	        //if(meGlobales.getPerfil() != MetodosGlobales.perfilDV()) //si no es DV
	        menu.add(registrar);
		}
        menu.add(actualizar);
		if( !consultora.getVisitada().equals("VE") && !isEstablecidas){
	        menu.add(visitaNoExitosa);
		}
        menu.add(MenuItem.separator(110));
        menu.add(miVenta);
        menu.add(miVentaMarca);
        menu.add(miDetallePuntaje);
        if(pantalla != 1 ){
            menu.add(miPosVenta);
        }
        menu.add(misAnotaciones);
	}
}
