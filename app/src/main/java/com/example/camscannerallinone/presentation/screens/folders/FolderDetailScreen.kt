package com.example.camscannerallinone.presentation.screens.folders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.camscannerallinone.presentation.screens.home.DocumentItem
import com.example.camscannerallinone.presentation.theme.PrimaryBlue
import com.example.camscannerallinone.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailScreen(
    onBack: () -> Unit,
    onNavigateToDocumentDetail: (Long) -> Unit,
    viewModel: FolderDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.folder?.name ?: "Folder", fontWeight = FontWeight.Bold, color = PrimaryBlue) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryBlue)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(White)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.documents) { document ->
                    DocumentItem(
                        document = document,
                        onClick = { onNavigateToDocumentDetail(document.id) },
                        onDelete = { /* Add delete logic to FolderDetailViewModel if needed */ },
                        onRename = { /* Add rename logic to FolderDetailViewModel if needed */ }
                    )
                }
            }
        }
    }
}
