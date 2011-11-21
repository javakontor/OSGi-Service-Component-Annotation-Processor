package org.javakontor.ds.apt.exporter;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.javakontor.ds.eclipse.DsResourceListener;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import aQute.bnd.annotation.metatype.Meta;
import aQute.bnd.annotation.metatype.Meta.AD;
import aQute.bnd.annotation.metatype.Meta.OCD;

public class MetatypeExporter {
  private static final String JAVA_LANG = "java.lang.";

  private org.jdom.Element    metaData  = null;

  private String              className = null;

  private String              componentPid;

  public MetatypeExporter(TypeElement mainClass, String componentPid, String designateClassName,
      ProcessingEnvironment processingEnv) {
    TypeElement designateType = getDesignateType(mainClass, designateClassName);
    className = mainClass.toString();
    this.componentPid = componentPid;

    if (designateType != null) {
      metaData = new org.jdom.Element("MetaData", Namespace.getNamespace("metatype",
          "http://www.osgi.org/xmlns/metatype/v1.1.0"));
      analyzeDesignateType(designateType);
      exportXml(processingEnv);
    }
  }

  private void analyzeConfigElement(Element element, AD ad, org.jdom.Element ocdElement) {
    org.jdom.Element adElement = new org.jdom.Element("AD");

    if (!ad.name().equals(Meta.NULL)) {
      adElement.setAttribute("name", ad.name());
    } else {
      adElement.setAttribute("name", element.getSimpleName().toString());
    }
    adElement.setAttribute("id", element.getSimpleName().toString());
    ExecutableElement executable = (ExecutableElement) element;
    TypeMirror typeMirror = executable.getReturnType();
    String typeName = typeMirror.toString();

    if (typeName.startsWith(JAVA_LANG)) {
      adElement.setAttribute("type", typeName.substring(JAVA_LANG.length()));
    } else if (typeName.equals("int")) {
      adElement.setAttribute("type", "Integer");
    } else if (typeName.equals("long")) {
      adElement.setAttribute("type", "Long");
    } else if (typeName.equals("char")) {
      adElement.setAttribute("type", "Char");
    } else if (typeName.equals("byte")) {
      adElement.setAttribute("type", "Byte");
    } else if (typeName.equals("double")) {
      adElement.setAttribute("type", "Double");
    } else if (typeName.equals("float")) {
      adElement.setAttribute("type", "Float");
    } else if (typeName.equals("short")) {
      adElement.setAttribute("type", "Short");
    } else if (typeName.equals("boolean")) {
      adElement.setAttribute("type", "Boolean");
    } else {
      adElement.setAttribute("type", "String");
      TypeKind kind = typeMirror.getKind();
      typeMirror.accept(new EnumVisitor(adElement), null);
      // System.out.println("Unbekannter Type für: " + element + ": " +
      // typeName);
    }
    if (!ad.description().equals(Meta.NULL)) {
      adElement.setAttribute("description", ad.description());
    }
    if (!ad.deflt().equals(Meta.NULL)) {
      adElement.setAttribute("default", ad.deflt());
    }
    if (!ad.min().equals(Meta.NULL)) {
      adElement.setAttribute("min", ad.min());
    }
    if (!ad.max().equals(Meta.NULL)) {
      adElement.setAttribute("max", ad.max());
    }
    if (ad.cardinality() > 0) {
      adElement.setAttribute("cardinality", "" + ad.cardinality());
    }
    if (ad.required()) {
      adElement.setAttribute("required", "true");
    }

    ocdElement.getContent().add(adElement);
  }

  private void analyzeDesignateType(TypeElement designateType) {
    OCD ocd = designateType.getAnnotation(OCD.class);
    org.jdom.Element ocdElement = new org.jdom.Element("OCD");
    if (!ocd.name().equals(Meta.NULL)) {
      ocdElement.setAttribute("name", ocd.name());
    } else {
      ocdElement.setAttribute("name", designateType.toString());
    }
    ocdElement.setAttribute("id", className);

    // if( !ocd.id().equals(Meta.NULL)) {
    // ocdElement.setAttribute("id", ocd.id() );
    // }
    if (!ocd.description().equals(Meta.NULL)) {
      ocdElement.setAttribute("description", ocd.description());
    }
    metaData.getContent().add(ocdElement);

    for (Element element : designateType.getEnclosedElements()) {
      AD ad = element.getAnnotation(AD.class);
      if (ad != null) {
        analyzeConfigElement(element, ad, ocdElement);
      }
    }
    org.jdom.Element designateElement = new org.jdom.Element("Designate");
    designateElement.setAttribute("pid", componentPid);
    org.jdom.Element designateObjectElement = new org.jdom.Element("Object");
    designateObjectElement.setAttribute("ocdref", componentPid);
    designateElement.getContent().add(designateObjectElement);
    metaData.getContent().add(designateElement);

  }

  private void exportXml(ProcessingEnvironment processingEnv) {
    Document doc = new Document();
    doc.addContent(new Comment("Generated by MetatypeExporter - Do not edit"));
    doc.setRootElement(metaData);

    XMLOutputter fmt = new XMLOutputter();
    Format format = Format.getPrettyFormat();
    format.setIndent("    ");
    fmt.setFormat(format);

    OutputStream os = null;
    try {
      String fileName = "metatype/" + className + ".xml";

      DsResourceListener.get().addObservedResource(fileName);
      final FileObject fo = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", fileName);
      os = fo.openOutputStream();

      fmt.output(doc, os);
      processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "MetatypeExporter erzeugt: " + fo.toUri());

    } catch (Throwable e1) {

      // TODO Auto-generated catch block
      e1.printStackTrace();
    } finally {
      try {
        os.close();
      } catch (IOException e) {

        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private TypeElement getDesignateType(TypeElement mainClass, String designateClassName) {
    if (designateClassName != null) {
      for (Element subElement : mainClass.getEnclosedElements()) {
        System.out.println("Suche DesignateType SubElement: " + subElement.toString());
        if (subElement.toString().equals(designateClassName)) {
          return (TypeElement) subElement;
        }
      }
    }
    return null;
  }

  class EnumVisitor extends SimpleTypeVisitor6<Void, Void> {

    private org.jdom.Element adElement;

    public EnumVisitor(org.jdom.Element adElement) {
      this.adElement = adElement;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Void arg1) {
      System.out.println("visitDeclared: " + t);
      Element element = t.asElement();
      if (element.getKind() == ElementKind.ENUM) {
        System.out.println("Enum: " + element);
        adElement.setAttribute("cardinality", "0");

        for (Element subElement : element.getEnclosedElements()) {
          if (subElement.getKind() == ElementKind.ENUM_CONSTANT) {
            System.out.println("Enum-Constant: " + subElement);
            org.jdom.Element option = new org.jdom.Element("Option");
            option.setAttribute("label", subElement.toString());
            option.setAttribute("value", subElement.toString());
            adElement.getContent().add(option);
          }
        }
      }
      return null;
    }
  }

}
