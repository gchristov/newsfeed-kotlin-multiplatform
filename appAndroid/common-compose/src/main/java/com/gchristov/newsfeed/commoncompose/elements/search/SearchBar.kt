import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.gchristov.newsfeed.commoncompose.elements.AppSurface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchAppBar(
	text: String,
	onTextChange: (String) -> Unit,
	onCloseClicked: () -> Unit,
	onSearchClicked: (String) -> Unit,
) {
	AppSurface(
		modifier = Modifier.fillMaxWidth()
			.height(56.dp),
		elevation = AppBarDefaults.TopAppBarElevation,
	) {
		TextField(modifier = Modifier
			.fillMaxWidth(),
			value = text,
			onValueChange = {
				onTextChange(it)
			},
			placeholder = {
				Text(
					modifier = Modifier
					.alpha(ContentAlpha.medium),
					text = "Search here...",
					color = Color.Black
				)
			},
			textStyle = TextStyle(
				fontSize = MaterialTheme.typography.subtitle1.fontSize,
				color = Color.Green
			),
			singleLine = true,
			leadingIcon = {
				IconButton(
					modifier = Modifier
						.alpha(ContentAlpha.medium),
					onClick = {}
				) {
					Icon(
						imageVector = Icons.Default.Search,
						contentDescription = "Search Icon",
						tint = Color.Black
					)
				}
			},
			trailingIcon = {
				IconButton(
					onClick = {
						if (text.isNotEmpty()) {
							onTextChange("")
						} else {
							onCloseClicked()
						}
					}
				) {
					Icon(
						imageVector = Icons.Default.Close,
						contentDescription = "Close Icon",
						tint = Color.Black
					)
				}
			},
			keyboardOptions = KeyboardOptions(
				imeAction = ImeAction.Search
			),
			keyboardActions = KeyboardActions(
				onSearch = {
					onSearchClicked(text)
				}
			),
			colors = TextFieldDefaults.textFieldColors(
				backgroundColor = Color.Transparent,
				cursorColor = Color.Black.copy(alpha = ContentAlpha.medium)
			))
	}
}

@Composable
@Preview
fun SearchAppBarPreview() {
	SearchAppBar(
		text = "Search articles...",
		onTextChange = {},
		onCloseClicked = {},
		onSearchClicked = {}
	)
}