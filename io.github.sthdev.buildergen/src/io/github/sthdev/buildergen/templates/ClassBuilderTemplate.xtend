package io.github.sthdev.buildergen.templates

import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import io.github.sthdev.buildergen.GenClassBuilderTemplateUtil

/**
 * Performs the generation of the actual builder class. The JET template redirects to this
 * template so that the JET features like import management, consideration of genmodel options,
 * etc. can be used with an Xtend code generation template, which is much easier to maintain.
 */
class ClassBuilderTemplate {

	static def String generate(GenClass genClass) {
		val genPackage = genClass.getGenPackage();
		val genModel = genClass.getGenModel();
		val builderClassName = GenClassBuilderTemplateUtil.getBuilderClassName(genClass);
		val classBody = '''
			/**
			 * Builder for {@link «genClass.getImportedInterfaceName()»}.
			 *
			 * @generated
			 */
			public class «builderClassName» {
			
				/**
				 * Creates a new builder for {@link «genClass.getName()»}.
				 *
				 * @generated
				 */
				public «builderClassName»() {
					// Does nothing by default.
				}
					
				/**
				 * Builds a new instance of {@link «genClass.getName()»}. The builder can be reused
				 * multiple times to create additional instances with the same feature values.
				 *
				 * @generated
				 */
				public «genClass.getName()» build() {
					«genClass.getName()» object = «genPackage.getImportedFactoryInterfaceName()».eINSTANCE.create«genClass.getName()»();
					
					return object;
				}
			}			
		'''
		
		val importStringBuffer = new StringBuffer;
		genModel.markImportLocation(importStringBuffer)
		genModel.emitSortedImports();
		
		return 
		'''
		package «GenClassBuilderTemplateUtil.getBuilderPackageName(genPackage)»;
		
		«importStringBuffer»
		
		«classBody»
		'''
	}
}
