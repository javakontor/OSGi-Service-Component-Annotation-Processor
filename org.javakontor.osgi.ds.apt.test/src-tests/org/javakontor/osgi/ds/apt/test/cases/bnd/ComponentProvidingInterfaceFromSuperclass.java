package org.javakontor.osgi.ds.apt.test.cases.bnd;

import java.util.Hashtable;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Component(provide = Map.class)
public class ComponentProvidingInterfaceFromSuperclass extends Hashtable<Object, Object> {

  public class Toto {

  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Activate
  protected void start(Map<String, Object> properties) {

  }
}
