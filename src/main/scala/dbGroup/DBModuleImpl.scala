package dbGroup

import dbGroup.entities.{OrderMetricsRepository, _}
import module.Configuration
import slick.backend.DatabaseConfig
import slick.dbio.DBIO
import slick.driver.JdbcProfile

import scala.concurrent.Future


trait Profile {
  val profile: JdbcProfile
}

trait DbModule extends Profile {
  val db: JdbcProfile#Backend#Database

  implicit def executeOperation[T](databaseOperation: DBIO[T]): Future[T] = {
    db.run(databaseOperation)
  }

  val languageDal: LanguageRepository
  val metricsDal: MetricsRepository
  val userDal: UserRepository
  val orderDal: OrderRepository
  val order_metricsDal: OrderMetricsRepository
}

trait DBModuleImpl extends DbModule {
  this: Configuration =>

  // use an alternative database configuration ex:
  private val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("pgdb")

  override implicit val profile: JdbcProfile = dbConfig.driver
  override implicit val db: JdbcProfile#Backend#Database = dbConfig.db

  override val languageDal: LanguageRepository = new LanguageRepository(profile)
  override val metricsDal: MetricsRepository = new MetricsRepository(profile)
  override val userDal: UserRepository = new UserRepository(profile)
  override val orderDal: OrderRepository = new OrderRepository(profile)
  override val order_metricsDal: OrderMetricsRepository =  new OrderMetricsRepository(profile)
}
