package dbGroup.entities

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.driver.JdbcProfile
import slick.lifted

case class UserEntity(override val id: Option[Long],login:String, password: String, email: String, cookies:Option[String],registrationDate:String)   extends Entity[UserEntity, Long] {
  def withId(id: Long): UserEntity = this.copy(id = Some(id))
}

class UserRepository(override val driver: JdbcProfile) extends Repository[UserEntity, Long](driver) {

  import dbGroup.driver.AnalysisPostgresProfile.api._

  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = lifted.TableQuery[UserTable]
  type TableType = UserTable

  class UserTable(tag: Tag) extends Table[UserEntity](tag, "user") with Keyed[Long] {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def login = column[String]("login")
    def password = column[String]("password")
    def email = column[String]("email")
    def cookies = column[Option[String]]("cookies")
    def registrationDate = column[String]("registrationDate")

    def * = (id.?,login, password,email, cookies,registrationDate) <> ((UserEntity.apply _).tupled, UserEntity.unapply _)
  }

  def getUserByLogin(login : String): DBIO[Option[UserEntity]] = {
    tableQuery.filter(_.login === login).result.headOption
  }

  def getUserByEmail(email : String): DBIO[Option[UserEntity]] = {
    tableQuery.filter(_.email === email).result.headOption
  }

}