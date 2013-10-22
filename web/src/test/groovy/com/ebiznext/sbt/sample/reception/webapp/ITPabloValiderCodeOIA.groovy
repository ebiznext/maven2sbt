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
class ITPabloValiderCodeOIA {

	static final String token = '8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-4847-2057-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-4316-4316-4316-4316-7363-4316-4316-4316-4316'

	static final HTTPClient client = HTTPClient.getInstance()
	@Test
	public void verifierCodeOIAValide() {
		String valideCodeOIA = '024X.TNR017'
		HttpURLConnection conn = validerCodeOIA(valideCodeOIA)
		assertNotNull(conn)
		try {
			assertEquals(200, conn.getResponseCode())
			def resp = client.parseTextAsJSON(conn)
			assertNotNull(resp)
			assertEquals(true, resp.resultat)
			assertEquals(valideCodeOIA, resp.codeOIA)
			assertEquals('TEST TDBWF NEW ARE', resp.libelle)			
		} catch (IOException e) {
			fail(e.getMessage())
		}
	}

	@Test
	public void verifierCodeOIAInvalide() {
		String invalideCodeOIA = '00000'
		HttpURLConnection conn = validerCodeOIA(invalideCodeOIA)
		assertNotNull(conn)
		try {
			assertEquals(200, conn.getResponseCode())
			def resp = client.parseTextAsJSON(conn)
			assertNotNull(resp)
			assertEquals(false, resp.resultat)
		} catch (IOException e) {
			fail(e.getMessage())
		}
	}

	private HttpURLConnection validerCodeOIA(String codeOIA){
		String port = System.getProperty("sample.port")
		assertNotNull(port)
		String url = "http://localhost:" + port + "/sample/ValiderCodeOIA"
		Map config = [debug:true]
		Map params = [token:token, codeOIA: codeOIA, service: 'etde.reception']
		HTTPClient client = HTTPClient.getInstance()
		HttpURLConnection conn = client.doGet(config, url, params)
		return conn
	}
}