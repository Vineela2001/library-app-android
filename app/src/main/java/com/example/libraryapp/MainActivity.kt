package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.room.BookDB
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.ui.theme.LibraryAppTheme
import com.example.libraryapp.ui.theme.UpdateScreen
import com.example.libraryapp.viewmodel.BookViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mContext = application
        val db = BookDB.getInstance(mContext)
        val repository = Repository(db)
        val viewModel = BookViewModel(repository = repository)
        val viewModel2 = BookViewModel(repository = repository)


        enableEdgeToEdge()
        setContent {
            LibraryAppTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Navigation
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "MainScreen") {

                        composable("MainScreen") {
                            MainScreen(
                                viewModel,
                                navController,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable("UpdateScreen/{bookid}") {
                            UpdateScreen(viewModel, bookId = it.arguments?.getString("bookid"))
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: BookViewModel,navController: NavHostController, modifier: Modifier = Modifier) {
    var inputBook by remember {
        mutableStateOf("")
    }
    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Insert books into Room DB", fontSize = 32.sp)
        OutlinedTextField(
            value = inputBook, onValueChange = { enteredText ->
                inputBook = enteredText
            }, label = { Text("Book name") },
            placeholder = { Text("Enter the book name") }
        )
        Button(onClick = {
            viewModel.addBook(BookEntity(0, inputBook))
        }) {
            Text("Insert Book into DB")
        }
        BooksList(viewModel, navController)
    }
}

@Composable
fun BookCard(viewModel: BookViewModel, navController: NavHostController,book: BookEntity) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Text(
                text = "" + book.id,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp)
            )
            Text(book.title, fontSize = 24.sp)

            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { viewModel.deleteBook(book) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }

                IconButton(onClick = {
                    navController.navigate("UpdateScreen/${book.id}")
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
            }

        }
    }

}

@Composable
fun BooksList(viewModel: BookViewModel, navController: NavHostController) {
    val books by viewModel.books.collectAsState(emptyList())

    LazyColumn(modifier = Modifier.fillMaxSize(0.7f)) {
        items(books) { item ->
            BookCard(viewModel, navController, item)
        }
    }

}