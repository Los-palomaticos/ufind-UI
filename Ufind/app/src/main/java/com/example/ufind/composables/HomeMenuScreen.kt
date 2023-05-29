package com.example.ufind.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout


@Preview(showBackground=true)
@Composable
fun HomeMenuScreen(){
    MenuScreen()

}

@Composable
fun MenuScreen() {
    PageHeader()
    FoundSomethingButton()
    PostsUFind()
    BottomNavBar()

}

@Composable
fun BottomNavBar() {
    TODO("Not yet implemented")
}

@Composable
fun PostsUFind() {
    TODO("Not yet implemented")
}

@Composable
fun FoundSomethingButton() {
    TODO("Not yet implemented")
}

@Composable
fun PageHeader() {
    TODO("Not yet implemented")
}

