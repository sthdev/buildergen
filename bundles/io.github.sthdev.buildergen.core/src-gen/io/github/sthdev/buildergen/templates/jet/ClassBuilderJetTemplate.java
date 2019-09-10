package io.github.sthdev.buildergen.templates.jet;

import org.eclipse.emf.codegen.ecore.genmodel.*;
import io.github.sthdev.buildergen.templates.ClassBuilderTemplate;

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
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    // Redirect to the Xtend template class for the actual code generation because there is not much tool support for editing JET templates.
    stringBuffer.append(TEXT_1);
    stringBuffer.append(ClassBuilderTemplate.generate((GenClass) argument));
    stringBuffer.append(TEXT_2);
    return stringBuffer.toString();
  }
}
