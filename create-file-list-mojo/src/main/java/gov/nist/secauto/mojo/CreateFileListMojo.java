/**
 * Portions of this software was developed by employees of the National Institute
 * of Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 17 United States Code Section 105, works of NIST employees are
 * not subject to copyright protection in the United States and are considered to
 * be in the public domain. Permission to freely use, copy, modify, and distribute
 * this software and its documentation without fee is hereby granted, provided that
 * this notice and disclaimer of warranty appears in all copies.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS' WITHOUT ANY WARRANTY OF ANY KIND, EITHER
 * EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY
 * THAT THE SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND FREEDOM FROM
 * INFRINGEMENT, AND ANY WARRANTY THAT THE DOCUMENTATION WILL CONFORM TO THE
 * SOFTWARE, OR ANY WARRANTY THAT THE SOFTWARE WILL BE ERROR FREE. IN NO EVENT
 * SHALL NIST BE LIABLE FOR ANY DAMAGES, INCLUDING, BUT NOT LIMITED TO, DIRECT,
 * INDIRECT, SPECIAL OR CONSEQUENTIAL DAMAGES, ARISING OUT OF, RESULTING FROM, OR
 * IN ANY WAY CONNECTED WITH THIS SOFTWARE, WHETHER OR NOT BASED UPON WARRANTY,
 * CONTRACT, TORT, OR OTHERWISE, WHETHER OR NOT INJURY WAS SUSTAINED BY PERSONS OR
 * PROPERTY OR OTHERWISE, AND WHETHER OR NOT LOSS WAS SUSTAINED FROM, OR AROSE OUT
 * OF THE RESULTS OF, OR USE OF, THE SOFTWARE OR SERVICES PROVIDED HEREUNDER.
 */

package gov.nist.secauto.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Create a file list by scanning a directory.
 * <p>
 * This class is based on code from
 * <a href="http://andreashohmann.com/articles/maven-source-file-list-plugin.html">an article</a> by
 * Andreas Hohmann.
 */
@Mojo(name = "create-list", defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE)
public class CreateFileListMojo extends AbstractMojo {
  /**
   * Location of the generated list of source files.
   */
  @Parameter(defaultValue = "${project.build.directory}/classes/file-list.txt", required = true)
  private File listFile;

  /**
   * @parameter expression="${create.resources-to-scan}"
   */
  @Parameter(defaultValue = "${create.resources-to-scan}", required = true)
  private List<Resource> resources;

  /**
   * Make the project context available.
   */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  /**
   * Execute the mojo.
   */
  public void execute() throws MojoExecutionException {
    final Log log = getLog();
    log.info("creating file list: '" + listFile.getAbsolutePath() + "'");
    BufferedWriter writer = null;
    try {
      listFile.getParentFile().mkdirs();
      writer = new BufferedWriter(new FileWriter(listFile));
    } catch (IOException e) {
      throw new MojoExecutionException("could not open list file destination '" + listFile + "'", e);
    }
    fillResources();
    scan(resources, writer);
    IOUtil.close(writer);
  }

  private void fillResources() {
    if (resources == null || resources.size() == 0) {
      resources = new LinkedList<Resource>();
      for (Object o : project.getResources()) {
        resources.add(new Resource((org.apache.maven.model.Resource) o));
      }
    }
  }

  /**
   * Scans a set of directories.
   *
   * @param roots
   *          Directories to scan
   * @param writer
   *          Where to write the source list
   * @throws MojoExecutionException
   *           propagated.
   */
  private void scan(List<Resource> roots, BufferedWriter writer) throws MojoExecutionException {
    for (Resource root : roots) {
      scan(root, writer);
    }
  }

  /**
   * Scans a single directory.
   *
   * @param resource
   *          Directory to scan
   * @param writer
   *          Where to write the source list
   * @throws MojoExecutionException
   *           in case of IO errors
   */
  private void scan(Resource resource, BufferedWriter writer) throws MojoExecutionException {
    final Log log = getLog();
    File root = new File(resource.getDirectory());
    if (!root.exists()) {
      return;
    }

    log.info("scanning source file directory '" + root + "'");
    log.debug("   with includes: " + resource.getIncludes().toString());
    log.debug("   with excludes: " + resource.getExcludes().toString());

    final DirectoryScanner directoryScanner = new DirectoryScanner();
    directoryScanner.setIncludes(resource.getIncludes().toArray(new String[0]));
    directoryScanner.setExcludes(resource.getExcludes().toArray(new String[0]));
    directoryScanner.setBasedir(root);
    directoryScanner.scan();

    log.debug("full scan path: " + directoryScanner.getBasedir().getAbsoluteFile());
    for (String fileName : directoryScanner.getIncludedFiles()) {
      try {
        fileName = fileName.replace('\\', '/');
        writer.write(fileName);
        writer.newLine();
      } catch (Exception e) {
        throw new MojoExecutionException("io error while writing source list", e);
      }
    }
  }

}