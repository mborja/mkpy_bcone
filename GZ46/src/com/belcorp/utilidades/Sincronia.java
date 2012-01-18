package com.belcorp.utilidades;

import com.belcorp.dao.UsuarioDB;
import com.belcorp.entidades.Usuario;

/** 
 * 
 * @author Manuel Borja L. 
 * 22.11.2011
 */ 

public class Sincronia extends Thread{
	
	public Sincronia()
	{	}
	
	public void Exec()
	{
		UsuarioDB usuarios;
    	usuarios = new UsuarioDB();
    	usuarios.existeUsr(false);
		usuarios = null;
	}
    
}
