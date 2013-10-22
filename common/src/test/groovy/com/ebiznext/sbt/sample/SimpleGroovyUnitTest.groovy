package com.ebiznext.sbt.sample

import static org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class SimpleGroovyUnitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

    @Test
    public void testFormatMontant() {
        String montant = com.ebiznext.sbt.sample.utils.Utils.formatMontant("123456.222");
        assertNotNull(montant);
        assertEquals("123456.22", montant);
    }

}
