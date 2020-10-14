package infrastructure

import api.Api
import domain.Domain

object UI extends Domain with Api with Infrastructure
{
    def main(args: Array[String]): Unit = {
        val user = manageUser.create("Ahmed", 200)
        println("User Balance before rent: " + user.balance)
        val bike = manageBike.rent(user, "normal")
        println("User Balance during rent: " + user.balance)
        manageBike.returnBack(user, bike)
        println("User Balance after return bike: " + user.balance)
    }
}