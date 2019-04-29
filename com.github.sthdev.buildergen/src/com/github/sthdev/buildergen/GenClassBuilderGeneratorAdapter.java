package com.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;

public class GenClassBuilderGeneratorAdapter extends GenBaseGeneratorAdapter {

	public GenClassBuilderGeneratorAdapter(BuilderGeneratorAdapterFactory generatorAdapterFactory) {
		super(generatorAdapterFactory);
	}

	@Override
	public boolean canGenerateModel(Object object) {
		GenClass genClass = (GenClass) object;

		return !genClass.isAbstract();
	}

	@Override
	public boolean canGenerateEdit(Object object) {
		return false;
	}

	@Override
	public boolean canGenerateEditor(Object object) {
		return false;
	}

	@Override
	public boolean canGenerateTests(Object object) {
		return false;
	}

	@Override
	protected Diagnostic generateModel(Object object, Monitor monitor) {
		GenClass genClass = (GenClass) object;

		monitor.beginTask("Generating builders...", 1);

		GenModel genModel = genClass.getGenModel();
		ensureProjectExists(genModel.getModelDirectory(), genClass, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE,
				genModel.isUpdateClasspath(), createMonitor(monitor, 1));

		generateJava(genModel.getModelDirectory(), getBuilderPackageName(genClass.getGenPackage()),
				getBuilderInterfaceName(genClass), getJETEmitter(getJETEmitterDescriptors(), VALIDATOR_ID), null,
				createMonitor(monitor, 1));

		return Diagnostic.OK_INSTANCE;
	}

	private String getBuilderPackageName(GenPackage genPackage) {
		String basePackage = genPackage.getInterfacePackageName();
		return basePackage.length() > 0 ? basePackage + ".builders" : "builders";
	}

	private String getBuilderInterfaceName(GenClass genClass) {
		return genClass.getName() + "Builder";
	}

}
