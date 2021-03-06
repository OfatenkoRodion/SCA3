package dbGroup.entities

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.driver.JdbcProfile
import slick.lifted

case class OrderMetricsEntity(override val id: Option[Long],orderId:Long, metricsId: Long,status:String,result:Option[String])   extends Entity[OrderMetricsEntity, Long] {
  def withId(id: Long): OrderMetricsEntity = this.copy(id = Some(id))
}

class OrderMetricsRepository(override val driver: JdbcProfile) extends Repository[OrderMetricsEntity, Long](driver) {

  import dbGroup.driver.AnalysisPostgresProfile.api._

  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = lifted.TableQuery[OrderMetricsTable]
  type TableType =OrderMetricsTable

  class OrderMetricsTable(tag: Tag) extends Table[OrderMetricsEntity](tag, "order_metrics") with Keyed[Long] {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def orderId = column[Long]("orderId")
    def metricsId = column[Long]("metricsId")
    def status = column[String]("status")
    def result = column[Option[String]]("result")

    def * = (id.?,orderId, metricsId, status, result) <> ((OrderMetricsEntity.apply _).tupled, OrderMetricsEntity.unapply _)
  }

  def findByOrderId (orderId: Long): DBIO[Seq[OrderMetricsEntity]] = {
    tableQuery.filter(v => v.orderId === orderId).result
  }

  def findById (id: Long): DBIO[Option[OrderMetricsEntity]] = {
    tableQuery.filter(v => v.id === id).result.headOption
  }

  def setResult(result:String, idMetricOrder: Long): Unit = {

  }

}