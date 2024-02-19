package com.canolabs.rallytransbetxi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.canolabs.rallytransbetxi.ui.theme.RallyTransbetxiTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    private val TAG = "FIREBASE_DATABASE"
    private val db = FirebaseFirestore.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyTransbetxiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                    val stages = hashMapOf(
                        "TC3" to "La Mina",
                        "TC4" to "Sagrat Cor - Rodaor"
                    )

                    db.collection("stages").document("TCP").set(stages).addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added")
                    }.addOnFailureListener {
                        Log.w(TAG, "Error adding document", it)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RallyTransbetxiTheme {
        Greeting("Android")
    }
}