package dbGroup.entities

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.driver.JdbcProfile
import slick.lifted

case class MetricsEntity(override val id: Option[Long],languageId:String, description: String,startCode:Int)   extends Entity[MetricsEntity, Long] {
  def withId(id: Long): MetricsEntity = this.copy(id = Some(id))
}

class MetricsRepository(override val driver: JdbcProfile) extends Repository[MetricsEntity, Long](driver) {

  import dbGroup.driver.AnalysisPostgresProfile.api._

  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = lifted.TableQuery[MetricsTable]
  type TableType = MetricsTable

  class MetricsTable(tag: Tag) extends Table[MetricsEntity](tag, "metrics") with Keyed[Long] {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def languageId = column[String]("languageId")
    def description = column[String]("description")
    def startCode = column[Int]("startCode")

    def * = (id.?,languageId, description, startCode) <> ((MetricsEntity.apply _).tupled, MetricsEntity.unapply _)
  }

}