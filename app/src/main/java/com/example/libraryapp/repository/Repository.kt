package com.example.libraryapp.repository

import com.example.libraryapp.room.BookDB
import com.example.libraryapp.room.BookEntity

// logic to decide which to use Room or server etc.. like addBookToRoom, addBookToServer
class Repository(val bookDB: BookDB) {

    suspend fun addBookToRoom(bookEntity: BookEntity){
        bookDB.bookDao().addBook(bookEntity)
    }

    fun getAllBooks() = bookDB.bookDao().getAllBooks()

    suspend fun deleteBookFromRoom(bookEntity: BookEntity){
        bookDB.bookDao().deleteBook(bookEntity)
    }

    suspend fun updateBook(bookEntity: BookEntity){
        bookDB.bookDao().updateBook(bookEntity)
    }
}