package domain

trait UserTrait {
  def create(name: String, balance: Double): User
  def login(user: User): Unit
  def logout(user: User): Unit
  def decreaseBalance(user: User, amount: Double): Unit
  def increaseBalance(user: User, amount: Double): Unit
}
