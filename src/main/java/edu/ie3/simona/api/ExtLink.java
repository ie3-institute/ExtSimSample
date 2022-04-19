/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.simona.api;

import edu.ie3.simona.api.data.ExtDataSimulation;
import edu.ie3.simona.api.sample.ExternalSampleSim;
import edu.ie3.simona.api.simulation.ExtSimulation;
import java.util.ArrayList;
import java.util.List;

public class ExtLink implements ExtLinkInterface {
  private final ExternalSampleSim sampleSim = new ExternalSampleSim();

  @Override
  public ExtSimulation getExtSimulation() {
    return sampleSim;
  }

  @Override
  public List<ExtDataSimulation> getExtDataSimulations() {
    ArrayList<ExtDataSimulation> list = new ArrayList<>();
    list.add(sampleSim);
    return list;
  }
}
