package io.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EClass;

/**
 * This is a {@link GeneratorAdapter} for {@link GenClass}es. It generates an
 * additional builder class for each non-abstract {@link GenClass} (or
 * {@link EClass} respectively). The actual code generation is delegated to a
 * JET template, which in turn delegates to an Xtend template.
 *
 */
public class GenClassBuilderGeneratorAdapter extends GenBaseGeneratorAdapter {

	/**
	 * This JET template is executed to generate the actual code.
	 */
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

		return Diagnostic.OK_INSTANCE;
	}

	private JETEmitter getClassBuilderTemplateJETEmitter() {
		return getJETEmitter(JET_EMITTER_DESCRIPTORS, 0);
	}

}
