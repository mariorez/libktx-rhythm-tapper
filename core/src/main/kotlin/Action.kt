data class Action(
    val name: Name,
    val type: Type
) {
    enum class Type { START, END }

    enum class Name {
        FULLSCREEN,
        EXIT_FULLSCREEN,
        F,
        G,
        H,
        J
    }
}
