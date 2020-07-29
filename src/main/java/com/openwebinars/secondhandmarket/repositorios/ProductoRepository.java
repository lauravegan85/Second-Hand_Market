package com.openwebinars.secondhandmarket.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openwebinars.secondhandmarket.modelo.Compra;
import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.modelo.Usuario;


public interface ProductoRepository extends JpaRepository<Producto,Long>{ //<TipoDatoEntidad,
																			// FormaObjetoDelTipoDatoDeLaClavePrimariaId>

//Consultas derivadas del nombre del método:
	
	List<Producto> findByPropietario(Usuario propietario); //BUSCAR TODOS LOS PRODUCTOS DE UN USUARIO.
	
	List<Producto> findByCompra(Compra compra); //BUSCAR TODOS LOS PRODUCTOS DE UNA DETERMINADA COMPRA.
	
	List<Producto> findByCompraIsNull(); //BUSCAR TODOS LOS PRODUCTOS QUE NO HAN SIDO COMPRADOS.
											//es decir, que la compra sea nula=estén todavía en venta.			
	
	List<Producto> findByNombreContainsIgnoreCaseAndCompraIsNull(String nombre); //BUSCAR TODOS LOS 
			//PRODUCTOS DONDE EL NOMBRE CONTENGA LA CADENA DE CARACTERES PASADA POR PARÁMETRO, Y
			//DONDE LA COMPRA SEA NULA (No hayan sido comprados).
		
	List<Producto> findByNombreContainsIgnoreCaseAndPropietario(String nombre,Usuario propietario);
		// BUSCAR TODOS LOS PRODUCTOS DONDE EL NOMBRE CONTENGA LA CADENA DE CARACTERES PASADA POR 
		//PARÁMETRO, Y QUE ADEMÁS TENGAN COMO PROPIETARIO AL QUE PASEMOS POR PARÁMETRO. 
}
