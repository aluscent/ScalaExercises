package AdvancedScala.Exercise

object Variance {
  class Vehicle(id: Int) {
    override def toString: String = s"ID($id)"
  }
  class Bike(id: Int) extends Vehicle(id)
  class Car(id: Int) extends Vehicle(id)

  class CovariantPark[+T](vehicles: List[T]) {
    def park[B >: T](vehicle: B): CovariantPark[B] = new CovariantPark(vehicle :: vehicles)
    def impound[B >: T](vehicleList: List[B]): Unit = vehicleList.foreach(x => println(s"Vehicle $x impounded"))
    def unPark[B >: T](vehicle: B): CovariantPark[B] = new CovariantPark(vehicles.filter(x => x != vehicle))
    def checkVehicles: List[T] = vehicles
  }

  class ContravariantPark[-T](vehicles: List[T]) {
    def park(vehicle: T): ContravariantPark[T] = new ContravariantPark(vehicle :: vehicles)
    def impound(vehicleList: List[T]): Unit = vehicleList.foreach(x => println(s"Vehicle $x impounded"))
    def unPark(vehicle: T): ContravariantPark[T] = new ContravariantPark(vehicles.filter(x => x != vehicle))
//    def checkVehicles[B <: T]: List[T] = vehicles
    override def toString: String = vehicles.mkString(" ")
  }

  def main(args: Array[String]): Unit = {
    val car_1 = new Car(234)
    val car_2 = new Car(235)
    val car_3 = new Car(236)
    val bike_1 = new Bike(768)

    val coParking_1 = new CovariantPark(List(car_1, car_2, car_3))
    val coParking_2 = coParking_1.park(bike_1).unPark(car_2)
    println(coParking_2.checkVehicles)

    val contraParking_1 = new ContravariantPark(List(car_1, car_2, car_3))
//    val contraParking_2 = contraParking_1.park(bike_1).unPark(car_2)
//    println(contraParking_2)
  }
}
