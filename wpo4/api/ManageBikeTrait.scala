package api

import domain.{Bike, User}

trait ManageBikeTrait {
  def rent(user: User, bikeType: String): Bike
  def returnBack(user: User, bike: Bike): Unit
  def canRent(user: User, bikeType: String): Boolean
}
