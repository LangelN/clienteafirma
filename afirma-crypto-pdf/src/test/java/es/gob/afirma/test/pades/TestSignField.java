package es.gob.afirma.test.pades;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;

import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.signers.pades.AOPDFSigner;

/** Pruebas de firmas PDF visibles.
 * @author Carlos Gamuci */
public class TestSignField {

	private final static String TEST_FILE = "TEST_PDF.pdf"; //$NON-NLS-1$

	private final static String RUBRIC_IMAGE = "rubric.jpg"; //$NON-NLS-1$

	private final static String DEFAULT_SIGNATURE_ALGORITHM = "SHA512withRSA"; //$NON-NLS-1$

	private final static String CERT_PATH = "ANF_PF_Activo.pfx"; //$NON-NLS-1$
	private final static String CERT_PASS = "12341234"; //$NON-NLS-1$
	private final static String CERT_ALIAS = "anf usuario activo"; //$NON-NLS-1$


	//TODO: Averiguar porque en MAVEN no encuentra la fuente Helvetica

	/** Prueba de firma PDF visible sin r&uacute;brica.
	 * @throws Exception */
	@SuppressWarnings("static-method")
	@Test
	@Ignore
	public void testCampoDeFirmaSoloConPosiciones() throws Exception {

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Prueba de firma de PDF solo con posiciones de firma"); //$NON-NLS-1$

		final PrivateKeyEntry pke;

        final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
        ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS.toCharArray());
        pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new KeyStore.PasswordProtection(CERT_PASS.toCharArray()));

		final Properties extraParams = new Properties();
		extraParams.put("signaturePositionOnPageLowerLeftX", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageLowerLeftY", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightX", "200"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightY", "200"); //$NON-NLS-1$ //$NON-NLS-2$

		final byte[] testPdf = AOUtil.getDataFromInputStream(ClassLoader.getSystemResourceAsStream(TEST_FILE));

		final AOPDFSigner signer = new AOPDFSigner();
		final byte[] signedPdf = signer.sign(
			testPdf,
			DEFAULT_SIGNATURE_ALGORITHM,
			pke.getPrivateKey(),
			pke.getCertificateChain(),
			extraParams
		);

		final File tempFile = File.createTempFile("afirmaPDF", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$

		final FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(signedPdf);
		fos.close();

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Fichero temporal para la comprobacion manual del resultado: " + //$NON-NLS-1$
				tempFile.getAbsolutePath());
	}

	/** Prueba de firma PDF visible con r&uacute;brica.
	 * @throws Exception */
	@SuppressWarnings("static-method")
	@Test
	@Ignore
	public void testCampoDeFirmaConPosicionesYRubrica() throws Exception {

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Prueba de firma de PDF con posiciones de firma y rubrica"); //$NON-NLS-1$

		final PrivateKeyEntry pke;

        final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
        ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS.toCharArray());
        pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new KeyStore.PasswordProtection(CERT_PASS.toCharArray()));

		final Properties extraParams = new Properties();
		extraParams.put("signaturePositionOnPageLowerLeftX", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageLowerLeftY", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightX", "200"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightY", "200"); //$NON-NLS-1$ //$NON-NLS-2$

		final byte[] rubricImage = AOUtil.getDataFromInputStream(ClassLoader.getSystemResourceAsStream(RUBRIC_IMAGE));

		final String rubricImageB64 = Base64.encode(rubricImage);

		extraParams.put("signatureRubricImage", rubricImageB64); //$NON-NLS-1$

		final byte[] testPdf = AOUtil.getDataFromInputStream(ClassLoader.getSystemResourceAsStream(TEST_FILE));

		final AOPDFSigner signer = new AOPDFSigner();
		final byte[] signedPdf = signer.sign(
			testPdf,
			DEFAULT_SIGNATURE_ALGORITHM,
			pke.getPrivateKey(),
			pke.getCertificateChain(),
			extraParams
		);

		final File tempFile = File.createTempFile("afirmaPDF", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$

		final FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(signedPdf);
		fos.close();

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Fichero temporal para la comprobacion manual del resultado: " + //$NON-NLS-1$
				tempFile.getAbsolutePath());
	}

	/** Prueba de firma PDF visible con un texto.
	 * @throws Exception */
	@SuppressWarnings("static-method")
	@Test
	@Ignore
	public void testCampoDeFirmaConPosicionesYTexto() throws Exception {

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Prueba de firma de PDF con posiciones de firma y texto"); //$NON-NLS-1$

		final PrivateKeyEntry pke;

        final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
        ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS.toCharArray());
        pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new KeyStore.PasswordProtection(CERT_PASS.toCharArray()));

		final Properties extraParams = new Properties();
		extraParams.put("signaturePositionOnPageLowerLeftX", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageLowerLeftY", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightX", "200"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightY", "200"); //$NON-NLS-1$ //$NON-NLS-2$

		extraParams.put("layer2Text", "Este es el texto de prueba 'Hola Mundo'"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontFamily", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontSize", "14"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontStyle", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontColor", "red"); //$NON-NLS-1$ //$NON-NLS-2$
//		extraParams.put("layer4Text ", "Texto secundario"); //$NON-NLS-1$

		final byte[] testPdf = AOUtil.getDataFromInputStream(ClassLoader.getSystemResourceAsStream(TEST_FILE));

		final AOPDFSigner signer = new AOPDFSigner();
		final byte[] signedPdf = signer.sign(
			testPdf,
			DEFAULT_SIGNATURE_ALGORITHM,
			pke.getPrivateKey(),
			pke.getCertificateChain(),
			extraParams
		);

		final File tempFile = File.createTempFile("afirmaPDF", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$

		final FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(signedPdf);
		fos.close();

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Fichero temporal para la comprobacion manual del resultado: " + //$NON-NLS-1$
				tempFile.getAbsolutePath());
	}

	/** Prueba de firma PDF visible con r&uacute;brica y texto.
	 * @throws Exception */
	@SuppressWarnings("static-method")
	@Test
	public void testCampoDeFirmaConPosicionesRubricaYTexto() throws Exception {

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Prueba de firma de PDF con posiciones de firma, rubrica y texto"); //$NON-NLS-1$

		final PrivateKeyEntry pke;

        final KeyStore ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
        ks.load(ClassLoader.getSystemResourceAsStream(CERT_PATH), CERT_PASS.toCharArray());
        pke = (PrivateKeyEntry) ks.getEntry(CERT_ALIAS, new KeyStore.PasswordProtection(CERT_PASS.toCharArray()));

		final Properties extraParams = new Properties();
		extraParams.put("signaturePositionOnPageLowerLeftX", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageLowerLeftY", "100"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightX", "200"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("signaturePositionOnPageUpperRightY", "200"); //$NON-NLS-1$ //$NON-NLS-2$

		final byte[] rubricImage = AOUtil.getDataFromInputStream(ClassLoader.getSystemResourceAsStream(RUBRIC_IMAGE));

		final String rubricImageB64 = Base64.encode(rubricImage);

		extraParams.put("signatureRubricImage", rubricImageB64); //$NON-NLS-1$
		extraParams.put("layer2Text", "Este es el texto de prueba 'Hola Mundo'"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontFamily", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontSize", "14"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontStyle", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		extraParams.put("layer2FontColor", "red"); //$NON-NLS-1$ //$NON-NLS-2$

		final byte[] testPdf = AOUtil.getDataFromInputStream(ClassLoader.getSystemResourceAsStream(TEST_FILE));

		final AOPDFSigner signer = new AOPDFSigner();
		final byte[] signedPdf = signer.sign(
			testPdf,
			DEFAULT_SIGNATURE_ALGORITHM,
			pke.getPrivateKey(),
			pke.getCertificateChain(),
			extraParams
		);

		final File tempFile = File.createTempFile("afirmaPDF", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$

		final FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(signedPdf);
		fos.close();

		Logger.getLogger("es.gob.afirma").info( //$NON-NLS-1$
				"Fichero temporal para la comprobacion manual del resultado: " + //$NON-NLS-1$
				tempFile.getAbsolutePath());
	}

	/** Entrada para pruebas manuales.
	 * @param args
	 * @throws Exception */
	public static void main(final String[] args) throws Exception {
		final TestSignField test = new TestSignField();
		test.testCampoDeFirmaConPosicionesRubricaYTexto();
	}
}
