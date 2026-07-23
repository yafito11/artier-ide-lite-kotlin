package com.artier.ide.lite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.artier.ide.lite.ui.theme.ArtierColors

/**
 * AI Assistant Panel - Cursor/Void IDE style
 *
 * Features:
 * - Source selector (Built-in / OpenCode / Claude Code)
 * - Mode selector (Agent / Gather / Chat)
 * - Message list
 * - Input field with send button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPanel(
    modifier: Modifier = Modifier
) {
    var selectedSource by remember { mutableStateOf("Built-in Agent") }
    var selectedMode by remember { mutableStateOf("Agent") }
    var inputText by remember { mutableStateOf("") }
    var isSourceExpanded by remember { mutableStateOf(false) }
    var isModeExpanded by remember { mutableStateOf(false) }

    val sources = listOf("Built-in Agent", "OpenCode", "Claude Code")
    val modes = listOf("Agent", "Gather", "Chat")

    Column(
        modifier = modifier.padding(12.dp)
    ) {
        // Header
        Text(
            text = "AI Assistant",
            style = MaterialTheme.typography.titleMedium,
            color = ArtierColors.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Source selector
        ExposedDropdownMenuBox(
            expanded = isSourceExpanded,
            onExpandedChange = { isSourceExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedSource,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sumber") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSourceExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ArtierColors.primary,
                    unfocusedBorderColor = ArtierColors.border,
                    focusedContainerColor = ArtierColors.inputBackground,
                    unfocusedContainerColor = ArtierColors.inputBackground
                )
            )

            ExposedDropdownMenu(
                expanded = isSourceExpanded,
                onDismissRequest = { isSourceExpanded = false }
            ) {
                sources.forEach { source ->
                    DropdownMenuItem(
                        text = { Text(source) },
                        onClick = {
                            selectedSource = source
                            isSourceExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mode selector
        ExposedDropdownMenuBox(
            expanded = isModeExpanded,
            onExpandedChange = { isModeExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedMode,
                onValueChange = {},
                readOnly = true,
                label = { Text("Mode") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isModeExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ArtierColors.primary,
                    unfocusedBorderColor = ArtierColors.border,
                    focusedContainerColor = ArtierColors.inputBackground,
                    unfocusedContainerColor = ArtierColors.inputBackground
                )
            )

            ExposedDropdownMenu(
                expanded = isModeExpanded,
                onDismissRequest = { isModeExpanded = false }
            ) {
                modes.forEach { mode ->
                    DropdownMenuItem(
                        text = { Text(mode) },
                        onClick = {
                            selectedMode = mode
                            isModeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = ArtierColors.divider)

        Spacer(modifier = Modifier.height(12.dp))

        // Messages area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Placeholder messages
            item {
                MessageBubble(
                    role = "assistant",
                    content = "Hello! I'm Artier AI Assistant. How can I help you today?"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input field
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask anything...") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ArtierColors.primary,
                    unfocusedBorderColor = ArtierColors.border,
                    focusedContainerColor = ArtierColors.inputBackground,
                    unfocusedContainerColor = ArtierColors.inputBackground
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { /* TODO: Send message */ }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = ArtierColors.primary
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    role: String,
    content: String
) {
    val isUser = role == "user"
    val backgroundColor = if (isUser) ArtierColors.primary.copy(alpha = 0.2f)
                          else ArtierColors.surfaceVariant
    val textColor = ArtierColors.onBackground

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        Text(
            text = if (isUser) "You" else "AI Assistant",
            style = MaterialTheme.typography.labelMedium,
            color = ArtierColors.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}
