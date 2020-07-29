package com.openwebinars.secondhandmarket.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openwebinars.secondhandmarket.modelo.Compra;
import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.repositorios.CompraRepository;

@Service
public class CompraServicio {
	
	@Autowired     //Inyección de dependencia de su repositorio.
	CompraRepository repositorio;
	
	@Autowired //Cableamos el servicio de Producto (porque una compra se compone de distintos productos)
	ProductoServicio productoServicio;
	
//MÉTODOS CRUD:
	
	public Compra insertar(Compra c,Usuario u) {//INSERTAR UNA COMPRA CONSIDERANDO SU PROPIETARIO
		c.setPropietario(u);//Asigna el propietario de la compra
		return repositorio.save(c); //inserta la compra
	}
	
	public Compra insertar(Compra c) {//INSERTAR UNA COMPRA (segunda forma posible de hacerlo)
		return repositorio.save(c);
	}
	
	
	public Producto addProducto(Producto p,Compra c) {//AÑADIR UN PRODUCTO A UNA COMPRA
	p.setCompra(c);
	return productoServicio.editar(p);
	}	/* La compra NO conoce los productos que tiene dentro. Como cada producto en esta app
	*sólo puede ser comprado una vez, es el producto el que sabe a la compra a la cuál pertenece.
	*Con lo cual, al añadir un producto a una compra lo que estamos haciendo es editar el producto.
	*/
	
	public Compra buscarPorId(long id) {//BUSCAR COMPRA POR ID
	return repositorio.findById(id).orElse(null);  /*Si no ponemos orElse(null) entonces da el error: cannot convert
										*from <Optional>Compra to Compra.
										*/
	}

	public List<Compra> todas(){//BUSCAR TODAS LAS COMPRAS
	return repositorio.findAll();
	}
	
	public List<Compra> porPropietario(Usuario u){ //BUSCAR COMPRA DE UN PROPIETARIO.
		return repositorio.findByPropietario(u);
	}
	
	

}
