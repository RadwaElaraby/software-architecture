package api

import domain.{User, UserTrait}
import com.google.inject.Inject

case class ManageUser @Inject()(userRepository: UserTrait) extends ManageUserTrait {
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