package com.openwebinars.secondhandmarket.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.openwebinars.secondhandmarket.upload.StorageProperties;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)//Notación necesaria al añadir la clase
										//StorageProperties.java del paquete upload

public class ConfiguracionAuditoria {  
	
	/*Esta configuración con sus anotaciones permitirá insertar la marca de tiempo (fecha y hora:TIMESTAMP)
	 *a los atributos de tipo Date del modelo; de esta manera no tenemos que hacer nada: esta 
	 *configuración recoge automáticamente los datos temporales del sistema. 
	 */

}
