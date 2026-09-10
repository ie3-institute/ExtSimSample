/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.sample;

import edu.ie3.simona.api.ExtSimulationProvider;
import edu.ie3.simona.api.data.SetupData;
import edu.ie3.simona.api.simulation.ExtSimulation;

public class ExtLink implements ExtSimulationProvider {
  private final ExternalSampleSim sampleSim = new ExternalSampleSim();

  @Override
  public ExtSimulation getExtSimulation() {
    return sampleSim;
  }

  @Override
  public void setup(SetupData setupData) {

  }
}
