package api

import domain.{Bike, BikeRepository, Domain, User, UserRepository}

case class ManageBike(bikeRepository: BikeRepository, userRepository: UserRepository) {

  def rent(user: User, bikeType: String): Bike = {
    var bike = bikeRepository.create(bikeType);
    if (!bike.isFree)
      return null
    userRepository.decreaseBalance(user, bike.price)
    bikeRepository.rent(bike);
    bike;
  }
  def returnBack(user: User, bike: Bike): Unit = {
    userRepository.increaseBalance(user, bike.price)
    bikeRepository.returnBack(bike)
  }
}