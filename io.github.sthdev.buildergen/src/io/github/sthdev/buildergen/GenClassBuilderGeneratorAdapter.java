package io.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;

public class GenClassBuilderGeneratorAdapter extends GenBaseGeneratorAdapter {

	private static final JETEmitterDescriptor[] JET_EMITTER_DESCRIPTORS = new JETEmitterDescriptor[] {
			new JETEmitterDescriptor("ClassBuilderTemplate.javajet",
					"io.github.sthdev.buildergen.templates.jet.ClassBuilderJetTemplate") };

	public GenClassBuilderGeneratorAdapter(BuilderGeneratorAdapterFactory generatorAdapterFactory) {
		super(generatorAdapterFactory);
	}

	@Override
	public boolean canGenerateModel(Object object) {
		GenClass genClass = (GenClass) object;

		return !genClass.isAbstract() && genClass.getEcoreClass().getInstanceClass() == null;
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

		generateJava(genModel.getModelDirectory(),
				GenClassBuilderTemplateUtil.getBuilderPackageName(genClass.getGenPackage()),
				GenClassBuilderTemplateUtil.getBuilderClassName(genClass), getClassBuilderTemplateJETEmitter(), null,
				createMonitor(monitor, 1));

//		for (GenFeature genFeature : genClass.getAllGenFeatures()) {
//			genFeature.capName(name)
//		}

//		genClass.getGenPackage().getImportedFactoryInterfaceName()

		return Diagnostic.OK_INSTANCE;
	}

	private JETEmitter getClassBuilderTemplateJETEmitter() {
		return getJETEmitter(JET_EMITTER_DESCRIPTORS, 0);
	}

}
