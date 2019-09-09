package io.github.sthdev.buildergen;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Test;

/**
 * Tests {@link GenClassBuilderTemplateUtil}.
 */
public class GenClassBuilderTemplateUtilTest {

	@Test
	public void getBuilderClassName_withValidGenClass_returnsGenClassNameWithBuilderSuffix() {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName("MyClass");
		GenClass genClass = GenModelFactory.eINSTANCE.createGenClass();
		genClass.setEcoreClass(eClass);

		String builderClassName = GenClassBuilderTemplateUtil.getBuilderClassName(genClass);

		assertThat(builderClassName, is(eClass.getName() + "Builder"));
	}
}
