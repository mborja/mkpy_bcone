package com.belcorp.entidades;

import java.util.Vector;

public class Item {
	private boolean colapsado;
	private boolean cabecera;
	private String descripcion;
	private String valor;
	private Vector items;
	private int indexPadre;
	private String ubicacion;
	
	public Item(boolean cabecera, String descripcion, String valor, boolean colapsado, int index) {
		this.cabecera = cabecera;
		this.descripcion = descripcion;
		this.valor = valor;
		this.colapsado = colapsado;
		this.indexPadre = index;
	}
	
	public Item(boolean cabecera, String descripcion, String valor, String ubicacion, boolean colapsado, int index) {
		this.cabecera = cabecera;
		this.descripcion = descripcion;
		this.valor = valor;
		this.setUbicacion(ubicacion);
		this.colapsado = colapsado;
		this.indexPadre = index;
	}

	public int getIndexPadre() {
		return indexPadre;
	}

	public void setIndexPadre(int indexPadre) {
		this.indexPadre = indexPadre;
	}

	public Vector getItems() {
		if ( items == null ) {
			items = new Vector();
		}
		return items;
	}

	public void setItems(Vector items) {
		this.items = items;
	}

	public boolean isColapsado() {
		return colapsado;
	}

	public void setColapsado(boolean colapsado) {
		this.colapsado = colapsado;
	}

	public boolean isCabecera() {
		return cabecera;
	}

	public void setCabecera(boolean cabecera) {
		this.cabecera = cabecera;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getUbicacion() {
		return ubicacion;
	}
}
