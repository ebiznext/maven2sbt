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
class ITEdificeLoginReception {

	@Test
	public void verifierLoginValide() {
		String validToken = '8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-2057-7401-2971-7401-8687-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-2852-2852-2852-2852-7363-2852-2852-2852-2852'
		HttpURLConnection conn = verifierLogin(validToken)
		assertNotNull(conn)
		try {
			assertEquals(200, conn.getResponseCode())
		} catch (IOException e) {
			fail(e.getMessage())
		}
	}

	@Test
	public void verifierLoginInvalide() {
		String invalidToken = '8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-2057-7401-2971-7401-8687-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-4316-4316-4316-4316-7363-4316-4316-4316-4316'
		HttpURLConnection conn = verifierLogin(invalidToken)
		assertNotNull(conn)
		try {
			assertEquals(400, conn.getResponseCode())
		} catch (IOException e) {
			fail(e.getMessage())
		}
	}

	private HttpURLConnection verifierLogin(String token){
		String port = System.getProperty("sample.port")
		assertNotNull(port)
		String url = "http://localhost:" + port + "/sample/VerifierLoginReception"
		Map config = [debug:true]
		Map params = [token:token]
		HTTPClient client = HTTPClient.getInstance()
		HttpURLConnection conn = client.doGet(config, url, params)
		return conn
	}
}