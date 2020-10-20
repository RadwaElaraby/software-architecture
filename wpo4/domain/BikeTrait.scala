package domain

trait BikeTrait {
  def create(bikeType: String): Bike
  def rent(bike: Bike): Unit
  def returnBack(bike: Bike): Unit
}
