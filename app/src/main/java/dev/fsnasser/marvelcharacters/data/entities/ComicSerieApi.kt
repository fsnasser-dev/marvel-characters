package dev.fsnasser.marvelcharacters.data.entities

data class ComicSerieApi (
    val data: Data
) {
    data class Data (
        val results: List<Result>
    ) {
        data class Result (
            val title: String,
            val thumbnail: Thumbnail
        ) {
            data class Thumbnail (
                val path: String,
                val extension: String
            )
        }
    }
}