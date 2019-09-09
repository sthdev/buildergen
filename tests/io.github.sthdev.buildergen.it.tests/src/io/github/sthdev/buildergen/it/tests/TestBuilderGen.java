package io.github.sthdev.buildergen.it.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.sthdev.buildergen.it.pobs.EclipseWorkbenchPob;

public class TestBuilderGen {

	private EclipseWorkbenchPob eclipseWorkbench;

	@Before
	public void setup() {
		eclipseWorkbench = new EclipseWorkbenchPob();
		eclipseWorkbench.closeWelcomeScreen();
	}

	@Test
	public void generateCodeForSimpleEClass() throws Exception {
		Assert.assertTrue(true);
	}
}
