package domain

class UserRepository extends UserTrait {
  def create(name: String, balance: Double): User = {
    return new User(name, balance, false);
  }
  def login(user: User): Unit = {
    user.IsLoggedIn = true;
  }
  def logout(user: User): Unit = {
    user.IsLoggedIn = false;
  }
  def decreaseBalance(user: User, amount: Double): Unit = user.balance -= amount;
  def increaseBalance(user: User, amount: Double): Unit = user.balance += amount;
}