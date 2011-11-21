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
package org.javakontor.osgi.ds.apt.test.cases;

import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Component
public class ComponentWithoutService {

  @Activate
  protected void activate(Map<String, Object> params) {

  }

}
