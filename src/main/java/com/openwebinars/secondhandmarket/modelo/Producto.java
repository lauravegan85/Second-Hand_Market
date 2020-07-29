package com.openwebinars.secondhandmarket.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity //Anotación para las entidades, en este caso, la entidad Productos.
public class Producto {

//ATRIBUTOS:
	
	@Id @GeneratedValue
	private long id;
	
	private String nombre;
	
	private float precio;
	
	private String imagen;
	
	
//CAMPOS DE ASOCIACIÓN:
	//(todo producto tendrá propietario y compra)
	
	@ManyToOne // Uno o muchos productos por usuario.
	private Usuario propietario; //usuario que vende el producto
	
	@ManyToOne     //Uno o Muchos productos por compra.   
	private Compra compra;  
	
//CONSTRUCTORES:
	
	public Producto() {} //vacío

	public Producto(String nombre, float precio, String imagen, Usuario propietario) { //todos los campos,
									//MENOS id (se autogenera) y menos Compra (elproducto se instancia o
	this.nombre = nombre;			//inicializa sin ser comprado;en todo caso se comprará después,o no).
	this.precio = precio;
	this.imagen = imagen;
	this.propietario = propietario;
}

	
//MÉTODOS SETTERS Y GETTERS.
	//(incluyen todos los campos)
			
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getPrecio() {
		return precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Usuario getPropietario() {  //Propietario del producto,es decir, quien lo pone a la venta
		return propietario;
	}

	public void setPropietario(Usuario propietario) {
		this.propietario = propietario;
	}

	public Compra getCompra() {  //Con este get puedes obtener los datos de la compra (incluido el
		return compra;					//propietario de la compra)
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	
	

// HASHCODE() AND EQUALS:
	//(todos los campos)
			
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((compra == null) ? 0 : compra.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((imagen == null) ? 0 : imagen.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + Float.floatToIntBits(precio);
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
		Producto other = (Producto) obj;
		if (compra == null) {
			if (other.compra != null)
				return false;
		} else if (!compra.equals(other.compra))
			return false;
		if (id != other.id)
			return false;
		if (imagen == null) {
			if (other.imagen != null)
				return false;
		} else if (!imagen.equals(other.imagen))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (Float.floatToIntBits(precio) != Float.floatToIntBits(other.precio))
			return false;
		if (propietario == null) {
			if (other.propietario != null)
				return false;
		} else if (!propietario.equals(other.propietario))
			return false;
		return true;
	}

	
	

//MÉTODO TOSTRING()
			
	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", imagen=" + imagen
				+ ", propietario=" + propietario + ", compra=" + compra + "]";
	}
	

	
	
	
	
	
	
}
