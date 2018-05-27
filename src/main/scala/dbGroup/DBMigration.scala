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

  val insertJavaLanguage =
    SqlMigration("INSERT INTO language (language) values (\'Java\')")

  val insertCSharpLanguage =
    SqlMigration("INSERT INTO language (language) values (\'C#\')")

  val migration2 = VersionedMigration("2", insertJavaLanguage, insertCSharpLanguage)

  val insertMetrics1 =
    SqlMigration("INSERT INTO metrics (languageId, description, startCode) values (1,\'getBlankStrCount\',101)")

  val insertMetrics2 =
    SqlMigration("INSERT INTO metrics (languageId, description, startCode) values (1,\'getCommentsCount\',102)")

  val insertMetrics3 =
    SqlMigration("INSERT INTO metrics (languageId, description, startCode) values (1,\'getRatioOfComments\',103)")

  val migration3 = VersionedMigration("3", insertMetrics1, insertMetrics2, insertMetrics3)


  val flyway = new Flyway()
  flyway.setDataSource(ConfigFactory.load().getString("pgdb.db.url"),
    ConfigFactory.load().getString("pgdb.db.user"),
    ConfigFactory.load().getString("pgdb.db.password"))
  flyway.setLocations()

  flyway.setResolvers(Resolver(migration),
    Resolver(migration2),
    //Resolver(migration3)
  )

  flyway.migrate()

}
