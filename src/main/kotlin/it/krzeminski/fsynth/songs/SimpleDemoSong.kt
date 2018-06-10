package it.krzeminski.fsynth.songs

import it.krzeminski.fsynth.MusicNote
import it.krzeminski.fsynth.MusicNote.*
import it.krzeminski.fsynth.sineWave

fun sineWaveForNote(note: MusicNote) = sineWave(note.frequency)

val silence = { _: Float -> 0.0f }

val simpleDemoSong: (Float) -> Float = { time ->
    when (time) {
        in 0.0f..0.25f -> sineWaveForNote(D4)
        in 0.25f..0.375f -> sineWaveForNote(Csharp4)
        in 0.375f..0.5f -> sineWaveForNote(D4)
        in 0.5f..0.75f -> sineWaveForNote(E4)
        in 0.75f..1.0f -> sineWaveForNote(D4)
        in 1.25f..1.5f -> sineWaveForNote(Fsharp4)
        in 1.5f..2.0f -> sineWaveForNote(G4)
        else -> silence
    }(time)
}
