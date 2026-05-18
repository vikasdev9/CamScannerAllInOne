package com.example.camscannerallinone.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.camscannerallinone.domain.model.Document
import com.example.camscannerallinone.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToCamera: () -> Unit,
    onNavigateToPreview: (Long) -> Unit
) {
    val documents by viewModel.documents.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onSearch = {},
                active = false,
                onActiveChange = {},
                placeholder = { Text("Search documents...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) { }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCamera) {
                Icon(Icons.Default.Add, contentDescription = "Scan")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(documents) { document ->
                DocumentItem(
                    document = document,
                    onClick = { onNavigateToPreview(document.id) },
                    onDelete = { viewModel.deleteDocument(document) },
                    onToggleFavorite = { viewModel.toggleFavorite(document) }
                )
            }
        }
    }
}

@Composable
fun DocumentItem(
    document: Document,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = document.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${document.pageCount} pages", style = MaterialTheme.typography.bodySmall)
            }
            // Add favorite and delete buttons here
        }
    }
}
