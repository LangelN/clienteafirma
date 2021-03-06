/* Copyright (C) 2011 [Gobierno de Espana]
 * This file is part of "Cliente @Firma".
 * "Cliente @Firma" is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation;
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * You may contact the copyright holder at: soporte.afirma@seap.minhap.es
 */

package es.gob.afirma.signfolder.server.proxy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/** Configuraci&oacute;n para la gesti&oacute;n del almacenamiento temporal de ficheros en servidor. */
final class RetrieveConfig {

	private static final Logger LOGGER = Logger.getLogger("es.gob.afirma");  //$NON-NLS-1$
	
	/** Fichero de configuraci&oacute;n. */
	private static final String CONFIG_FILE = "configuration.properties"; //$NON-NLS-1$

	/** Clave para la configuraci&oacute;n del directorio para la creacion de ficheros temporales. */
	private static final String TMP_DIR_KEY =  "tmpDir"; //$NON-NLS-1$

	/** Directorio temporal por defecto. */
	private static String defaultTmpDir;
	
	/** Directorio temporal a usar. */
	private static final File TMP_DIR;

	/** Clave para la configuraci&oacute;n del tiempo de caducidad de los ficheros temporales. */
	private static final String EXPIRATION_TIME_KEY =  "expTime"; //$NON-NLS-1$

	/** Milisegundos que, por defecto, tardan los mensajes en caducar. */
	private static final long DEFAULT_EXPIRATION_TIME = 60000; // 1 minuto
	
	private static final long EXPIRATION_TIME;

	/** Modo de depuraci&oacute;n activo o no, en el que no se borran los ficheros en servidor. */
	static final boolean DEBUG_NO_DELETE;

	private static final String DEBUG_KEY = "debug"; //$NON-NLS-1$

	static {
		final Properties config = new Properties();
		try {
			final InputStream is = RetrieveConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			config.load(is);
			is.close();
		}
		catch (final IOException e) {
			LOGGER.severe(
				"No se ha podido cargar el fichero con las propiedades (" + CONFIG_FILE + "), se usaran los valores por defecto: " + e.toString() //$NON-NLS-1$ //$NON-NLS-2$
			);
		}
		
		DEBUG_NO_DELETE = Boolean.parseBoolean(config.getProperty(DEBUG_KEY));
		if (DEBUG_NO_DELETE) {
			LOGGER.warning("Modo de depuracion activado, no se borraran los ficheros en servidor"); //$NON-NLS-1$
		}
		
		try {
			defaultTmpDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		}
		catch (final Exception e) {
			LOGGER.warning(
				"El directorio temporal no ha podido determinarse por la variable de entorno 'java.io.tmpdir': " + e //$NON-NLS-1$
			);
			try {
				defaultTmpDir = File.createTempFile("tmp", null).getParentFile().getAbsolutePath(); //$NON-NLS-1$
			}
			catch (final Exception e1) {
				defaultTmpDir = null;
				LOGGER.warning(
					"No se ha podido cargar un directorio temporal por defecto, se debera configurar expresamente en el fichero de propiedades: "  + e1 //$NON-NLS-1$
				);
			}
		}
		
		File tmpDir = new File(config.getProperty(TMP_DIR_KEY, defaultTmpDir).trim());
		if (!tmpDir.isDirectory() || !tmpDir.canRead()) {
			LOGGER.warning(
				"El directorio temporal indicado en el fichero de propiedades (" + config.getProperty(TMP_DIR_KEY, defaultTmpDir) + ") no existe, se usara el por defecto: " +  defaultTmpDir //$NON-NLS-1$ //$NON-NLS-2$
			);
			tmpDir = new File(defaultTmpDir);
			if (!tmpDir.isDirectory() ||!tmpDir.canRead()) {
				throw new IllegalStateException("No se ha podido definir un directorio temporal"); //$NON-NLS-1$
			}
		}
		TMP_DIR = tmpDir;
		
		long expTime;
		try {
			expTime = config.containsKey(EXPIRATION_TIME_KEY) ?
				Long.parseLong(config.getProperty(EXPIRATION_TIME_KEY)) :
					DEFAULT_EXPIRATION_TIME;
		}
		catch (final Exception e) {
			LOGGER.warning(
				"Tiempo de expiracion invalido en el fichero de configuracion (" + CONFIG_FILE + "), se usara " + DEFAULT_EXPIRATION_TIME + ": " + e //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			);
			expTime = DEFAULT_EXPIRATION_TIME;
		}
		EXPIRATION_TIME = expTime;
		
	}

	/** Recupera el directorio configurado para la creaci&oacute;n de ficheros temporales o el por defecto.
	 * @return Directorio temporal.
	 * @throws NullPointerException Cuando no se indicala ruta del directorio temporal ni se puede obtener
	 *                              del sistema */
	static File getTempDir() {
		return TMP_DIR;
	}

	/** Recupera el tiempo en milisegundos que puede almacenarse un fichero antes de considerarse caducado.
	 * @return Tiempo m&aacute;ximo en milisegundos que puede tardarse en recoger un fichero antes de que
	 *         caduque. */
	static long getExpirationTime() {
		return EXPIRATION_TIME;
	}
}
