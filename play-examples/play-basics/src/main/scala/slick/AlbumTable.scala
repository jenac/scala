package slick

import slick.lifted.Tag
import com.github.tminglei.slickpg.ExPostgresProfile.api._
import AlbumTable.Column._
class AlbumTable(tag: Tag) extends Table[AlbumEntity](tag, AlbumTable.Name){
  def recordId = column[Long](RecordId, O.PrimaryKey, O.AutoInc)
  def title = column[String](Title)
  def artist = column[String](Artist)
  def pk = primaryKey(PK, (recordId)) //if multiple pk, (col1, col2) here.

  override def * = fields <> (AlbumEntity.tupled, AlbumEntity.unapply)

  private val fields = (
    recordId,
    title,
    artist
  )
}

object AlbumTable {
  final val Name = "album"
  object Column {
    final val RecordId = "record_id"
    final val Title = "title"
    final val Artist = "artist"
    final val PK = "pk_album_record_id"
  }
}