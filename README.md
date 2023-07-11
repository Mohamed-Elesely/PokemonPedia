App Overview
This app allows users to browse and view information about different Pokemon. The app consists of two screens: PokemonListScreen and PokemonDetailScreen.

PokemonListScreen
The PokemonListScreen is the main screen of the app. It displays a list of Pokemon and provides a search functionality. Here's an overview of its components:

SearchBar: A custom Composable function that displays a search bar where users can enter search queries to find specific Pokemon.
PokemonList: A Composable function that renders the list of Pokemon. It uses a LazyColumn to display the Pokemon entries.
RetrySection: A Composable function that displays an error message and a retry button if there was an error while loading Pokemon data.
PokemonRow: A Composable function that represents a row of Pokemon entries in the list.

PokemonDetailScreen
The PokemonDetailScreen is the screen that shows detailed information about a specific Pokemon. Here's an overview of its components:

PokemonDetailTopSection: A Composable function that displays a back button for navigation to the previous screen.
PokemonDetailStateWrapper: A Composable function that handles the different states of loading Pokemon data, such as loading, success, and error.
PokemonDetailSection: A Composable function that displays the main details of the Pokemon, including its name, types, weight, and height.
PokemonTypeSection: A Composable function that shows the types of the Pokemon.
PokemonDetailDataSection: A Composable function that displays the weight and height of the Pokemon.
PokemonBaseStats: A Composable function that shows the base stats of the Pokemon.

Architecture and Libraries Used
The app follows the MVVM (Model-View-ViewModel) architecture pattern. Here's an overview of the libraries and concepts used:

Kotlin: The programming language used for developing the app.
Jetpack Compose: The modern UI toolkit used for building the user interface of the app.
Navigation Components: Used for implementing navigation between different screens in the app.
Coroutines: Used for handling asynchronous operations and background tasks in a structured and efficient manner.
Hilt (Dependency Injection): Used for dependency injection, allowing for easier management of dependencies in the app.
Coil: An image loading library used for loading and displaying Pokemon images from URLs.
Retrofit: A networking library used for making HTTP requests to the PokeAPI to fetch Pokemon data.

Overall, the app leverages modern Android development techniques and frameworks to provide a visually appealing and user-friendly Pokemon encyclopedia experience.
