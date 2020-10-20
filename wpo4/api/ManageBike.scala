package api

import domain.{Bike, BikeTrait, User, UserTrait}
import com.google.inject.Inject

case class ManageBike @Inject()(bikeRepository: BikeTrait, userRepository: UserTrait) extends ManageBikeTrait {

  def rent(user: User, bikeType: String): Bike = {
    var bike = bikeRepository.create(bikeType);
    if (!bike.isFree)
      return null
    userRepository.decreaseBalance(user, bike.price)
    bikeRepository.rent(bike);
    bike;
  }

  def canRent(user: User, bikeType: String): Boolean = {
    val bike = bikeRepository.create(bikeType)
    if (user.balance >= bike.price) true else false
  }

  def returnBack(user: User, bike: Bike): Unit = {
    userRepository.increaseBalance(user, bike.price)
    bikeRepository.returnBack(bike)
  }
}