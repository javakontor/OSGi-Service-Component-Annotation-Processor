/*******************************************************************************
 * Copyright (c) 2011 Nils Hartmann
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nils Hartmann - initial API and implementation
 ******************************************************************************/
package org.javakontor.osgi.ds.apt.test;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.javakontor.ds.apt.DsAnnotationProcessor;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class AbstractAptTest extends XMLTestCase {

  /**
   * Compiles the given class
   * 
   * <p>
   * The class is compiled to the {@link #getOutputDir() output directory}. During compilation the
   * {@link DsAnnotationProcessor} is run.
   * 
   * @param unqualifiedClassName
   */
  protected void compile(String unqualifiedClassName) {

    // get source file for class
    File file = getSourceFile(unqualifiedClassName);

    try {
      // get the compiler
      // (see http://download.oracle.com/javase/6/docs/api/javax/tools/JavaCompiler.html)
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
      Iterable<? extends JavaFileObject> compilationUnits = fileManager
          .getJavaFileObjectsFromFiles(Arrays.asList(file));

      // Set destination directory
      List<String> options = Arrays.asList("-d", getOutputDir().getAbsolutePath());

      // Create the CompilationTaks
      CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);

      // set the DsAnnotationProcessor
      task.setProcessors(Arrays.asList(new DsAnnotationProcessor()));

      // run the compilation
      if (!task.call()) {
        throw new RuntimeException("Compilation of " + file + " was not successful");
      }

    } catch (Exception ex) {
      throw new RuntimeException("Could not compile: " + ex, ex);
    }

  }

  /**
   * @param unqualifiedClassName
   */
  public void assertDsXmlEquals(String unqualifiedClassName) throws Exception {
    File expectedDsFile = getDsFile(false, unqualifiedClassName);
    File actualDsFile = getDsFile(true, unqualifiedClassName);
    Diff diff = new Diff(new FileReader(expectedDsFile), new FileReader(actualDsFile));
    XMLUnit.setIgnoreWhitespace(true);

    assertTrue(diff.toString(), diff.identical());
  }

  protected File getDsFile(boolean actuallyGenerated, String unqualifiedClassName) {

    // get qualified name: packageName + unqualifiedClassName
    String qualifiedXmlName = getClass().getPackage().getName() + ".cases." + unqualifiedClassName;

    File dsFile = null;

    if (actuallyGenerated) {
      // return the generated XML file from output dir
      String relativeXmlFileName = qualifiedXmlName + ".xml";

      dsFile = new File(getOutputDir(), relativeXmlFileName);

    } else {
      // return the expected XML file from the src-tests folder
      String relativeXmlFileName = qualifiedXmlName.replace('.', '/') + ".xml";

      dsFile = new File(getSrcDir(), relativeXmlFileName);
    }

    if (!dsFile.isFile()) {
      throw new RuntimeException("DS-File '" + dsFile + "' does not exists");
    }

    return dsFile;

  }

  /**
   * Returns a {@link File} pointing to the java-File for the specified class
   * 
   * @param unqualifiedClassName
   * @return
   */
  protected File getSourceFile(String unqualifiedClassName) {

    // get qualified classname: packageName + unqualifiedClassName
    String qualifiedClassName = getClass().getPackage().getName() + ".cases." + unqualifiedClassName;

    // get relative name of class file
    String relativeClassFileName = qualifiedClassName.replace('.', '/') + ".java";

    return new File(getSrcDir(), relativeClassFileName);

  }

  /**
   * Returns the project directory, i.e the root of the source- and output directories
   * 
   * @return
   */
  protected File getProjectDir() {
    String userDir = System.getProperty("user.dir");
    // File projectDir = new File(
    // "/Users/nils/develop/p2/git-repositories/OSGi-Service-Component-Annotation-Processor/org.javakontor.osgi.ds.apt.test");
    File projectDir = new File(userDir);
    if (!projectDir.isDirectory()) {
      throw new RuntimeException("ProjectDir '" + projectDir + "' is not an existing directory");
    }
    return projectDir;
  }

  /**
   * Returns the source directory that contains the test classes
   * 
   * @return
   */
  protected File getSrcDir() {
    return new File(getProjectDir(), "src-tests");
  }

  /**
   * Returns the (exisiting) output dir
   * 
   * @return
   */
  protected File getOutputDir() {
    File outputDir = new File(getProjectDir(), "output");
    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    if (!outputDir.isDirectory()) {
      throw new RuntimeException(String.format("Output directory '%s' is not an existing directory", outputDir));
    }

    return outputDir;

  }
}
