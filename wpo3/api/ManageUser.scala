package api

import domain.{Domain, User, UserRepository}

case class ManageUser(userRepository: UserRepository) {
  def create(name: String, balance: Double): User = {
    return userRepository.create(name, balance);
  }
  def login(user: User): Unit = {
    userRepository.login(user)
  }
  def logout(user: User): Unit = {
    userRepository.logout(user)
  }
  def reduceBalance(user: User, amount: Double): Unit = {
    user.balance -= amount
  }
  def increaseBalance(user: User, amount: Double): Unit = {
    user.balance += amount
  }
}