package domain

case class User(var name: String, var balance: Double, var IsLoggedIn: Boolean = false)
