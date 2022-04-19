/*
 * © 2021. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.simona.api.sample;

import edu.ie3.simona.api.data.ev.model.EvModel;
import edu.ie3.util.quantities.PowerSystemUnits;
import java.util.UUID;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Power;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

public class EvModelImpl implements EvModel {
  private final UUID uuid;
  private final String id;
  private final ComparableQuantity<Power> sRatedAC;
  private final ComparableQuantity<Power> sRatedDC;
  private final ComparableQuantity<Energy> eStorage;
  private final ComparableQuantity<Energy> storedEnergy;

  public EvModelImpl(
      UUID uuid,
      String id,
      ComparableQuantity<Power> sRatedAC,
      ComparableQuantity<Power> sRatedDC,
      ComparableQuantity<Energy> eStorage,
      ComparableQuantity<Energy> storedEnergy) {
    this.uuid = uuid;
    this.id = id;
    this.sRatedAC = sRatedAC;
    this.sRatedDC = sRatedDC;
    this.eStorage = eStorage;
    this.storedEnergy = storedEnergy;
  }

  public EvModelImpl(
      UUID uuid,
      String id,
      ComparableQuantity<Power> sRatedAC,
      ComparableQuantity<Power> sRatedDC,
      ComparableQuantity<Energy> eStorage) {
    this.uuid = uuid;
    this.id = id;
    this.sRatedAC = sRatedAC;
    this.sRatedDC = sRatedDC;
    this.eStorage = eStorage;
    this.storedEnergy = Quantities.getQuantity(0d, PowerSystemUnits.KILOWATTHOUR);
  }

  @Override
  public UUID getUuid() {
    return uuid;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public ComparableQuantity<Power> getSRatedAC() {
    return sRatedAC;
  }

  @Override
  public ComparableQuantity<Power> getSRatedDC() {
    return sRatedDC;
  }

  @Override
  public ComparableQuantity<Energy> getEStorage() {
    return eStorage;
  }

  @Override
  public ComparableQuantity<Energy> getStoredEnergy() {
    return storedEnergy;
  }

  @Override
  public Long getDepartureTick() {
    return null;
  }

  @Override
  public EvModelImpl copyWith(ComparableQuantity<Energy> newStoredEnergy) {
    return new EvModelImpl(uuid, id, sRatedAC, sRatedDC, eStorage, newStoredEnergy);
  }
}
