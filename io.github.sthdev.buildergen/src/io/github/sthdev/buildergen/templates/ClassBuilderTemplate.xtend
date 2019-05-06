package io.github.sthdev.buildergen.templates

import io.github.sthdev.buildergen.GenClassBuilderTemplateUtil
import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.Map.Entry
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.ecore.EStructuralFeature

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

		genModel.importManager.addImport(EStructuralFeature.name);
		genModel.importManager.addImport(List.name);
		genModel.importManager.addImport(Map.name);
		genModel.importManager.addImport(HashMap.name);
		genModel.importManager.addImport(Map.name, Entry.simpleName);

		val classBody = '''
			/**
			 * Builder for {@link «genClass.getImportedInterfaceName()»}.
			 *
			 * @generated
			 */
			public class «builderClassName» {
			
				/**
				 * This map contains the feature values of the built object.
				 * 
				 * @generated
				 */
				private final Map<EStructuralFeature, Object> featureValues = new HashMap<EStructuralFeature, Object>();
			
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
				@SuppressWarnings("unchecked")
				public «genClass.getName()» build() {
					«genClass.getName()» object = «genPackage.getImportedFactoryInterfaceName()».eINSTANCE.create«genClass.getName()»();
					
					for (Entry<EStructuralFeature, Object> entry : featureValues.entrySet()) {
						if (!entry.getKey().isMany()) {
							object.eSet(entry.getKey(), entry);
						}
						else {
							((List<Object>) object.eGet(entry.getKey())).addAll((List<? extends Object>) entry.getValue());	
						}
					}
					
					return object;
				}
				
				«generateFeatureSetters(genClass)»
			}
		'''

		val importStringBuffer = new StringBuffer;
		genModel.markImportLocation(importStringBuffer)
		genModel.emitSortedImports();

		return '''
			package «GenClassBuilderTemplateUtil.getBuilderPackageName(genPackage)»;
			
			«importStringBuffer»
			
			«classBody»
		'''
	}

	def static String generateFeatureSetters(GenClass genClass) {
		val features = genClass.allGenFeatures.filter[!isDerived && isChangeable]
		return '''
		«FOR genFeature : features.filter[!listType]»
			«generateSingleValuedFeatureSetter(genFeature, GenClassBuilderTemplateUtil.getBuilderClassName(genClass))»
		«ENDFOR»
		'''
	}

	def static String generateSingleValuedFeatureSetter(GenFeature genFeature, String builderClassName) {
		val paramName = genFeature.safeName
		val featureConstant = genFeature.getQualifiedFeatureAccessor()
		
		return '''
		/**
		 * Sets the value of the «genFeature.formattedName» feature.
		 *
		 * <!-- begin-user-doc -->
		 * «genFeature.documentation»
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		public «builderClassName» with«genFeature.capName»(«genFeature.getListItemType(null)» «paramName») {
			featureValues.put(«featureConstant», «paramName»);
			
			return this;
		}'''
	}
}
