package domain

class BikeRepository {
  def create(bikeType: String): Bike =
  {
    if (bikeType == "electric")
      new ElectricBike()
    else if (bikeType == "road")
      new RoadBike()
    else
      new  NormalBike()
  }
  def rent(bike: Bike): Unit = {
    bike.isFree = false;
  }
  def returnBack(bike: Bike): Unit = {
    bike.isFree = true;
  }
}
