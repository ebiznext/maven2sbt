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
class ITEdificeListerReceptions {

	static final String token = '8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-2057-7401-2971-7401-8687-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-2852-2852-2852-2852-7363-2852-2852-2852-2852'

	static final HTTPClient client = HTTPClient.getInstance()

	@Test
	public void verifierListeCommandesAReceptionnerFournisseurNonVide() {
		String numeroCommandeValide = '1000003371'
		HttpURLConnection conn = rechercherDetailCommandeAReceptionner(numeroCommandeValide)
		assertNotNull(conn)
		try {
			assertEquals(200, conn.getResponseCode())
			def commandes = client.parseTextAsJSON(conn)
			assertNotNull commandes
			assertEquals (1, commandes.size)
			commandes.each { commande ->
				assertNotNull(commande.entete)
				assertNotNull(commande.entete.raisonSociale)
				assertEquals('MAILLET FRANCIS COMPETITION', commande.entete.raisonSociale)
				assertNotNull(commande.entete.codeSociete)
				assertEquals('024X', commande.entete.codeSociete)
				assertNotNull(commande.entete.montantCommande)
				assertEquals('4960.0', commande.entete.montantCommande)
				assertNotNull(commande.entete.codeFournisseur)
				assertEquals('0000300051', commande.entete.codeFournisseur)
				assertNotNull(commande.entete.devise)
				assertEquals('EUR', commande.entete.devise)
				assertNotNull(commande.lignes)
			    assertEquals (4, commande.lignes.size)
			}
		} catch (IOException e) {
		    e.printStackTrace()
			fail(e.getMessage())
		}
	}

	@Test
	public void verifierListeCommandesAReceptionnerFournisseurVide() {
		String numeroCommandeNonValide = '00000'
		HttpURLConnection conn = rechercherDetailCommandeAReceptionner(numeroCommandeNonValide)
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

	private HttpURLConnection rechercherDetailCommandeAReceptionner(String numeroCommande){
		String port = System.getProperty("sample.port")
		assertNotNull(port)
		String url = "http://localhost:" + port + "/sample/RechercherDetailCommandeAReceptionner"
		Map config = [debug:true]
		Map params = [token:token, numeroCommande:numeroCommande, service: 'edifice.reception']
		HttpURLConnection conn = client.doGet(config, url, params)
		return conn
	}
}