package com.artier.ide.lite.feature.aipanel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.artier.ide.lite.core.model.AiMode
import com.artier.ide.lite.core.model.Message
import com.artier.ide.lite.core.model.MessageRole
import com.artier.ide.lite.ui.theme.ArtierColors
import kotlinx.coroutines.flow.first

/**
 * AI Assistant Panel - Cursor/Void IDE style
 *
 * Features:
 * - Source selector (Built-in / OpenCode / Claude Code)
 * - Mode selector (Agent / Gather / Chat)
 * - Message list with streaming support
 * - Input field with send button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiPanel(
    modifier: Modifier = Modifier,
    viewModel: AiViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()

    var isSourceExpanded by remember { mutableStateOf(false) }
    var isModeExpanded by remember { mutableStateOf(false) }

    val sources = listOf("Built-in Agent", "OpenCode", "Claude Code")
    val modes = listOf("Agent", "Gather", "Chat")

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier.padding(12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI Assistant",
                style = MaterialTheme.typography.titleMedium,
                color = ArtierColors.onBackground
            )

            IconButton(
                onClick = { viewModel.clearConversation() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear conversation",
                    tint = ArtierColors.textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Source selector
        ExposedDropdownMenuBox(
            expanded = isSourceExpanded,
            onExpandedChange = { isSourceExpanded = it }
        ) {
            OutlinedTextField(
                value = viewModel.selectedSource,
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
                            viewModel.setSource(source)
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
                value = viewModel.selectedMode.name,
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
                            viewModel.setMode(AiMode.valueOf(mode.uppercase()))
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
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ask anything about your code...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArtierColors.textMuted
                        )
                    }
                }
            }

            items(messages) { message ->
                MessageBubble(message = message)
            }

            // Show streaming indicator
            if (viewModel.isStreaming) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = ArtierColors.primary
                        )
                        Text(
                            text = "Thinking...",
                            style = MaterialTheme.typography.bodySmall,
                            color = ArtierColors.textMuted
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input field
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.inputText,
                onValueChange = { viewModel.updateInput(it) },
                placeholder = { Text("Ask anything...") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ArtierColors.primary,
                    unfocusedBorderColor = ArtierColors.border,
                    focusedContainerColor = ArtierColors.inputBackground,
                    unfocusedContainerColor = ArtierColors.inputBackground
                ),
                maxLines = 3,
                enabled = !viewModel.isStreaming
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { viewModel.sendMessage() },
                enabled = viewModel.inputText.isNotBlank() && !viewModel.isStreaming
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (viewModel.inputText.isNotBlank() && !viewModel.isStreaming)
                        ArtierColors.primary else ArtierColors.textMuted
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    modifier: Modifier = Modifier
) {
    val isUser = message.role == MessageRole.USER
    val backgroundColor = if (isUser) ArtierColors.primary.copy(alpha = 0.2f)
                          else ArtierColors.surfaceVariant
    val roleColor = ArtierColors.primary
    val roleText = when (message.role) {
        MessageRole.USER -> "You"
        MessageRole.ASSISTANT -> "AI Assistant"
        MessageRole.TOOL -> "Tool"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        Text(
            text = roleText,
            style = MaterialTheme.typography.labelMedium,
            color = roleColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = message.content,
            style = MaterialTheme.typography.bodyMedium,
            color = ArtierColors.onBackground,
            fontFamily = FontFamily.Monospace
        )
    }
}
