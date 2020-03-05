package slick

import com.github.tminglei.slickpg.ExPostgresProfile.api._
import slick.SongTable.Column._
import slick.lifted.Tag

class SongTable(tag: Tag, albumTable: TableQuery[AlbumTable]) extends Table[SongEntity](tag, SongTable.Name){

  def recordId = column[Long](RecordId, O.PrimaryKey, O.AutoInc)
  def name = column[String](Name)
  def lengthInSeconds = column[Int](LengthInSeconds)
  def albumRecordId = column[Long](AlbumRecordId)
  def pk = primaryKey(PK, (recordId))
  def album = foreignKey(FK, albumRecordId, albumTable)(_.recordId)

  override def * = ???

  private val fields = (
    recordId,
    name,
    lengthInSeconds,
    albumRecordId
  )
}

object SongTable {
  final val Name = "song"
  object Column {
    final val RecordId = "record_id"
    final val Name = "name"
    final val LengthInSeconds = "length_in_seconds"
    final val AlbumRecordId = "album_record_id"
    final val PK = "pk_song_record_id"
    final val FK = "fk_album_record_id"
  }
}

