/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.sample;

import edu.ie3.simona.api.ExtLinkInterface;
import edu.ie3.simona.api.simulation.ExtSimAdapterData;
import edu.ie3.simona.api.simulation.ExtSimulation;

public class ExtLink implements ExtLinkInterface {
  private ExternalSampleSim sampleSim;

  @Override
  public ExtSimulation getExtSimulation() {
    return sampleSim;
  }

  @Override
  public void setup(ExtSimAdapterData data) {
    sampleSim = new ExternalSampleSim("ExtSimSample");
  }
}
