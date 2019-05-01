package com.github.sthdev.buildergen;

import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapterFactory;
import org.eclipse.emf.common.notify.Adapter;

public class BuilderGeneratorAdapterFactory extends GenModelGeneratorAdapterFactory {

	@Override
	public Adapter createGenClassAdapter() {
		if (genClassGeneratorAdapter == null) {
			genClassGeneratorAdapter = new GenClassBuilderGeneratorAdapter(this);
		}

		return genClassGeneratorAdapter;
	}

}
