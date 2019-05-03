package db

import java.sql.ResultSet

import entities.module.Module

object ModuleDao extends Dao[Module] {
  override val tableName = "MODULE"
  override val handler = (rs: ResultSet) => if (rs.next) Array(Module(rs)) else Array()

  def insert(module: Module) =
    run.update(conn, s"INSERT INTO MODULE VALUES(${module.moduleId}, '${module.moduleCode}', '${module.moduleName}', '${module.moduleDescription}', NULL, NULL)")
}