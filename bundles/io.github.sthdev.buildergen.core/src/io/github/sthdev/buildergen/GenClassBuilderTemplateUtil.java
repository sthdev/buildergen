package io.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;

/**
 * This utility class provides some static methods to compute the package and
 * class names of generated builder classes.
 */
public class GenClassBuilderTemplateUtil {

	private static String BUILDER_PACKAGE_NAME = "builder";

	private GenClassBuilderTemplateUtil() {
		// no instances allowed
	}

	/**
	 * Returns the name of the Java package that should contain the builder classes
	 * generated for the {@link GenClass}es in the specified {@link GenPackage}.
	 */
	public static String getBuilderPackageName(GenPackage genPackage) {
		String basePackage = genPackage.getInterfacePackageName();
		return basePackage != null && !basePackage.isEmpty() ? basePackage + "." + BUILDER_PACKAGE_NAME
				: BUILDER_PACKAGE_NAME;
	}

	/**
	 * Returns the name of the builder class that should be generated for the
	 * specified {@link GenClass}.
	 */
	public static String getBuilderClassName(GenClass genClass) {
		return genClass.getName() + "Builder";
	}

}
