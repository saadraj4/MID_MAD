package com.example.finallab

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finallab.ui.theme.FinalLabTheme
import kotlinx.coroutines.delay
import androidx.compose.material3.DropdownMenuItem as DropdownMenuItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "splash") {
                composable("splash") { SplashScreen(navController = navController) }
                composable("main") { MainScreen(navController) }
                composable("overview") { OverviewScreen() }
                composable("add") {
                    AddTransactionScreen { name, amount, type, reason ->
                        // Here you would handle adding the transaction.
                        // For now, we'll just pop back to the previous screen.
                        // In a real app, you'd likely update some form of shared state or database.
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}





// SplashScreen.kt
@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("main")
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo"
        )
        Text(
            text = "EconTrackers",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun MainScreen(navController: NavController){
    var currentBalance by remember { mutableStateOf(0.0) }

    Column {
        Text("Current Balance: $0.00")
        Button(onClick = { navController.navigate("add") }) {
            Text("Add Transaction")
        }

        Button(onClick = { navController.navigate("overview") }) {
            Text("Overview")
        }
    }
}

    @Composable
    fun AddTransactionScreen(onAddTransaction: (String, Double, String, String) -> Unit) {
        var transactionName by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }
        var reason by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        var selectedTransactionType by remember { mutableStateOf("") }

        Scaffold(

        ) { padding ->
            Column(modifier = Modifier
                .padding(padding)
                .padding(16.dp)) {

                OutlinedTextField(
                    value = transactionName,
                    onValueChange = { transactionName = it },
                    label = { Text("Transaction Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = selectedTransactionType,
                    onValueChange = {},
                    label = { Text("Transaction Type") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Filled.ArrowDropDown, "Dropdown")
                        }
                    },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Income") }, // Material 3 DropdownMenuItem uses content parameters directly
                        onClick = {
                            selectedTransactionType = "Income"
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Expense") }, // Similar adjustment for the "Expense" item
                        onClick = {
                            selectedTransactionType = "Expense"
                            expanded = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (transactionName.isNotBlank() && amount.isNotBlank() && selectedTransactionType.isNotBlank()) {
                            onAddTransaction(transactionName, amount.toDoubleOrNull() ?: 0.0, selectedTransactionType, reason)
                            // Reset form fields (optional here since we're navigating away)

                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = transactionName.isNotBlank() && amount.toDoubleOrNull() != null
                ) {
                    Text("Add")
                }
            }
        }
    }


    @Composable
    fun OverviewScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top section with income, expenses, and total balance
            Card(
                modifier = Modifier.padding(bottom = 8.dp),

            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Incomes")
                    Text("RS 390,00")
                }
            }

            Card(
                modifier = Modifier.padding(vertical = 8.dp),

            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Expenses")
                    Text("-RS 300,00")
                }
            }

            Card(
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Total",  color = Color.White)
                    Text("RS 90,00",  color = Color.White)
                }
            }

            // Section for last transactions
            Spacer(Modifier.height(16.dp))
            Text("Last Transactions" )
            Spacer(Modifier.height(8.dp))

            // This is a placeholder for your transactions list
            // In a real app, this would be a dynamically generated list
            Column {
                TransactionItem(description = "Income", amount = "RS 390,00", date = "2024/03/29")
                TransactionItem(description = "Expense", amount = "-RS 300,00", date = "2024/03/29")
                // Add more transactions as needed
            }

            // Floating Action Button to add a new transaction
            // Align it to the bottom-right of the screen
            // In real app logic, you'd handle the click to navigate to the add transaction screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(onClick = { /* Navigate to add transaction screen */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Transaction")
                }
            }
        }
    }

    @Composable
    fun TransactionItem(description: String, amount: String, date: String) {
        Row(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(description )
                Text(amount)
            }
            Text(date)
        }
    }

