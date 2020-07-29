package com.openwebinars.secondhandmarket.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SeguridadConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	UserDetailsService userDetailsService; //Autocablea,inyecta el usuario Contruido en  la clase UserDetailsServiceImpl.java para USARLO EN EL MÉTODO DE AUTENTICACIÓN.

	//Sobreescribimos algunos métodos: clic dcho -> source -> Override/Implement Methods
	        //dejamos seleccionados: configure(HttpSecurity) y configure(AuthenticationManagerBuilder)
					//Esto configura la autenticación y la autorización.
	
	
//MÉTODO DE AUTENTICACIÓN:
	
	/*Cada vez que se quiera hacer una autenticación usará el servicio userDetailsService para localizar al
	 * usuario en la bbdd, y filtrará el password con BCrypt. 
	 */
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { //CONFIGURA LA AUTENTICACIÓN haciendo uso del Servicio del Detalle de Usuario
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); //método passwordEncoder() desarrollado abajo
	} 
	
	//Método passwordEncoder de BCrypt que proporciona Spring:
	@Bean 
	public BCryptPasswordEncoder passwordEncoder() {  //Lo usamos en el método de configuración anterior
		return new BCryptPasswordEncoder();
	}

	
	
//MÉTODO DE AUTORIZACIÓN:	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { //CONFIGURA LA AUTORIZACIÓN
	
		http
			.authorizeRequests() //QUE AUTORICE PETICIONES...
				.antMatchers("/","/webjars/**","/css/**","/h2-console/**","/public/**","/auth/**","/files/**")//siempre que estén en las mencionadas rutas
				.permitAll() //las permita a todos los usuarios
				.anyRequest().authenticated()  //cualquier otra petición debe ser autenticada
				.and() //y además...
			.formLogin()	//QUE AÑADA UN FORMULARIO DE LOGIN...
				.loginPage("/auth/login")  //indicamos la ruta donde queremos que esté la página de login
				.defaultSuccessUrl("/public/index",true) //Indica URL por defecto a la que será redirigido el usuario tras enviar datos del formulario con éxito.
				.loginProcessingUrl("/auth/login-post") //redirige al controlador de configuración del registro de usuario en el formulario; el parámetro es la ruta al controlador para validar usuario y password.
				.permitAll() //permite el acceso al formulario de login a cualquier usuario
				.and() //y además......
			.logout() //QUE CONTENTA UN LOGOUT...
				.logoutUrl("/auth/logout") //indica la URL de logout
				.logoutSuccessUrl("/public/index");  //URL de redirección tras hacer logout con éxito
		http.csrf().disable();  //Deshabilitar elementos de seguridad necesarios para uso de la consola de h2
		http.headers().frameOptions().disable(); //Deshabilitar elementos de seguridad necesarios para uso de la consola de h2
	}

	
}
