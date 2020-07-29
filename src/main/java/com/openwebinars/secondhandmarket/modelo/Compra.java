package com.openwebinars.secondhandmarket.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity //Anotación para las entidades, en este caso, la entidad Compra.
@EntityListeners(AuditingEntityListener.class)  //Anotación para que funcionen los campos anotados con
														//CreatedDate

public class Compra {

//ATRIBUTOS: 
	
	@Id @GeneratedValue
	private long id;
	
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaCompra;
	
	@ManyToOne //una compra o muchas, pertenecientes a 1 usuario.
	private Usuario propietario; //campo de asociación con el usuario propietario de esta compra
	
//CONSTRUCTORES:
	
	public Compra(){ } //vacío


	public Compra(Usuario propietario) {  // sin los campos id ni fecha, porque son autogenerados.
		this.propietario = propietario;
	}


//GETTERS AND SETTERS:
//con todos los campos, incluido id


public long getId() {
	return id;
}


public void setId(long id) {
	this.id = id;
}


public Date getFechaCompra() {
	return fechaCompra;
}


public void setFechaCompra(Date fechaCompra) {
	this.fechaCompra = fechaCompra;
}

public Usuario getPropietario() {
	return propietario;
}


public void setPropietario(Usuario propietario) {
	this.propietario = propietario;
}



//MÉTODOS HASHCODE() AND EQUALS:

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((fechaCompra == null) ? 0 : fechaCompra.hashCode());
	result = prime * result + (int) (id ^ (id >>> 32));
	result = prime * result + ((propietario == null) ? 0 : propietario.hashCode());
	return result;
}


@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Compra other = (Compra) obj;
	if (fechaCompra == null) {
		if (other.fechaCompra != null)
			return false;
	} else if (!fechaCompra.equals(other.fechaCompra))
		return false;
	if (id != other.id)
		return false;
	if (propietario == null) {
		if (other.propietario != null)
			return false;
	} else if (!propietario.equals(other.propietario))
		return false;
	return true;
}




//MÉTODO TOSTRING():

@Override
public String toString() {
	return "Compra [id=" + id + ", fechaCompra=" + fechaCompra + ", propietario=" + propietario + "]";
}
	
	
	
}
