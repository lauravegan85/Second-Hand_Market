package com.openwebinars.secondhandmarket.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openwebinars.secondhandmarket.modelo.Compra;
import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.repositorios.ProductoRepository;
import com.openwebinars.secondhandmarket.upload.StorageService;

@Service  //Anotación para la clase de servicio.
public class ProductoServicio {

	@Autowired
	ProductoRepository repositorio;  //Inyecta la dependencia del repositorio de producto,para poder acceder a él.

	@Autowired  //Autocablea servicio de almacenamiento para poder usar las fotos de los productos subidas por el usuario.
	StorageService storageService; 

//MÉTODOS CRUD:
	
	public Producto insertar(Producto p) { //INSERTAR PRODUCTO NUEVO AL REPOSITORIO DE PRODUCTOS.
		return repositorio.save(p);    //Para usar Producto p hay que importar modelo.Producto
	}
	
	public void borrar(long id) { //BORRAR PRODUCTO DEL REPOSITORIO EN BASE AL ID
		repositorio.deleteById(id); //aqui tb habria q implementar borrado de su imagen (pdte de hacer)
	}
	
	public void borrar(Producto p) { //BORRAR PRODUCTO DEL REPOSITORIO EN BASE A LA ENTIDAD PRODUCTO.
		if(!p.getImagen().isEmpty())  //Si la imagen no está vacía...
			storageService.delete(p.getImagen());  //...entonces borra tb la imagen del producto.
		repositorio.delete(p); //Para usar Producto p hay que importar modelo.Producto
	}
	
	public Producto editar(Producto p) { //EDITA UN PRODUCTO DEL REPOSITORIO.
		return repositorio.save(p); //Para usar Producto p hay que importar modelo.Producto
	}

	public Producto findById(long id) { //MÉTODO QUE BUSCA UN PRODUCTO DEL REPOSITORIO POR SU ID, Y
		return repositorio.findById(id).orElse(null);  //SI NO LO ENCUENTRA, DEVUELVE NULL.
	}//Este método se llama tb findById, pero podia haberse llamado: public Producto MostrarPorId(long id) {...}
	
	public List<Producto> findAll(){ //MÉTODO QUE BUSCA TODOS LOS PRODUCTOS DEL REPOSITORIO.
		return repositorio.findAll();
	}//Este método se llama findAll() pero podía haberse llamado: public List<Producto> MostrarTodos(){..}
	
	
//IMPLANTAMOS AHORA LOS RECUBRIMIENTOS DE LAS DISTINTAS BÚSQUEDAS:	
	
	public List<Producto> productosDeUnPropietario(Usuario u){//BUSCAR TODOS LOS PRODUCTOS DE UN USUARIO.
		return repositorio.findByPropietario(u); //Para usar Usuario u hay que importar modelo.Usuario 
	}									
	
	public List<Producto> productosDeUnaCompra(Compra c){ //BUSCAR TODOS LOS PRODUCTOS DE UNA DETERMINADA COMPRA.
		return repositorio.findByCompra(c);//Para usar Compra c, hay que importar modelo.Compra
	}
	
	public List<Producto> productosSinVender(){//BUSCAR TODOS LOS PRODUCTOS QUE NO HAN SIDO COMPRADOS.
	return repositorio.findByCompraIsNull();
	}
	
	public List<Producto> buscar(String query){ //BUSCAR TODOS LOS PRODUCTOS DONDE EL NOMBRE CONTENGA LA 
		//CADENA DE CARACTERES PASADA POR PARÁMETRO, Y DONDE LA COMPRA SEA NULA(No hayan sido comprados).
		return repositorio.findByNombreContainsIgnoreCaseAndCompraIsNull(query);
	}
	
	public List<Producto> buscarMisProductos(String query,Usuario u){// BUSCAR TODOS LOS PRODUCTOS 
		//DONDE EL NOMBRE CONTENGA LA CADENA DE CARACTERES PASADA POR PARÁMETRO, Y QUE ADEMÁS TENGAN
		// COMO PROPIETARIO AL QUE PASEMOS POR PARÁMETRO. 
		return repositorio.findByNombreContainsIgnoreCaseAndPropietario(query,u);
	}
	
//IMPLEMENTAMOS UN MÉTODO ADICIONAL: 
	
	public List<Producto> variosPorId(List<Long> ids){ // BÚSQUEDA DE VARIOS PRODUCTOS POR SU ID
	return repositorio.findAllById(ids);
	}
}