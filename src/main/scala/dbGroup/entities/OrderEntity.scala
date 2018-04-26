package dbGroup.entities

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.driver.JdbcProfile
import slick.lifted

case class OrderEntity(override val id: Option[Long],login:String,startDate:String, link: String)   extends Entity[OrderEntity, Long] {
  def withId(id: Long): OrderEntity = this.copy(id = Some(id))
}

class OrderRepository(override val driver: JdbcProfile) extends Repository[OrderEntity, Long](driver) {

  import dbGroup.driver.AnalysisPostgresProfile.api._

  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = lifted.TableQuery[OrderTable]
  type TableType = OrderTable

  class OrderTable(tag: Tag) extends Table[OrderEntity](tag, "order") with Keyed[Long] {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def login = column[String]("login")

    def startDate = column[String]("startDate")

    def link = column[String]("link")

    def * = (id.?,login,startDate, link) <> ((OrderEntity.apply _).tupled, OrderEntity.unapply _)
  }

  def findByOrderId (orderId: Long): DBIO[Option[OrderEntity]]= {
    tableQuery.filter(_.id === orderId).result.headOption
  }

}