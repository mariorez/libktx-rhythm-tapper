package manager

import kotlin.properties.Delegates

class SongManager(
    private val songData: String
) {
    private var songName: String by Delegates.notNull()
    private var songDuration: Float by Delegates.notNull()
    private var keyTimeIndex = 2
    private val keyTimeList = mutableListOf<Pair<String, Float>>()

    init {
        songData.lines().forEach {
            if (it.isEmpty()) return@forEach
            val (key, value) = it.split(":")
            when (key) {
                "SongName" -> songName = value
                "SongDuration" -> songDuration = value.toFloat()
                else -> keyTimeList.add(Pair(key, value.toFloat()))
            }
        }
    }

    fun finished() = keyTimeIndex >= keyTimeList.size

    fun currentKey(): String {
        return keyTimeList[keyTimeIndex].first
    }

    fun currentTime(): Float {
        return keyTimeList[keyTimeIndex].second
    }

    fun advanceIndex() {
        keyTimeIndex++
    }
}