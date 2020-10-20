package infrastructure

import java.util.Dictionary

import api.ApiBinding
import com.google.inject.Guice
import domain.{Bike, DomainBinding}

import scala.io.StdIn

object UI
{
    def main(args: Array[String]): Unit = {
        val injector = Guice.createInjector(new ApiBinding, new DomainBinding)
        val infra = injector.getInstance(classOf[Infrastructure])
        val manageUser = infra.manageUser
        val manageBike = infra.manageBike

        println("What is your name?")
        val userName = StdIn.readLine()

        println("How much is your balance?")
        val userBalance = StdIn.readDouble()

        val user = manageUser.create(userName, userBalance)
        println("Your Balance before rent: " + user.balance)

        var rentedBikes: Map[Int, Bike] = Map()

        var DoAnotherOperation = 'y'
        var action: Int = 0
        while(DoAnotherOperation == 'y')
        {
            do
            {
                println("----------------------------------")
                println("What do you want to do?")
                println("1. rent normal bike $10")
                println("2. rent road bike $20")
                println("3. rent electric bike $30")
                if (rentedBikes.nonEmpty)
                    println("4. return bike")

                action = StdIn.readInt()
            }
            while(!Set[Int] (1,2,3,4).contains(action))

            if (action == 4)
            {
                println("Specify Bike Id:")
                rentedBikes.foreach(p => println("#"+p._1+": $"+ p._2.price))
                val bikeId = StdIn.readInt()
                manageBike.returnBack(user, rentedBikes(bikeId))
                rentedBikes -= bikeId
                println("You returned bike Id #" + bikeId)
                println("Your Current Balance: " + user.balance)
            }
            else
            {
                val bikeType = if (action == 1) "normal" else if (action == 2) "road" else "electric"
                if (manageBike.canRent(user, bikeType))
                {
                    val newBikeId: Int = rentedBikes.count(b => true) + 1
                    val newBike = manageBike.rent(user, bikeType)
                    rentedBikes += (newBikeId -> newBike)
                    println("You rented a " + bikeType + " bike Id #" + newBikeId)
                    println("Your Current Balance: " + user.balance)
                }
                else
                    println("Sorry! Your balance is less than required")
            }

            println("do you want to do another operation? (y/n)")
            DoAnotherOperation = scala.io.StdIn.readChar()
        }



    }
}