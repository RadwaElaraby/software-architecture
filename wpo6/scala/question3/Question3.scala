package question3
import java.nio.file.{Path, Paths}

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, FlowShape, Graph, IOResult, OverflowStrategy}
import akka.stream.scaladsl.{Broadcast, FileIO, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.util.ByteString
import java.nio.file.StandardOpenOption._

import akka.io.Tcp.SO.KeepAlive
import question2.Question2.{distanceInKM, pathCSVFile}
import question2.{LocationClass, RideCovered, StationClass}

import scala.concurrent.{ExecutionContextExecutor, Future}

case class Ride(id: String,
                rideable_type: String,
                start_date: String,
                end_date: String,
                start_station: Station,
                end_station: Station,
                start_location: Location,
                end_location: Location,
                member: String)

case class Station(id: Int, name: String)

case class Location(latitude: Double, longitude: Double)

object Question3 extends App {

  implicit val actorSystem: ActorSystem = ActorSystem("Question3")
  implicit val dispatcher: ExecutionContextExecutor = actorSystem.dispatcher
  implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()

  val resourcesFolder: String = "src/main/resources"
  val pathCSVFile: Path = Paths.get(s"$resourcesFolder/Divvy_Trips_2020_Q1.csv")
  val outputFile: Path = Paths.get(s"$resourcesFolder/results/results.csv")

  val source: Source[ByteString, Future[IOResult]] = FileIO.fromPath(pathCSVFile)

  // Parse the file with Alpakka
  val csvParsing: Flow[ByteString, List[ByteString], NotUsed] = CsvParsing.lineScanner()

  // Map the values with the header of the CSV file
  val mappingHeader: Flow[List[ByteString], Map[String, ByteString], NotUsed] = CsvToMap.toMap()


  // Objectify the values
  val flowRide: Flow[Map[String, ByteString], RideCovered, NotUsed] = Flow[Map[String, ByteString]].map(m => {
    m.map(e => {
      (e._1, e._2.utf8String)
    })
  })
  .filter(i => i.size == (i.values.filter(_.nonEmpty)).size)
  .map(i => {
    RideCovered(
      i("ride_id"),
      i("rideable_type"),
      i("started_at"),
      i("ended_at"),
      StationClass(id = i("start_station_id").toInt, name = i("start_station_name")),
      StationClass(id = i("end_station_id").toInt, name = i("end_station_name")),
      distanceInKM(
        LocationClass(latitude = i("start_lat").toDouble, longitude = i("start_lng").toDouble),
        LocationClass(latitude = i("end_lat").toDouble, longitude = i("end_lng").toDouble)
      ),
      i("member_casual")
    )
  })

  // Implement a custom FlowShape for this Flow using the GraphDSL
  val flowSelectedStations: Graph[FlowShape[RideCovered, RideCovered], NotUsed] = Flow.fromGraph(
    GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val broadcaster = builder.add(Broadcast[RideCovered](4))
      val merger = builder.add(Merge[RideCovered](4))

      val failBuffer = Flow[RideCovered].buffer(1, OverflowStrategy.fail)
      val backPressureBuffer = Flow[RideCovered].buffer(1, OverflowStrategy.backpressure)
      val dropHeadBuffer = Flow[RideCovered].buffer(1, OverflowStrategy.dropHead)
      val dropTailBuffer = Flow[RideCovered].buffer(1, OverflowStrategy.dropTail)

      val stations96And239Filter : Flow[RideCovered, RideCovered, NotUsed] = Flow[RideCovered].filter(a => List(96, 239).contains(a.start_station.id) && List(96, 239).contains(a.end_station.id))
      val stations234And110Filter : Flow[RideCovered, RideCovered, NotUsed] = Flow[RideCovered].filter(a => List(234, 110).contains(a.start_station.id) && List(234, 110).contains(a.end_station.id))
      val onlyCasualMemFilter: Flow[RideCovered, RideCovered, NotUsed] = Flow[RideCovered].filter(a => a.member == "casual")

      broadcaster.out(0) ~> failBuffer ~> stations96And239Filter ~> merger.in(0)
      broadcaster.out(1) ~> backPressureBuffer ~> stations96And239Filter ~> merger.in(1)
      broadcaster.out(2) ~> dropHeadBuffer ~> stations234And110Filter ~> onlyCasualMemFilter ~> merger.in(2)
      broadcaster.out(3) ~> dropTailBuffer ~> stations234And110Filter ~> onlyCasualMemFilter ~> merger.in(3)

      FlowShape(broadcaster.in, merger.out)
    }
  )

  val sink: Sink[RideCovered, NotUsed] = Flow[RideCovered].map(i => ByteString(i + "\n")).to(FileIO.toPath(outputFile))

  val runnableGraph: RunnableGraph[Future[IOResult]] = source
    .via(csvParsing)
    .via(mappingHeader)
    .via(flowRide)
    .via(flowSelectedStations)
    .to(sink)

  runnableGraph.run().foreach(_ => actorSystem.terminate())
}
