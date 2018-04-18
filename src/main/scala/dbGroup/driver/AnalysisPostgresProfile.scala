package dbGroup.driver

import com.github.tminglei.slickpg.PgSprayJsonSupport
import com.github.tminglei.slickpg.array.PgArrayJdbcTypes
import slick.jdbc.PostgresProfile


trait AnalysisPostgresProfile extends PostgresProfile

  with PgSprayJsonSupport
  with PgArrayJdbcTypes {
  def pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  override val api = MyAPI

  object MyAPI extends API
    with SprayJsonPlainImplicits
    with JsonImplicits {

    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
  }
}

object AnalysisPostgresProfile extends AnalysisPostgresProfile
