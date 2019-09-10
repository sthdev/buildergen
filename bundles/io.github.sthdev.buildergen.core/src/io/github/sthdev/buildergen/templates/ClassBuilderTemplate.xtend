package io.github.sthdev.buildergen.templates

import io.github.sthdev.buildergen.GenClassBuilderTemplateUtil
import java.util.Arrays
import java.util.Collection
import java.util.HashMap
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Map.Entry
import org.eclipse.emf.codegen.ecore.genmodel.GenClass
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature
import org.eclipse.emf.ecore.EStructuralFeature

/**
 * Performs the generation of the actual builder classes. The JET template redirects to this
 * template so that the JET features like import management, consideration of genmodel options,
 * etc. can be used with an Xtend code generation template, which is much easier to maintain.
 * <p>
 * The generated builder class consists of a build() method, which creates a new instance of
 * the builder's type, setter methods named after the type's EStructuralFeatures, as
 * well as a Map, which stores the feature values. The setter methods store their feature 
 * values in the Map, the build method creates a new instance and iterates over the map to set
 * the feature values. 
 */
class ClassBuilderTemplate {

	val static String FEATURE_VALUES_MAP_NAME = "featureValues";

	/**
	 * Generates a builder class for the specified GenClass. 
	 */
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
				private final Map<EStructuralFeature, Object> «FEATURE_VALUES_MAP_NAME» = new HashMap<EStructuralFeature, Object>();
			
				«generateConstructor(genClass)»
			
				«generateBuildMethod(genClass)»
				
