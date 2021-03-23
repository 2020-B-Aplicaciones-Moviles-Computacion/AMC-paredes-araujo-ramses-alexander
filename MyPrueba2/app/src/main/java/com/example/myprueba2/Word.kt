package com.example.myprueba2

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "word_table")
class Word(word: String) {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private val mWord: String
    fun getWord(): String {
        return mWord
    }

    init {
        mWord = word
    }
}