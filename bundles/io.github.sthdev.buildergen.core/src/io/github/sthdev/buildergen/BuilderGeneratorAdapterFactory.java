package io.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;

/**
 * This adapter factory is registered via the org.eclipse.emf.codegen.ecore.generatorAdapters
 * extension point so it is called by the EMF code generator. This factory creates the
 * {@link GenClassBuilderGeneratorAdapter} for {@link GenClass}es.
 *
 */
public class BuilderGeneratorAdapterFactory extends GenModelGeneratorAdapterFactory {

	@Override
	public Adapter createGenClassAdapter() {
		if (genClassGeneratorAdapter == null) {
			genClassGeneratorAdapter = new GenClassBuilderGeneratorAdapter(this);
		}

		return genClassGeneratorAdapter;
	}

}
