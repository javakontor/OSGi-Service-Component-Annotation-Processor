package org.javakontor.ds.eclipse;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;

public class DsResourceListener implements IResourceChangeListener {
  private static DsResourceListener singleton;

  public static synchronized DsResourceListener get() {
    if (singleton == null) {
      singleton = new DsResourceListener();
    }
    return singleton;
  }

  private Set<String> observedResources = new HashSet<String>();

  /**
   * Aus Performancegruenden werden nur bekannte Resourcen werden auch wieder geloescht.
   * 
   * @param observedResource
   */
  public void addObservedResource(String observedResource) {
    String className = observedResource.replace('.', '/').substring(0, observedResource.length() - 4) + ".class";
    observedResources.add(className);
  }

  private DsResourceListener() {

  }

  @Override
  public void resourceChanged(IResourceChangeEvent event) {
    if (event.getType() == IResourceChangeEvent.POST_CHANGE) {

      try {
        event.getDelta().accept(new DsResourceDeltaVisitor());
      } catch (CoreException e1) {
        e1.printStackTrace();
      }
    }
  }

  class DsResourceDeltaVisitor implements IResourceDeltaVisitor {
    private IProject project;

    public DsResourceDeltaVisitor() {
    }

    @Override
    public boolean visit(IResourceDelta delta) throws CoreException {
      switch (delta.getKind()) {

      case IResourceDelta.REMOVED:

        if ((project != null) && JavaProject.hasJavaNature(project)) {
          IJavaProject javaProject = JavaCore.create(project);
          IPath outputLocation = javaProject.getOutputLocation();
          if (outputLocation.isPrefixOf(delta.getResource().getFullPath())) {
            IPath relativePath = delta.getResource().getFullPath().makeRelativeTo(outputLocation);
            String className = relativePath.toString();
            boolean isObserverd = observedResources.contains(className);
            if (className.endsWith(".class") && isObserverd) {
              className = className.substring(0, className.length() - 6);
              String dsFilename = "OSGI-INF/" + className.replace('/', '.') + ".xml";
              IFile dsFile = project.getFile(dsFilename);

              if (dsFile.exists()) {
                DeleteDsWorkspaceJob dsWorkspaceJob = new DeleteDsWorkspaceJob(dsFile);
                dsWorkspaceJob.schedule();
              }
            }
          }
        }
        return true;

      case IResourceDelta.CHANGED:
        if (delta.getResource() instanceof IProject) {
          project = (IProject) delta.getResource();
        }

        break;
      }
      return true;
    }
  }

  class DeleteDsWorkspaceJob extends WorkspaceJob {

    private IFile dsFile;

    public DeleteDsWorkspaceJob(IFile dsFile) {
      super("DeleteDsWorkspaceRunnable");
      this.dsFile = dsFile;
    }

    @Override
    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
      if (dsFile.exists()) {
        dsFile.delete(true, monitor);
      }
      return Status.OK_STATUS;
    }
  }

}