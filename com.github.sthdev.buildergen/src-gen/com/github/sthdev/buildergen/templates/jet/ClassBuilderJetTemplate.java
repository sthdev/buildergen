package com.github.sthdev.buildergen.templates.jet;

import org.eclipse.emf.codegen.ecore.genmodel.*;
import com.github.sthdev.buildergen.GenClassBuilderTemplateUtil;

public class ClassBuilderJetTemplate
{
  protected static String nl;
  public static synchronized ClassBuilderJetTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ClassBuilderJetTemplate result = new ClassBuilderJetTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL + NL + "/**" + NL + " * Builder for {@link ";
  protected final String TEXT_4 = "}." + NL + " *" + NL + " * @generated" + NL + " */" + NL + "public class ";
  protected final String TEXT_5 = " {" + NL + "" + NL + "\t/**" + NL + "\t * Creates a new builder for {@link ";
  protected final String TEXT_6 = "}." + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_7 = "() {" + NL + "\t\t// Does nothing by default." + NL + "\t}" + NL + "\t\t" + NL + "\t/**" + NL + "\t * Builds a new instance of {@link ";
  protected final String TEXT_8 = "}. The builder can be reused" + NL + "\t * multiple times to create additional instances with the same feature values." + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_9 = " build() {" + NL + "\t\t";
  protected final String TEXT_10 = " object = ";
  protected final String TEXT_11 = ".eINSTANCE.create";
  protected final String TEXT_12 = "();" + NL + "\t\t" + NL + "\t\treturn object;" + NL + "\t}" + NL + "}";
  protected final String TEXT_13 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	GenClass genClass = (GenClass) argument;
	GenPackage genPackage = genClass.getGenPackage();
	GenModel genModel = genClass.getGenModel();
	String builderPackageName = GenClassBuilderTemplateUtil.getBuilderPackageName(genPackage);
	String builderClassName = GenClassBuilderTemplateUtil.getBuilderClassName(genClass);

    stringBuffer.append(TEXT_1);
    stringBuffer.append(builderPackageName);
    stringBuffer.append(TEXT_2);
    genModel.markImportLocation(stringBuffer);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(genClass.getImportedInterfaceName());
    stringBuffer.append(TEXT_4);
    stringBuffer.append(builderClassName);
    stringBuffer.append(TEXT_5);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_6);
    stringBuffer.append(builderClassName);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_8);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_9);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_10);
    stringBuffer.append(genPackage.getImportedFactoryInterfaceName());
    stringBuffer.append(TEXT_11);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_12);
    genModel.emitSortedImports();
    stringBuffer.append(TEXT_13);
    return stringBuffer.toString();
  }
}
