package manager

import kotlin.properties.Delegates

class SongManager {
    private var songName: String by Delegates.notNull()
    private var songDuration: Float by Delegates.notNull()
    private var keyTimeIndex = 0
    private val keyTimeList = mutableListOf<Pair<String, Float>>()

    fun parseSongData(songData: String) {
        songData.lines().forEach {
            if (it.isNotEmpty()) return
            val (key, value) = it.split(":")
            when (key) {
                "SongName" -> songName = value
                "SongDuration" -> songDuration = value.toFloat()
                else -> keyTimeList.add(Pair(key, value.toFloat()))
            }
        }
    }
}