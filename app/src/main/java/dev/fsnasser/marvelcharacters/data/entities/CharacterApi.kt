package dev.fsnasser.marvelcharacters.data.entities

data class CharacterApi (
    val copyright: String,
    val data: Data
) {
    data class Data (
        val results: List<Result>
    ) {
        data class Result (
            val id: Long,
            val name: String,
            val description: String,
            val thumbnail: Thumbnail
        ) {
            data class Thumbnail (
                val path: String,
                val extension: String
            )
        }
    }
}