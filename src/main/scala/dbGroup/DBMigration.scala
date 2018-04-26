package dbGroup

import com.typesafe.config.ConfigFactory
import slick.migration.api.{PostgresDialect, ReversibleTableMigration, SqlMigration, TableMigration}
import org.flywaydb.core.Flyway
import slick.migration.api.flyway.{Resolver, VersionedMigration}

trait DBMigration {

}

trait DBMigrationImpl extends DBMigration  with DbModule {

  implicit val dialect = new PostgresDialect


  val createLanguageTable =
    TableMigration(languageDal.tableQuery)
      .create
      .addColumns(_.id,_.language)
  val createMetricsTable =
    TableMigration(metricsDal.tableQuery)
      .create
      .addColumns(_.id,_.languageId,_.description,_.startCode)

  val createUserTable =
    TableMigration(userDal.tableQuery)
      .create
      .addColumns(_.id,_.login, _.password,_.email, _.cookies, _.registrationDate)

  val createOrderTable =
    TableMigration(orderDal.tableQuery)
      .create
      .addColumns(_.id,_.login,_.startDate, _.link)

  val createOrderMetricsTable =
    TableMigration(order_metricsDal.tableQuery)
      .create
      .addColumns(_.id,_.orderId,_.metricsId,_.status,_.result)

  val migration = VersionedMigration("1", createLanguageTable, createMetricsTable,createUserTable,createOrderTable,createOrderMetricsTable)

  val flyway = new Flyway()
  flyway.setDataSource(ConfigFactory.load().getString("pgdb.db.url"),
    ConfigFactory.load().getString("pgdb.db.user"),
    ConfigFactory.load().getString("pgdb.db.password"))
  flyway.setLocations()

  flyway.setResolvers(Resolver(migration))

  flyway.migrate()

}
