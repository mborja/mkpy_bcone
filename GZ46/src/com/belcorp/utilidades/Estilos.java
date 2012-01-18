/*
 * Estilos.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */

package com.belcorp.utilidades;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

public final class Estilos {

        public static int getColorModulo() {
        return 0xFFFFFF;
    }
        
        public static int getBackGround() {
        return Color.WHITE; //0x6973b0;
    }

    public static int getBGModulo() {
        return 0x92278F; //0x6a288a;
    }

    public static int getBGSubModulo() {
        return 0xA963A8; //0x989db9;
    }

    public static int getBGScreen() {
        return Color.WHITE; //0xe2e2e2;
    }
    public static int getFuente() {
        return Color.BLACK;
    }

    public static int getBGInterlinea(int linea) {
        if ( linea % 2 == 0 ) {
            return 0xA963A8; //0xd2bedc;
        } else {
            return 0xD1D3D4; //0xdbdada;
        }
    }

    public static int getColorInterlinea(int linea) {
        if ( linea % 2 == 0 ) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    public static int getBGSelected() {
        return Color.RED;
    }

    public static void pushScreen(MainScreen screen) {
        UiApplication.getUiApplication().pushScreen(screen);   
   }
} 


