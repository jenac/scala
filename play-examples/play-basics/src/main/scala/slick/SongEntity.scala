package slick

case class SongEntity (
                      recordId: Long,
                      name: String,
                      lengthInSeconds: Int,
                      albumRecordId: Long
)
