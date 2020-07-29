package com.openwebinars.secondhandmarket.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openwebinars.secondhandmarket.modelo.Compra;
import com.openwebinars.secondhandmarket.modelo.Usuario;

public interface CompraRepository extends JpaRepository<Compra, Long> {//<TipoDatoEntidad,
																//FormaObjetoDelTipoDatoDeLaClavePrimariaId>

//Métodos de consulta derivados del nombre del método:
	
	List<Compra> findByPropietario(Usuario propietario); //BUSCA TODAS LAS COMPRAS DE UN USUARIO.
	// Con findByPropietario usamos el atributo Propietario, que es de tipo Usuario.
	
	
	
	
	
}
