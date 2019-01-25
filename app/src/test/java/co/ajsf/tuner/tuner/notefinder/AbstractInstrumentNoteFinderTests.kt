package co.ajsf.tuner.tuner.notefinder

import co.ajsf.tuner.model.Instrument
import co.ajsf.tuner.tuner.notefinder.model.MusicalNote
import co.ajsf.tuner.tuner.notefinder.model.mapToMusicalNoteList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal abstract class AbstractInstrumentNoteFinderTests() {

    protected abstract val instrument: Instrument

    private lateinit var noteFinder: NoteFinder

    @BeforeEach
    fun setup() {
        noteFinder = NoteFinder
            .instrumentNoteFinder(instrument.mapToMusicalNoteList())
    }

    @Test
    fun `it returns NO_NOTE for negative frequencies`() {
        val noteData = noteFinder.findNote(-1f)
        assertEquals(NO_NOTE, noteData)
    }

    @Test
    fun `it returns NO_NOTE for frequencies that are a whole step below the lowest string`() {
        val note = MusicalNote(
            MusicalNote
                .fromFloat(instrument.strings.first().freq)
                .relativeFreq(-2)
        )

        val noteData = noteFinder.findNote(note.floatFreq)

        assertEquals(NO_NOTE, noteData)
    }

    @Test
    fun `it returns the first string with a delta of 100 for frequencies that are a half step below the lowest string`() {
        val note = MusicalNote(
            MusicalNote
                .fromFloat(instrument.strings.first().freq)
                .relativeFreq(-1)
        )

        val noteData = noteFinder.findNote(note.floatFreq)
        val expectedNote = NoteData(instrument.strings.first().name, 0, -100)

        assertEquals(expectedNote, noteData)
    }

    @Test
    fun `it returns NO_NOTE for frequencies that are a whole step above the highest string`() {
        val note = MusicalNote(
            MusicalNote
                .fromFloat(instrument.strings.last().freq)
                .relativeFreq(2)
        )

        val noteData = noteFinder.findNote(note.floatFreq)

        assertEquals(NO_NOTE, noteData)
    }

    @Test
    fun `it returns the first string with a delta of 100 for frequencies that are a half step above the highest string`() {
        val note = MusicalNote(
            MusicalNote
                .fromFloat(instrument.strings.last().freq)
                .relativeFreq(1)
        )

        val noteData = noteFinder.findNote(note.floatFreq)
        val expectedData = NoteData(instrument.strings.last().name, instrument.strings.lastIndex, 100)

        assertEquals(expectedData, noteData)
    }

    @Test
    fun `it returns the correct note and number with a delta of 0 for tuned frequencies`() {
        instrument.strings.forEachIndexed { index, string ->

            val noteData = noteFinder.findNote(string.freq)
            val expectedData = NoteData(string.name, index, 0)

            assertEquals(expectedData, noteData)
        }
    }

    @Test
    fun `it returns the correct string name and number for frequencies a half step below the note`() {
        instrument.strings.forEachIndexed { index, string ->
            val note = MusicalNote(
                MusicalNote
                    .fromFloat(string.freq)
                    .relativeFreq(-1)
            )

            val noteData = noteFinder.findNote(note.floatFreq)

            assertEquals(string.name, noteData.name)
            assertEquals(index, noteData.number)
        }
    }

    @Test
    fun `it returns the correct string name and number for frequencies a half step above the note`() {
        instrument.strings.forEachIndexed { index, string ->
            val note = MusicalNote(
                MusicalNote
                    .fromFloat(string.freq)
                    .relativeFreq(1)
            )

            val noteData = noteFinder.findNote(note.floatFreq)

            assertEquals(string.name, noteData.name)
            assertEquals(index, noteData.number)
        }
    }


}