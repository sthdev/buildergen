package io.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;

public class GenClassBuilderTemplateUtil {

	private static String BUILDER_PACKAGE_NAME = "builder";

	public static String getBuilderPackageName(GenPackage genPackage) {
		String basePackage = genPackage.getInterfacePackageName();
		return basePackage != null && !basePackage.isEmpty() ? basePackage + "." + BUILDER_PACKAGE_NAME
				: BUILDER_PACKAGE_NAME;
	}

	public static String getBuilderClassName(GenClass genClass) {
		return genClass.getName() + "Builder";
	}

}
