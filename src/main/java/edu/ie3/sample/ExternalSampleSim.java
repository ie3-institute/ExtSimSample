/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.sample;

import ch.qos.logback.classic.Logger;
import edu.ie3.simona.api.data.ev.ExtEvData;
import edu.ie3.simona.api.data.ev.ExtEvSimulation;
import edu.ie3.simona.api.data.ev.model.EvModel;
import edu.ie3.simona.api.simulation.ExtSimulation;
import edu.ie3.util.quantities.PowerSystemUnits;
import java.util.*;
import org.slf4j.LoggerFactory;
import tech.units.indriya.quantity.Quantities;

/** Example simulation that keeps swapping two evs between two evcs */
public class ExternalSampleSim extends ExtSimulation implements ExtEvSimulation {

  private ExtEvData evData;

  private final UUID evcs1 = UUID.fromString("06a14909-366e-4e94-a593-1016e1455b30");
  private final UUID evcs2 = UUID.fromString("104acdaa-5dc5-4197-aed2-2fddb3c4f237");

  private final Logger log = (Logger) LoggerFactory.getLogger("ExternalSampleSim");

  // initialize with
  private EvModelImpl evA =
      new EvModelImpl(
          UUID.fromString("73c041c7-68e9-470e-8ca2-21fd7dbd1797"),
          "carA",
          Quantities.getQuantity(11d, PowerSystemUnits.KILOWATT),
          Quantities.getQuantity(11d, PowerSystemUnits.KILOWATT),
          Quantities.getQuantity(58d, PowerSystemUnits.KILOWATTHOUR));

  private EvModelImpl evB =
      new EvModelImpl(
          UUID.fromString("6d7d27a1-5cbb-4b73-aecb-dfcc5a6fb22e"),
          "carB",
          Quantities.getQuantity(11d, PowerSystemUnits.KILOWATT),
          Quantities.getQuantity(11d, PowerSystemUnits.KILOWATT),
          Quantities.getQuantity(80d, PowerSystemUnits.KILOWATTHOUR));

  @Override
  public void setExtEvData(ExtEvData evData) {
    this.evData = evData;
  }

  @Override
  protected Optional<Long> initialize() {
    log.info("Main args handed over to external simulation: {}", Arrays.toString(getMainArgs()));

    return Optional.of(0L);
  }

  @Override
  protected Optional<Long> doActivity(long tick) {
    try {
      log.info("External simulation: Tick {} has been triggered.", tick);

      final Map<UUID, Integer> availableEvcs;
      availableEvcs = evData.requestAvailablePublicEvcs();

      log.debug("Avaiable evcs: {}", availableEvcs);

      final int phase = (int) ((tick / 900) % 4);

      Map<UUID, List<EvModel>> arrivals = new HashMap<>();
      Map<UUID, List<UUID>> departures = new HashMap<>();

      switch (phase) {
        case 0 -> {
          arrivals.put(evcs1, List.of(evA));
          arrivals.put(evcs2, List.of(evB));
        }
        case 1 -> {
          departures.put(evcs1, List.of(evA.getUuid()));
          departures.put(evcs2, List.of(evB.getUuid()));
        }
        case 2 -> {
          arrivals.put(evcs1, List.of(evB));
          arrivals.put(evcs2, List.of(evA));
        }
        case 3 -> {
          departures.put(evcs1, List.of(evB.getUuid()));
          departures.put(evcs2, List.of(evA.getUuid()));
        }
        default -> throw new RuntimeException("Unknown phase");
      }

      if (!departures.isEmpty()) {
        List<EvModel> departedEvs = evData.requestDepartingEvs(departures);

        log.debug("Received departed evs from SIMONA: {}", departedEvs);

        // store returned charging level
        for (EvModel departed : departedEvs) {
          if (departed.getUuid() == evA.getUuid()) evA = evA.copyWith(departed.getStoredEnergy());
          else if (departed.getUuid() == evB.getUuid())
            evB = evB.copyWith(departed.getStoredEnergy());
        }
      }

      if (!arrivals.isEmpty()) {
        log.debug("Sending arrivals to SIMONA: {}", arrivals);

        evData.provideArrivingEvs(arrivals);
      }

      Long newTick = tick + 900;
      // return triggers activity complete automatically
      log.info("Sending next tick to SIMONA: {}", newTick);
      return Optional.of(newTick);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
