package dbGroup.entities

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.driver.JdbcProfile
import slick.lifted

case class LanguageEntity(override val id: Option[Long],language:String)   extends Entity[LanguageEntity, Long] {
  def withId(id: Long): LanguageEntity = this.copy(id = Some(id))
}

class LanguageRepository(override val driver: JdbcProfile) extends Repository[LanguageEntity, Long](driver) {

  import dbGroup.driver.AnalysisPostgresProfile.api._

  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = lifted.TableQuery[LanguageTable]
  type TableType = LanguageTable

  class LanguageTable(tag: Tag) extends Table[LanguageEntity](tag, "language") with Keyed[Long] {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def language = column[String]("language")

    def * = (id.?,language) <> ((LanguageEntity.apply _).tupled, LanguageEntity.unapply _)
  }

  def getLanguage(language : String): DBIO[Option[LanguageEntity]] = {

    tableQuery.filter(_.language === language).result.headOption
  }
}