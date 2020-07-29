package com.openwebinars.secondhandmarket.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openwebinars.secondhandmarket.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> { //<TipoDatoEntidad,
																//FormaObjetoDelTipoDatoDeLaClavePrimariaId>
	

//Métodos de consulta derivados del nombre del método:
	Usuario findFirstByEmail(String email);
	
	/* Nos devuelve el primer usuario que encuentre según el mail indicado en el buscador.
	 * No obstante los emails deberían ser únicos y por lo tanto deberíamos añadir
	 * una regla de validacion del email en el atributo del modelo, hacer cambios
	 * necesarios, modificar este metodo del repositorio.
	 */
	
}
