package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class shoppingItems(
    var id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun ToDoListApp(){
    var sItems by remember{ mutableStateOf(listOf<shoppingItems>()) }
    var showDialog by remember{ mutableStateOf(false)}
    var itemName by remember{ mutableStateOf("")}
    var itemquantity by remember{ mutableStateOf("")}


    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {showDialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(text = "TO DO LIST: add Tasks")

        }
        // load only that much which is visible in the mobile screen
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems) {
                item ->
                if (item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete ={
                        editedName, editedQuantity ->
                        sItems = sItems.map { it.copy(isEditing = false ) }
                        val editedItem = sItems.find { it.id == item.id }
                          editedItem?.let {
                              it.name = editedName
                              it.quantity = editedQuantity
                          }
                    } )

                }else{
                    shoppingListItems(item = item, onEditClick = {
                        sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                    },
                        onCancelClick = {
                            sItems = sItems -item
                        }
                    )
                }
            }
        }

        if(showDialog){
            AlertDialog(onDismissRequest = {  showDialog = false},
                confirmButton = {

                          Row(modifier = Modifier
                              .fillMaxWidth()
                              .padding(8.dp),
                              horizontalArrangement = Arrangement.SpaceBetween) {
                              Button(onClick = {
                                  if(itemName.isNotBlank()){
                                      val newItem = shoppingItems(
                                          id = sItems.size+1,
                                          name = itemName,
                                          quantity = itemquantity.toInt()
                                      )
                                      sItems = sItems + newItem
                                      showDialog = false
                                      itemName = ""

                                  }
                              }) {
                                    Text(text = "add")
                              }
                              Button(onClick = {showDialog = false}) {
                                  Text(text = "cencel")
                              }



                          }
                },
                title = { Text(text = "TO DO LIST: Add Tasks & hrs here-")},
                text = {
                    Column {
                        OutlinedTextField(value = itemName,
                            onValueChange = {itemName = it},
                        singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(value = itemquantity,
                            onValueChange = { itemquantity = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            )


            }
        }

    }

@Composable
 fun ShoppingItemEditor(item: shoppingItems, onEditComplete:(String,Int) -> Unit){
     var editedName by remember { mutableStateOf(item.name)}
    var editedQuality by remember{ mutableStateOf(item.quantity.toString())}
    var isEditing by remember{ mutableStateOf(item.isEditing)}

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BasicTextField(value = editedName,
           onValueChange = {editedName = it},
            singleLine = true,
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
            )
        BasicTextField(value = editedName,
            onValueChange = {editedName = it},
            singleLine = true,
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        )
        {
            Button(onClick = {
                isEditing = false
                onEditComplete(editedName,editedQuality.toIntOrNull() ?: 1)
            }) {
                Text(text = "Save")
            }
        }
    }
 }


@Composable
fun shoppingListItems(
    item: shoppingItems,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit,
){
    Row (modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            border = BorderStroke(4.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20.dp)
        )) {
        Text(text ="Tasks:- ${item.name}", modifier = Modifier.padding(8.dp))
        Text(text = "hrs:- ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onCancelClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null )
            }
        }
    }

}

