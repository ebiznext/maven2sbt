/**
 * 
 */
package com.ebiznext.sbt.sample.reception.webapp

import org.junit.Test

import static org.junit.Assert.*

import com.ebiznext.http.client.HTTPClient

/**
 * @author stephane.manciot@ebiznext.com
 * 
 */
class ITEdificeListerFournisseurs {

	static final String token = '8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-2057-7401-2971-7401-8687-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-2852-2852-2852-2852-7363-2852-2852-2852-2852'

	static final HTTPClient client = HTTPClient.getInstance()

	@Test
	public void verifierListeFournisseursNonVide() {
		String valideCodeOIA = '024X.TNR017'
		HttpURLConnection conn = listerFournisseurs(valideCodeOIA)
		assertNotNull(conn)
		 try {
			assertEquals(200, conn.getResponseCode())
			def fournisseurs = client.parseTextAsJSON(conn)
			assertNotNull fournisseurs
			assertEquals (2, fournisseurs.size)
			fournisseurs.each { fournisseur ->
				assertNotNull(fournisseur.code)
				assertNotNull(fournisseur.libelle)
				assertNotNull(fournisseur.nbCommandes)
			}
		} catch (IOException e) {
		    e.printStackTrace()
			fail(e.getMessage())
		}
	}

	@Test
	public void verifierListeFournisseursVide() {
		String invalideCodeOIA = '00000'
		HttpURLConnection conn = listerFournisseurs(invalideCodeOIA)
		assertNotNull(conn)
		try {
			assertEquals(200, conn.getResponseCode())
			def fournisseurs = client.parseTextAsJSON(conn)
			assertNotNull fournisseurs
			assertEquals (0, fournisseurs.size)
		} catch (IOException e) {
			fail(e.getMessage())
		}
	}

	private HttpURLConnection listerFournisseurs(String codeOIA){
		String port = System.getProperty("sample.port")
		println ("sample.port" + port)
		assertNotNull(port)
		String url = "http://localhost:" + port + "/sample/ListerFournisseurs"
		Map config = [debug:true]
		Map params = [token:token, codeOIA: codeOIA, service: 'edifice.reception']
		HttpURLConnection conn = client.doGet(config, url, params)
		return conn
	}
}