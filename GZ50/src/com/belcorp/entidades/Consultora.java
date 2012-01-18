package com.belcorp.entidades;

import java.util.Vector;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.Persistable;

public final class Consultora implements Persistable {
        private String id;
        private String codigo;
        private String nombre;
        private String saldo;
        private String idSeccion;
        private String direccion;
        private String docIdentidad;
        private String telefono1;
        private String telefono2;
        private String telefono3;
        private String email;
        private String campanaIngreso;
        private String autorizadoPasarPedido;
        private String idNivel;
        private String clasificacionMetodologica;//1,2,3,4,5 pantallas
        private String pasoPedido;
        private String montoUltimoPedido;
        private String asisteCompartamos;
        private double gananciaUltimaCampana;
        private String idMeta;
        private double montoMeta;
        private String descripcionMeta;
        private String estadoConsultora;
        private String datosDupla;
        private String anotaciones;
        
        private Vector concursos; //Concurso puntaje 
        private Vector pedidos;//ventas
        private Vector pedidosxMarca;//ventas por marcas
        private Vector postVentas;
        private String asistenciaTalleres;
        private String visitada; // vacio=no trabajada, VNE=visitada no enviar, VE=visitada enviada, NVNE=no visita no enviada, NVE=no visita enviada 
        private Vector motivoNoVista; // lista de id de motivos de la no visita, NULL si esta visitada
        private String modificada; // vacio = no modificada, ME=modificada enviada, MNE=modificada no enviada
        private String anotada; // vacio=sin anotaciones, AE=anotacion enviada, ANE=anotacion no enviada
        private String conMeta; // vacio=sin cambios en meta, ME=meta enviada, MNE=meta no enviada
        private String idMotivosNoVisita;
        private String motivosNoVisita;
        private String operacion;
        
        private Vector historialAnotaciones; // Lista con las anotacinoes
                
        public Vector getHistorialAnotaciones() {
        	if ( historialAnotaciones == null ) {
        		historialAnotaciones = new Vector();
        	}
			return historialAnotaciones;
		}
		public void setHistorialAnotaciones(Vector historialAnotaciones) {
			this.historialAnotaciones = historialAnotaciones;
		}
		public String getOperacion() {
			return operacion;
		}
		public void setOperacion(String operacion) {
			this.operacion = operacion;
		}
		public String getAsistenciaTalleres() {
			return asistenciaTalleres;
		}
		public void setAsistenciaTalleres(String asistenciaTalleres) {
			this.asistenciaTalleres = asistenciaTalleres;
		}
		public String getEstadoConsultora() {
			return estadoConsultora;
		}
		public void setEstadoConsultora(String estadoConsultora) {
			this.estadoConsultora = estadoConsultora;
		}
		public String getIdMotivosNoVisita() {
			return idMotivosNoVisita;
		}
		public void setIdMotivosNoVisita(String idMotivosNoVisita) {
			this.idMotivosNoVisita = idMotivosNoVisita;
		}
        public String getMotivosNoVisita() {
			return motivosNoVisita;
		}
		public void setMotivosNoVisita(String motivosNoVisita) {
			this.motivosNoVisita = motivosNoVisita;
		}
		public String getConMeta() {
			return conMeta;
		}
		public void setConMeta(String conMeta) {
			this.conMeta = conMeta;
		}
		public String getIdNivel() {
                return idNivel;
        }
        public void setIdNivel(String idNivel) {
                this.idNivel = idNivel;
        }
        public String getVisitada() {
                return visitada;
        }
        public void setVisitada(String visitada) {
                this.visitada = visitada;
        }
        public Vector getMotivoNoVista() {
                return motivoNoVista;
        }
        public void setMotivoNoVista(Vector motivoNoVista) {
                this.motivoNoVista = motivoNoVista;
        }
        public String getModificada() {
                return modificada;
        }
        public void setModificada(String modificada) {
                this.modificada = modificada;
        }
        public String getAnotada() {
                return anotada;
        }
        public void setAnotada(String anotada) {
                this.anotada = anotada;
        }
        public String getId() {
                return id;
        }
        public void setId(String id) {
                this.id = id;
        }
        public String getCodigo() {
                return codigo;
        }
        public void setCodigo(String codigo) {
                this.codigo = codigo;
        }
        public String getNombre() {
                return nombre;
        }
        public void setNombre(String nombre) {
                this.nombre = nombre;
        }
        public String getSaldo() {
                return saldo;
        }
        public void setSaldo(String saldo) {
                this.saldo = saldo;
        }
        public String getIdSeccion() {
                return idSeccion;
        }
        public void setIdSeccion(String idSeccion) {
                this.idSeccion = idSeccion;
        }
        public String getDireccion() {
                return direccion;
        }
        public void setDireccion(String direccion) {
                this.direccion = direccion;
        }
        public String getDocIdentidad() {
                return docIdentidad;
        }
        public void setDocIdentidad(String docIdentidad) {
                this.docIdentidad = docIdentidad;
        }
        public String getTelefono1() {
                return telefono1;
        }
        public void setTelefono1(String telefono1) {
                this.telefono1 = telefono1;
        }
        public String getTelefono2() {
                return telefono2;
        }
        public void setTelefono2(String telefono2) {
                this.telefono2 = telefono2;
        }
        public String getTelefono3() {
                return telefono3;
        }
        public void setTelefono3(String telefono3) {
                this.telefono3 = telefono3;
        }
        public String getEmail() {
                return email;
        }
        public void setEmail(String email) {
                this.email = email;
        }
        public String getCampanaIngreso() {
        	return "C" + campanaIngreso.substring(4) + "/" + campanaIngreso.substring(0, 4);
        }
        public void setCampanaIngreso(String campanaIngreso) {
                this.campanaIngreso = campanaIngreso;
        }
        public String getAutorizadoPasarPedido() {
        	if ( autorizadoPasarPedido.equals("1") ) {
        		return "Si";
        	} else {
        		return "No";
        	}
        }
        public void setAutorizadoPasarPedido(String autorizadoPasarPedido) {
                this.autorizadoPasarPedido = autorizadoPasarPedido;
        }
        public String getClasificacionMetodologica() {
                return clasificacionMetodologica;
        }
        public void setClasificacionMetodologica(String clasificacionMetodologica) {
                this.clasificacionMetodologica = clasificacionMetodologica;
        }
        public String getPasoPedido() {
        	if ( pasoPedido.equals("1") ) {
        		return "Sí";
        	} else {
        		return "No";
        	}
        }
        public void setPasoPedido(String pasoPedido) {
                this.pasoPedido = pasoPedido;
        }
        public String getMontoUltimoPedido() {
                return montoUltimoPedido;
        }
        public void setMontoUltimoPedido(String montoUltimoPedido) {
                this.montoUltimoPedido = montoUltimoPedido;
        }
        public String getAsisteCompartamos() {
        	if ( asisteCompartamos.equals("1") ) {
        		return "Si";
        	} else {
        		return "No";
        	}
        }
        public void setAsisteCompartamos(String asisteCompartamos) {
                this.asisteCompartamos = asisteCompartamos;
        }
        public double getGananciaUltimaCampana() {
                return gananciaUltimaCampana;
        }
        public void setGananciaUltimaCampana(double gananciaUltimaCampana) {
                this.gananciaUltimaCampana = gananciaUltimaCampana;
        }
        public String getIdMeta() {
                return idMeta;
        }
        public void setIdMeta(String idMeta) {
                this.idMeta = idMeta;
        }
        public double getMontoMeta() {
                return montoMeta;
        }
        public void setMontoMeta(double montoMeta) {
                this.montoMeta = montoMeta;
        }
        public String getDescripcionMeta() {
                return descripcionMeta;
        }
        public void setDescripcionMeta(String descripcionMeta) {
                this.descripcionMeta = descripcionMeta;
        }
        public String getDatosDupla() {
                return datosDupla;
        }
        public void setDatosDupla(String datosDupla) {
                this.datosDupla = datosDupla;
        }
        public String getAnotaciones() {
                return anotaciones;
        }
        public void setAnotaciones(String anotaciones) {
                this.anotaciones = anotaciones;
        }
        public Vector getConcursos() {
            if ( concursos == null )
                concursos = new Vector();
            return concursos;
        }
        public void setConcursos(Vector concursos) {
                this.concursos = concursos;
        }
        public Vector getPedidos() {
            if ( pedidos == null ) 
                pedidos = new Vector();
            return pedidos;
        }
        
