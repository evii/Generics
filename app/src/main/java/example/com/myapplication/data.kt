package example.com.myapplication

open class Employee(open val id: Int)

data class BackOfficeEmployee(override val id: Int, open val name: String): Employee(id)

data class Programmer(override val id: Int, val name: String, val salary: Int, val language: String): Employee(id)

// T: Employee = Generic constraint - upper bound
data class Contract<T: Employee> (val employee: T, val duration: Int)

// *********** Declaration-Site Variance **************
// out T : Employee = Declaration-Site Variance - COVARIANCE - T is read parameter - it acts only as return type
// Location<Programmer> is subtype of Location<Employee>
data class Location<out T : Employee> (val employee: T, val location: String) {
  //T acts as return type
    fun getEmployeeType(): T {
        return employee
    }
}
// in T : Employee = Declaration-Site Variance - CONTRAVARIANCE, T is input parameter in setEmployeeType method,
// Manager<Employee> is subtype of Manager<Programmer>
// member variable employee must be private!
data class Manager<in T : Employee> (private var employee: T, val managerName: String) {
    //T acts as return type
    fun setEmployeeType(employee: T) {
        this.employee = employee
    }
}

fun main() {

    val backOfficeEmpl1: BackOfficeEmployee = BackOfficeEmployee(1, "Lara")
    val programmer1: Programmer = Programmer(2, "Peter", 12000, "Java")

    // with Generic constraint for Contract - it is effective and safe to have just one class Contract for all types of Employees
    val contractWithBOEmpl1 = Contract(backOfficeEmpl1, 24)
    val contractWithProgrammer1 = Contract(programmer1,18)

    // Polymorphism
    var employee: Employee
    employee = programmer1
    val id = employee.id
    println(id)

    //Polymorphism with generic types - limitations:
    var contractWithEmployee: Contract<Employee>
    // this throws error, because Contract<Programmer> is not subtype of Contract<Employee> !! INVARIANCE
    //contractWithEmployee = contractWithProgrammer1

    //On the other hand this works as T: Employee is out
    val  locationProgrammer1 = Location(programmer1, "London")
    var locationEmployee: Location<Employee>
    locationEmployee = locationProgrammer1
    println(locationEmployee.location)

    // And this works as T: Employee is in
    val managerOfProgrammer1: Manager <Programmer>
    val manager: Manager<Employee> = Manager(employee, "Ralph")
    managerOfProgrammer1 = manager
    println(managerOfProgrammer1.managerName)
}