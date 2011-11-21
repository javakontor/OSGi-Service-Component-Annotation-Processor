package org.javakontor.ds.eclipse;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DsActivator implements BundleActivator {

  @Override
  public void start(BundleContext context) throws Exception {
    System.out.println("DsActivator.start");
    final DsResourceListener dsResourceListener = DsResourceListener.get();

    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IProject[] projects = workspace.getRoot().getProjects();
    for (IProject project : projects) {
      IFolder osgiInfo = project.getFolder("OSGI-INF");
      if ((osgiInfo != null) && osgiInfo.exists()) {
        osgiInfo.accept(new IResourceVisitor() {

          @Override
          public boolean visit(IResource resource) throws CoreException {
            if (resource.getName().endsWith(".xml")) {
              dsResourceListener.addObservedResource(resource.getName());
            }
            return true;
          }
        });
      }
    }
    ResourcesPlugin.getWorkspace().addResourceChangeListener(dsResourceListener,
        IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
  }

}
