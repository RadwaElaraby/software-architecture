package api

import domain.User

trait ManageUserTrait {
  def create(name: String, balance: Double): User
  def login(user: User): Unit
  def logout(user: User): Unit
  def reduceBalance(user: User, amount: Double): Unit
  def increaseBalance(user: User, amount: Double): Unit
}
