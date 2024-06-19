package com.example.testauthflow

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.testauthflow.ui.theme.TestAuthFlowTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : ComponentActivity() {

    companion object {
        private const val CLIENT_ID = "60a29cff7b3e4ed5804711375e81b06a"
        private const val REDIRECT_URI = "https://example.com/callback"
    }

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
            if (result == null) return@registerForActivityResult
            val response = AuthorizationClient.getResponse(result.resultCode, result.data)
            Log.d("MainActivity", "Code: " + response.code)
            Log.d("MainActivity", "Error: " + response.error)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAuthFlowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetCode {
                        val builder = AuthorizationRequest.Builder(
                            CLIENT_ID,
                            AuthorizationResponse.Type.CODE,
                            REDIRECT_URI
                        )

                        builder.setScopes(arrayOf("streaming"))
                        builder.setShowDialog(true)
                        val request = builder.build()

                        startActivityForResult.launch(
                            AuthorizationClient.createLoginActivityIntent(this, request)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GetCode(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.wrapContentSize(),
        onClick = {
            onClick()
        },
        content = {
            Text(
                text = "Get code",
                modifier = modifier
            )
        }
    )
}