        public void sortPedidos() {
            int n, i, j;
            if (pedidos == null) {
            	n = 0;
            } else {
            	n = pedidos.size();
            }
            for ( i = 0; i < n; i++ ) {
                for ( j = 0; j < n; j++ ) {
                	PedidosVenta pedidoI = (PedidosVenta) pedidos.elementAt(i); 
                	PedidosVenta pedidoJ = (PedidosVenta) pedidos.elementAt(j);
                	try {
                    	if ( Long.parseLong( pedidoI.getCampana() ) > Long.parseLong( pedidoJ.getCampana() ) ) { // I es menor que J
                    		pedidos.setElementAt(pedidoI, j);
                    		pedidos.setElementAt(pedidoJ, i);
                    	}
                	} catch(Exception e) {
                		Dialog.inform("" + e.getMessage());
                	}
                }
            }
	        //this.pedidos = pedidos;
        }

        public void setPedidos(Vector pes) {
	        this.pedidos = pes;
        	this.sortPedidos();
        }
        
        public Vector getPedidosxMarca() {
            if ( pedidosxMarca == null ) 
                pedidosxMarca = new Vector();
            return pedidosxMarca;
        }
        public void sortPedidosxMarca() {
            int n, i, j;
            if (pedidosxMarca == null) {
            	n = 0;
            } else {
                n = pedidosxMarca.size();
            }
            for ( i = 0; i < n; i++ ) {
                for ( j = 0; j < n; j++ ) {
                	PedidosxMarca pedidoI = (PedidosxMarca) pedidosxMarca.elementAt(i); 
                	PedidosxMarca pedidoJ = (PedidosxMarca) pedidosxMarca.elementAt(j);
                	try {
                    	if ( Long.parseLong( pedidoI.getCampana() ) > Long.parseLong( pedidoJ.getCampana() ) ) { // I es menor que J
                    		pedidosxMarca.setElementAt(pedidoI, j);
                    		pedidosxMarca.setElementAt(pedidoJ, i);
                    	}
                	} catch(Exception e) {
                		
                	}
                }
            }
            //this.pedidosxMarca = pesxMarca;
        }

        public void setPedidosxMarca(Vector pesxMarca) {
            this.pedidosxMarca = pesxMarca;
            this.sortPedidosxMarca();
        }
        public Vector getPostVentas() {
            if ( postVentas == null ) 
                postVentas = new Vector();
            return postVentas;
        }
        public void setPostVentas(Vector postVentas) {
                this.postVentas = postVentas;
        }
        
}
