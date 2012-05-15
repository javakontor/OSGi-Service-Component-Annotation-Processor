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

import org.junit.Test;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class OSGiScannerTest extends AbstractAptTest {

  @Test
  public void test_ComponentWithoutService() throws Exception {
    compile("osgi/ComponentWithoutService");

    assertDsXmlEquals("osgi.ComponentWithoutService");
  }

  @Test
  public void test_ComponentProvidingInterfaceFromSuperclass() throws Exception {
    compile("osgi/ComponentProvidingInterfaceFromSuperclass");

    assertDsXmlEquals("osgi.ComponentProvidingInterfaceFromSuperclass");
  }

  @Test
  public void test_ComponentNotEnabled() throws Exception {
    compile("osgi/ComponentNotEnabled");

    assertDsXmlEquals("osgi.ComponentNotEnabled");
  }

}
