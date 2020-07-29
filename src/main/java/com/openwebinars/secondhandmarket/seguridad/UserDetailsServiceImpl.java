package com.openwebinars.secondhandmarket.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.repositorios.UsuarioRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	
	@Autowired
	UsuarioRepository repositorio;    //Autocablear el repositorio de usuario para poder usar el método posterior.
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { /*
	Método que permite buscar un usuario por su nombre de usuario y devolver una instancia de UserDetails para
	completar el proceso de autenticación. Para ello es necesario autocablear el repositorio de usuario.
	*/	
		
		
		//Hay que BUSCAR EL USUARIO :
		Usuario usuario=repositorio.findFirstByEmail(username);  //El nombre de usuario será su email.
						//Busca el usuario del username (email) dado,por ese email, y lo almacena en la variable.
		
		//CONSTRUIMOS UNA INSTANCIA DE USERDETAILS a partir del usuario, usando un UserBuilder:
		UserBuilder builder=null;
				
				 /*UserDetails es la interfaz de usuario utilizada por SpringSecurity en toda su arquitectura
				 * de autenticación. Traduce/transforma el usuario a un UserDetails. */
				
			if(usuario != null) { //Si hemos encontrado el usuario.. 
				//Construimos el usuario:
				builder = User.withUsername(username); //Empezamos a construir con el username recibido
				builder.disabled(false); //Que no esté deshabilitado
				builder.password(usuario.getPassword()); //Que el password sea el del usuario registrado
				builder.authorities(new SimpleGrantedAuthority("ROLE_USER")); //Que las authorities que recibe por parámetro sean los de la clase dada por el propio Spring, instanciada en parámetro, así no tenemos que implementar nuestra propia clase Authority.
			}else { //Si NO hemos encontrado el usuario
				throw new UsernameNotFoundException("Usuario no encontrado"); //lanza excepción de usuario no encontrado.
			}
			return builder.build(); //Devuelve el usuario construido.
	}  /* Ahora hay que inyectar el usuario construido dentro de la clase SeguridadConfig y esto se hace autocableando
	   *un userDetailService antes de los metodos de autenticación y autorización. Este userDetailService se va a usar
	   *en el método de autenticación.
	   */

}
