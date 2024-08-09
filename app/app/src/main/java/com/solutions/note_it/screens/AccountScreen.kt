package com.solutions.note_it.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.solutions.note_it.R.string.btn_submit
import com.solutions.note_it.R.string.placeholder_email
import com.solutions.note_it.R.string.placeholder_name
import com.solutions.note_it.R.string.placeholder_nickname
import com.solutions.note_it.viewmodels.AccountViewModel

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel()
) {
    LaunchedEffect("") {
        viewModel.loadUser()
    }

    var user = viewModel.user

    Surface(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        val spacerPad = 8.dp

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(label = { Text(text = stringResource(placeholder_nickname)) },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                singleLine = true,
                value = user.nickname,
                onValueChange = { nickname ->
                    user = user.copy(nickname = nickname)
                    viewModel.updateUserDetail(user) })
            Spacer(modifier = Modifier.height(spacerPad))

            OutlinedTextField(label = { Text(text = stringResource(placeholder_name)) },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                singleLine = true,
                value = user.name,
                onValueChange = { name ->
                    user = user.copy(name = name)
                    viewModel.updateUserDetail(user) })
            Spacer(modifier = Modifier.height(spacerPad))

            OutlinedTextField(label = { Text(text = stringResource(placeholder_email)) },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                singleLine = true,
                value = user.email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { email ->
                    user = user.copy(email = email)
                    viewModel.updateUserDetail(user) })
            Spacer(modifier = Modifier.height(spacerPad))

            Button(
                onClick = { viewModel.updateUser() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(btn_submit))
            }
        }
    }
}