package com.example.camscannerallinone.presentation.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.camscannerallinone.domain.model.Page

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (Long) -> Unit,
    onNavigateToPDF: (String) -> Unit,
    viewModel: DocumentDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.showFolderSelection) {
        FolderSelectionDialog(
            folders = state.folders,
            onDismiss = { viewModel.hideFolderSelection() },
            onFolderSelected = { viewModel.moveToFolder(it) }
        )
    }

    androidx.compose.runtime.LaunchedEffect(state.exportedPdfPath) {
        state.exportedPdfPath?.let { path ->
            onNavigateToPDF(path)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.document?.name ?: "Document") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.exportToPdf() }) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                    }
                    IconButton(onClick = { viewModel.showFolderSelection() }) {
                        Icon(Icons.Default.FolderOpen, contentDescription = "Move to Folder")
                    }
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(state.pages) { page ->
                PageItem(
                    page = page,
                    onClick = { onNavigateToEdit(page.id) }
                )
            }
        }
    }
}

@Composable
fun PageItem(
    page: Page,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.BottomCenter) {
            AsyncImage(
                model = page.originalImageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "Page ${page.pageNumber}",
                modifier = Modifier
                    .padding(4.dp),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun FolderSelectionDialog(
    folders: List<com.example.camscannerallinone.domain.model.Folder>,
    onDismiss: () -> Unit,
    onFolderSelected: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Folder") },
        text = {
            LazyColumn {
                items(folders) { folder ->
                    ListItem(
                        headlineContent = { Text(folder.name) },
                        modifier = Modifier.clickable { onFolderSelected(folder.id) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
