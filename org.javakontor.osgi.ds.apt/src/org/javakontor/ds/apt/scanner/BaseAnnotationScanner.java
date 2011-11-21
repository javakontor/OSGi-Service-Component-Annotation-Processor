package org.javakontor.ds.apt.scanner;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner6;

import org.javakontor.ds.apt.exporter.DsXmlExporter;

/**
 * Provides common functionality for both OSGi- and bnd-Annotations scanner
 * 
 * 
 */
public class BaseAnnotationScanner extends ElementScanner6<Void, Void> {
  protected DsXmlExporter         dsXmlExporter;

  protected boolean               errorsFound = false;

  protected ProcessingEnvironment processingEnv;

  public BaseAnnotationScanner(ProcessingEnvironment processingEnv) {
    dsXmlExporter = new DsXmlExporter();
    this.processingEnv = processingEnv;
  }

  public void exportXml() {
    if (!errorsFound) {
      dsXmlExporter.exportXml(processingEnv);
    }
  }

  protected String getActivateMethod(TypeElement e, Class activateAnnotation) {
    for (Element element : e.getEnclosedElements()) {
      if (element.getAnnotation(activateAnnotation) != null) {
        return element.getSimpleName().toString();
      }
    }
    return null;
  }

  protected String getDeactivateMethod(TypeElement e, Class deactivateAnnotation) {
    for (Element element : e.getEnclosedElements()) {
      if (element.getAnnotation(deactivateAnnotation) != null) {
        return element.getSimpleName().toString();
      }
    }
    return null;
  }

  protected Element getUnbindMethod(ExecutableElement bindMethod, String unbindNameFromReference) {
    String bindName = bindMethod.getSimpleName().toString();
    String unbindName = "";
    if (bindName.startsWith("add")) {
      unbindName = "remove" + bindName.substring(3);
    } else {
      unbindName = "un" + bindName;
    }
    if (unbindNameFromReference.length() > 0) {
      unbindName = unbindNameFromReference;
    }
    for (Element element : bindMethod.getEnclosingElement().getEnclosedElements()) {
      if (element.getSimpleName().toString().equals(unbindName)) {
        return element;
      }
    }
    return null;
  }

}
