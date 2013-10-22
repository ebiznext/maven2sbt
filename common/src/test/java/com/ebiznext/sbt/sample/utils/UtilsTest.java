/**
 * 
 */
package com.ebiznext.sbt.sample.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ebiznext.sbt.sample.reception.vo.UserToken;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author stephane.manciot@ebiznext.com
 *
 */
public class UtilsTest {

	static final String TOKEN = "8656-8277-2787-7652-8964-7502-4045-14059-8949-8949-3145-7363-14579-2057-7401-2971-7401-8687-14579-711-12844-14579-8687-14579-8890-4847-7401-13335-1440-7363-2852-2852-2852-2852-7363-2852-2852-2852-2852";

	static final String TOKEN_TO_JSON="{\"uuid\":\"12345678990\",\"service\":\"edifice.reception\",\"login\":\"aaaa\",\"password\":\"aaaa\"}";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.ebiznext.sbt.sample.utils.Utils#decrypt(java.lang.String)}.
	 */
	@Test
	public void testDecrypt() {
		UserToken token = Utils.decrypt(TOKEN);
		assertNotNull(token);
		assertEquals("12345678990", token.getUuid());
		assertEquals("edifice.reception", token.getService());
		assertEquals("aaaa", token.getLogin());
		assertEquals("aaaa", token.getPassword());
	}

	/**
	 * Test method for {@link com.ebiznext.sbt.sample.utils.Utils#jsonOutput(java.lang.Object)}.
	 */
	@Test
	public void testJsonOutput() {
		try {
			String asJSON = Utils.jsonOutput(Utils.decrypt(TOKEN));
			assertNotNull(asJSON);
			assertEquals(TOKEN_TO_JSON, asJSON);
		} catch (JsonGenerationException e) {
			fail(e.getMessage());
		} catch (JsonMappingException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.ebiznext.sbt.sample.utils.Utils#formatMontant(java.lang.String)}.
	 */
	/*@Test
	public void testFormatMontant() {
		String montant = Utils.formatMontant("123456.222");
		assertNotNull(montant);
		assertEquals("123456.22", montant);
	}*/

	/**
	 * Test method for {@link com.ebiznext.sbt.sample.utils.Utils#formatDescription(java.lang.String)}.
	 */
	@Test
	public void testFormatDescription() {
		String description = Utils.formatDescription("description");
		assertNotNull(description);
		assertEquals("description", description);
	}

}