				«generateFeatureSetters(genClass)»
			}
		'''

		val importStringBuffer = new StringBuffer;
		genModel.markImportLocation(importStringBuffer)
		genModel.emitSortedImports();

		var classText = ""

		if (genModel.hasCopyright) {
			classText += genModel.getCopyright("");
		}

		classText += '''
			package «GenClassBuilderTemplateUtil.getBuilderPackageName(genPackage)»;
			
			«importStringBuffer»
			
			«classBody»
		'''

		return classText;
	}

	/**
	 * Generates the builder's constructor.
	 */
	private static def String generateConstructor(GenClass genClass) {
		return '''
			/**
			 * Creates a new builder for {@link «genClass.getName()»}.
			 *
			 * @generated
			 */
			public «GenClassBuilderTemplateUtil.getBuilderClassName(genClass)»() {
				// Does nothing by default.
			}
		'''
	}

	/**
	 * Generates the builder's build() method. The build() method creates a new instance of
	 * the builder's type, iterates through the map of feature values and sets those value
	 * on the built object.
	 */
	private static def String generateBuildMethod(GenClass genClass) {
		return '''
			/**
			 * Builds a new instance of {@link «genClass.getName()»}. The builder can be reused
			 * multiple times to create additional instances with the same feature values.
			 *
			 * @generated
			 */
			@SuppressWarnings("unchecked")
			public «genClass.getName()» build() {
				«genClass.getName()» object = «genClass.genPackage.getImportedFactoryInterfaceName()».eINSTANCE.create«genClass.getName()»();
				
				for (Entry<EStructuralFeature, Object> entry : «FEATURE_VALUES_MAP_NAME».entrySet()) {
					if (!entry.getKey().isMany()) {
						object.eSet(entry.getKey(), entry.getValue());
					}
					else if (entry.getValue() instanceof Map) {
						((Map<Object, Object>) object.eGet(entry.getKey())).putAll((Map<? extends Object, ? extends Object>) entry.getValue());	
					}
					else {
						((List<Object>) object.eGet(entry.getKey())).addAll((List<? extends Object>) entry.getValue());	
					}
				}
				
				return object;
			}
		'''
	}

	/**
	 * Generates setter methods for all features of the specified GenClass
	 * (including inherited features) that are changeable and not derived.
	 * For multi-valued features, two setters are generated: One accepting
	 * a list of values and one with a vararg parameter.
	 * <p>
	 * Map-typed references are also supported. For map-typed references,
	 * one setter accepting a map and one accepting a key and value are
	 * generated. 
	 */
	private static def String generateFeatureSetters(GenClass genClass) {
		val features = genClass.allGenFeatures.filter[!isDerived && isChangeable]
		val builderClassName = GenClassBuilderTemplateUtil.getBuilderClassName(genClass)

		return '''
			«FOR genFeature : features»
				«IF !genFeature.listType»
					«generateSingleValuedFeatureSetter(genFeature, builderClassName)»
					
				«ELSEIF genFeature.mapType»
					«generateMapTypeFeatureSetter(genFeature, builderClassName)»
					
					«generateMapTypeSingleEntryFeatureSetter(genFeature, builderClassName)»
					
				«ELSE»
					«generateMultiValuedFeatureSetter(genFeature, builderClassName)»
					
					«generateMultiValuedVarArgsFeatureSetter(genFeature, builderClassName)»
					
				«ENDIF»
			«ENDFOR»
		'''
	}

	/**
	 * Generates a setter for a single-valued feature.
	 */
	private static def String generateSingleValuedFeatureSetter(GenFeature genFeature, String builderClassName) {
		val paramName = genFeature.safeName
		val featureConstant = genFeature.getQualifiedFeatureAccessor()

		return '''
		/**
		 * Sets the value of the «genFeature.formattedName» «IF genFeature.isContains»containment «ENDIF»feature.
		 *
		 * <!-- begin-user-doc -->
		 * «genFeature.documentation»
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		public «builderClassName» with«genFeature.capName»(«genFeature.getImportedType(null)» «paramName») {
			«FEATURE_VALUES_MAP_NAME».put(«featureConstant», «paramName»);
			
			return this;
		}'''
	}

	/**
	 * Generates a setter for a multi-valued feature accepting a list of feature values.
	 */
	private static def String generateMultiValuedFeatureSetter(GenFeature genFeature, String builderClassName) {
		val paramName = genFeature.safeName
		val featureConstant = genFeature.getQualifiedFeatureAccessor()
		val listItemType = genFeature.getListItemType(null)

		genFeature.genModel.importManager.addImport(LinkedList.name)
		genFeature.genModel.importManager.addImport(Collection.name)

		return '''
		/**
		 * Adds the specified values to the «genFeature.formattedName» «IF genFeature.isContains»containment «ENDIF»feature.
		 *
		 * <!-- begin-user-doc -->
		 * «genFeature.documentation»
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		public «builderClassName» with«genFeature.capName»(Collection<«listItemType»> «paramName») {
			@SuppressWarnings("unchecked")
			List<«listItemType»> values = (List<«listItemType»>) «FEATURE_VALUES_MAP_NAME».get(«featureConstant»);
			
			if (values == null) {
				values = new LinkedList<«listItemType»>();
				«FEATURE_VALUES_MAP_NAME».put(«featureConstant», values);
			}
			
			values.addAll(«paramName»);
			
			return this;
		}'''
	}

	/**
	 * Generates a setter for a multi-valued feature accepting a vararg parameter.
	 */
	private static def String generateMultiValuedVarArgsFeatureSetter(GenFeature genFeature, String builderClassName) {
		val featureConstant = genFeature.getQualifiedFeatureAccessor()
		val listItemType = genFeature.getListItemType(null)

		genFeature.genModel.importManager.addImport(LinkedList.name)
		genFeature.genModel.importManager.addImport(Arrays.name)

		return '''
		/**
		 * Adds the specified values to the «genFeature.formattedName» «IF genFeature.isContains»containment «ENDIF»feature.
		 *
		 * <!-- begin-user-doc -->
		 * «genFeature.documentation»
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		public «builderClassName» with«genFeature.capName»(«listItemType» item, «listItemType»... items) {
			@SuppressWarnings("unchecked")
			List<«listItemType»> values = (List<«listItemType»>) «FEATURE_VALUES_MAP_NAME».get(«featureConstant»);
			
			if (values == null) {
				values = new LinkedList<«listItemType»>();
				«FEATURE_VALUES_MAP_NAME».put(«featureConstant», values);
			}
			
			values.add(item);
			
			if (items != null) {
				values.addAll(Arrays.asList(items));
			}
			
			return this;
		}'''
	}

	/**
	 * Generates a setter for a map-typed feature accepting a Map.
	 */
	private static def generateMapTypeFeatureSetter(GenFeature genFeature, String builderClassName) {
		val paramName = genFeature.safeName
		val featureConstant = genFeature.getQualifiedFeatureAccessor()
		val keyType = genFeature.getImportedMapKeyType(null)
		val valueType = genFeature.getImportedMapValueType(null)

		genFeature.genModel.importManager.addImport(Map.name)
		genFeature.genModel.importManager.addImport(HashMap.name)

		return '''
		/**
		 * Adds the specified key-value pairs to the «genFeature.formattedName» map feature.
		 *
		 * <!-- begin-user-doc -->
		 * «genFeature.documentation»
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		public «builderClassName» with«genFeature.capName»(Map<«keyType», «valueType»> «paramName») {
			@SuppressWarnings("unchecked")
			Map<«keyType», «valueType»> values = (Map<«keyType», «valueType»>) «FEATURE_VALUES_MAP_NAME».get(«featureConstant»);
			
			if (values == null) {
				values = new HashMap<«keyType», «valueType»>();
				«FEATURE_VALUES_MAP_NAME».put(«featureConstant», values);
			}
			
			values.putAll(«paramName»);
			
			return this;
		}'''
	}

	/**
	 * Generates a setter for a map-typed feature accepting a key and a value.
	 */
	private static def generateMapTypeSingleEntryFeatureSetter(GenFeature genFeature, String builderClassName) {
		val featureConstant = genFeature.getQualifiedFeatureAccessor()
		val keyType = genFeature.getImportedMapKeyType(null)
		val valueType = genFeature.getImportedMapValueType(null)

		genFeature.genModel.importManager.addImport(Map.name)
		genFeature.genModel.importManager.addImport(HashMap.name)

		return '''
		/**
		 * Adds the specified key-value pair to the «genFeature.formattedName» map feature.
		 *
		 * <!-- begin-user-doc -->
		 * «genFeature.documentation»
		 * <!-- end-user-doc -->
		 *
		 * @generated
		 */
		public «builderClassName» with«genFeature.capName»(«keyType» key, «valueType» value) {
			@SuppressWarnings("unchecked")
			Map<«keyType», «valueType»> values = (Map<«keyType», «valueType»>) «FEATURE_VALUES_MAP_NAME».get(«featureConstant»);
			
			if (values == null) {
				values = new HashMap<«keyType», «valueType»>();
				«FEATURE_VALUES_MAP_NAME».put(«featureConstant», values);
			}
			
			values.put(key, value);
			
			return this;
		}'''
	}

}
