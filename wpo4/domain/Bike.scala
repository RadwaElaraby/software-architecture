package domain

sealed abstract class Bike(var isFree: Boolean = true, var price: Double = 0)

case class NormalBike(var color: String = "grey") extends Bike
{
  price = 10
}
case class RoadBike(var color: String = "black") extends Bike
{
  price = 20
}
case class ElectricBike(var color: String = "brown") extends Bike
{
  price = 30
}
