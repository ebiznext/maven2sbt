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
class ITEdificeReceptionnerCommande {

	static final String token = '8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-2057-7401-2971-7401-8687-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-2852-2852-2852-2852-7363-2852-2852-2852-2852'

	static final HTTPClient client = HTTPClient.getInstance()
	
	static final String numeroCommandeValide = '1000003328'
	
	@Test
	public void receptionnerLignesDeCommandeOK() {
		HttpURLConnection conn = receptionnerLignesDeCommande(numeroCommandeValide, '1,1', 'bonDeLivraison')
		assertNotNull(conn)
		try {
			assertEquals(200, conn.getResponseCode())
		} catch (IOException e) {
		    e.printStackTrace()
			fail(e.getMessage())
		}
	}

	private HttpURLConnection receptionnerLignesDeCommande(String numeroCommande, String reception, String bonDeLivraison){
		String port = System.getProperty("sample.port")
		assertNotNull(port)
		String url = "http://localhost:" + port + "/sample/ReceptionnerLignesDeCommande"
		Map config = [debug:true]
		Map params = [token:token, service:'edifice.reception']
		params['numerocommande'] = numeroCommande
		params['reception'] = reception
		params['bonDeLivraison'] = bonDeLivraison
		HttpURLConnection conn = client.doGet(config, url, params)
		return conn
	}
}