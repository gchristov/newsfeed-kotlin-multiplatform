import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
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
	//TODO: appsurface is clashing with text view at the moment

		//TODO: https://stackoverflow.com/questions/65780722/jetpack-compose-how-to-remove-edittext-textfield-underline-and-keep-cursor
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
					color = Color.Gray
				)
			},
			textStyle = TextStyle(
				fontSize = MaterialTheme.typography.subtitle1.fontSize
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
				backgroundColor = Color.White,
				cursorColor = Color.Gray.copy(alpha = ContentAlpha.medium),
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				disabledIndicatorColor = Color.Transparent,
				textColor = Color.Black

			))
	}
}


//			placeholder = {
//				Text(
//					modifier = Modifier
//					.alpha(ContentAlpha.medium),
//					text = "Search here...",
//					color = Color.Gray
//				)
//			},
//			textStyle = TextStyle(
//				fontSize = MaterialTheme.typography.subtitle1.fontSize,
//				color = Color.Black
//			),
//			singleLine = true,
//			leadingIcon = {
//				IconButton(
//					modifier = Modifier
//						.alpha(ContentAlpha.medium),
//					onClick = {}
//				) {
//					Icon(
//						imageVector = Icons.Default.Search,
//						contentDescription = "Search Icon",
//						tint = Color.Black
//					)
//				}
//			},
//			trailingIcon = {
//				IconButton(
//					onClick = {
//						if (text.isNotEmpty()) {
//							onTextChange("")
//						} else {
//							onCloseClicked()
//						}
//					}
//				) {
//					Icon(
//						imageVector = Icons.Default.Close,
//						contentDescription = "Close Icon",
//						tint = Color.Black
//					)
//				}
//			},
//			keyboardOptions = KeyboardOptions(
//				imeAction = ImeAction.Search
//			),
//			keyboardActions = KeyboardActions(
//				onSearch = {
//					onSearchClicked(text)
//				}
//			),
//			colors = TextFieldDefaults.textFieldColors(
//				textColor = Color.Black,
//				disabledTextColor = Color.Gray,
//				backgroundColor = Color.White,
//				focusedIndicatorColor = Color.Transparent,
//				unfocusedIndicatorColor = Color.Transparent,
//				disabledIndicatorColor = Color.Transparent,
//				cursorColor = Color.Black
//			))
//	}
//}

	@Composable
//@Preview
	fun SearchAppBarPreview() {
		SearchAppBar(
			text = "Search articles...",
			onTextChange = {},
			onCloseClicked = {},
			onSearchClicked = {}
		)
	}
