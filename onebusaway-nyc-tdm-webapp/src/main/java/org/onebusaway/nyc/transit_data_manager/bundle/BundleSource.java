package org.onebusaway.nyc.transit_data_manager.bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.onebusaway.nyc.transit_data_manager.bundle.model.Bundle;

public interface BundleSource {
  /**
   * Get all the bundles that seem initially correct.
   * @return A list of the available bundle objects.
   */
  List<Bundle> getBundles();
  
  /**
   * Checks that the bundleId exists, and that the file is listed in the metadata.
   * @param bundleId The Id of the bundle to which the requested file belongs.
   * @param relativeFilePath the relative path of the file, as listed in the metadata.
   * @return true if the bundle exists and file is listed in the metadata. false otherwise. 
   */
  boolean checkIsValidBundleFile(String bundleId, String relativeFilePath);
  
  /**
   * Get the full path of a bundle file.
   * @param bundleId
   * @param relativeFilePath
   * @return
   */
  File getBundleFile(String bundleId, String relativeFilePath) throws FileNotFoundException;
}
